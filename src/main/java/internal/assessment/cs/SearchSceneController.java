package internal.assessment.cs;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.net.URL;
import java.util.LinkedList;
import java.util.Objects;
import java.util.ResourceBundle;

public class SearchSceneController extends InfoHelper implements Initializable {

    public Button btnShowResults;
    public ComboBox cbChooseTag;
    public ListView<String> lsvResults;
    public Button btnEdit;
    public Button btnCancel;
    public TextField txtKeyphrase;
    public Text txtAlert;
    private int numResults;

    private LinkedList<String> resultsFileNames = new LinkedList<>();

    public void handleShowResultsAction(ActionEvent actionEvent) {
        lsvResults.getItems().clear();
        resultsFileNames.clear();
        txtAlert.setText("");
        numResults = 0;
        File dir = new File(getRepositoryPath());

        if(!txtKeyphrase.getText().equals("")) {
            String tag;
            if(cbChooseTag.getValue()!=null){
                tag = (String)cbChooseTag.getValue();
            }else{
                tag = "";
            }

            for (File f : dir.listFiles()) {
                FileHelper fh = new FileHelper(f.getPath());
                MatchList ml = fh.searchFileForPhraseByTag(txtKeyphrase.getText(), tag);
                for (int i = 0; i <= ml.getLength() - 1; i++) { // i is the index of the match in the match list
                    lsvResults.getItems().add(ml.toSearchResult(i));
                    resultsFileNames.add(ml.getFilename());
                    numResults++;
                }
            }
        }else{
            for (File f : dir.listFiles()){
                FileHelper fh = new FileHelper(f.getPath());
                if(cbChooseTag.getValue()!=null && fh.containsTag((String)cbChooseTag.getValue())){
                    lsvResults.getItems().add("Found the tag \'" + cbChooseTag.getValue() + "\' in file: \'" + f.getName() + "\'");
                    resultsFileNames.add(f.getName());
                    numResults++;
                }
            }
        }
        if(lsvResults.getItems().isEmpty()){
            txtAlert.setText("No results found...");
        }else{
            txtAlert.setText(numResults + " matches found.");
        }
    }
    public void handleEditAction(ActionEvent actionEvent) {
        try{
            setTmpInfo(resultsFileNames.get(lsvResults.getSelectionModel().getSelectedIndex()));
            ((Stage)btnCancel.getScene().getWindow()).close();
        }catch(java.lang.IndexOutOfBoundsException e){

        }
    }
    public void handleCancelAction(ActionEvent actionEvent) {
        closeWindow();
    }

    public void handleMouseClickAction(MouseEvent mouseEvent) {
        btnEdit.setDisable(false); //TODO: fix this garbage
        if (mouseEvent.getButton().equals(MouseButton.PRIMARY)
                && mouseEvent.getClickCount()==2
        ){
            handleEditAction(new ActionEvent());
        }
        //selectedFilename = lsvResults.getSelectionModel().getSelectedIndex();
    }

    public void handleGoToTagAction(ActionEvent actionEvent) { // when user presses "ENTER" key in txtKeyphrase
        cbChooseTag.requestFocus();
    }
    public void handleGoToSearchAction(KeyEvent keyEvent){
        if(keyEvent.getCode() == KeyCode.ENTER) {
            btnShowResults.requestFocus();
        }
    }

    private void closeWindow(){
        ((Stage)btnCancel.getScene().getWindow()).close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //cbChooseTag.getScene().getStylesheets().add("/css/style_main_dark.css");

        FileHelper jsonFileHelper = new FileHelper(getDataFolderPath() + "/info.json");
        JSONObject jsonInfo = jsonFileHelper.readToJSONObj();
        JSONArray tags = new JSONArray();

        for(File f : Objects.requireNonNull(new File(getRepositoryPath()).listFiles())){
            FileHelper fh = new FileHelper(f.getPath());
            for (String tag : fh.searchForTags()){
                if(!tags.contains(tag)){
                    tags.add(tag);
                }
            }
        }

        // to alphabetize
        String[] tagsList = new String[tags.size()];
        for(int i = 0; i < tagsList.length; i++){
            tagsList[i] = (String)tags.get(i);
        }
        Model.bubbleSort(tagsList);
        tags.clear();
        for(String tag: tagsList){
            tags.add(tag);
        }
        //


        jsonInfo.put("tags", tags);
        jsonFileHelper.writeToFile(jsonInfo.toJSONString());

        for(Object tag : tags) {
            cbChooseTag.getItems().add(tag);
        }
    }


}
