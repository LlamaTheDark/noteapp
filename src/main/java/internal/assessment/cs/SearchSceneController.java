package internal.assessment.cs;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
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

    private LinkedList<String> resultsFileNames = new LinkedList<>();

    public void handleShowResultsAction(ActionEvent actionEvent) {
        lsvResults.getItems().clear();
        txtAlert.setText("");

        if(!txtKeyphrase.getText().equals("")) {
            String tag;
            if(cbChooseTag.getValue()!=null){
                tag = (String)cbChooseTag.getValue();
            }else{
                tag = "";
            }
            File dir = new File(getNoteFolderPath());
            for (File f : dir.listFiles()) {
                FileHelper fh = new FileHelper(f.getPath());
                MatchList ml = fh.searchFileForPhrase(txtKeyphrase.getText(), tag);
                for (int i = 0; i <= ml.getLength() - 1; i++) { // i is the index of the match in the match list
                    lsvResults.getItems().add(ml.toSearchResult(i));
                    resultsFileNames.add(ml.getFilename());
                }
            }
        }
        if(lsvResults.getItems().isEmpty()){
            txtAlert.setText("No results found...");
        }
    }
    public void handleEditAction(ActionEvent actionEvent) {
        setTmpInfo(resultsFileNames.get(lsvResults.getSelectionModel().getSelectedIndex()));
        ((Stage)btnCancel.getScene().getWindow()).close();
    }
    public void handleCancelAction(ActionEvent actionEvent) {
        closeWindow();
    }

    public void handleMouseClickAction(MouseEvent mouseEvent) {
        if(btnEdit.isDisabled()){
            btnEdit.setDisable(false);
        }
        //selectedFilename = lsvResults.getSelectionModel().getSelectedIndex();
    }

    public void handleGoToTagAction(ActionEvent actionEvent) { // when user presses "ENTER" key in txtKeyphrase
        //cbChooseTag.getEditor().textProperty().
        //TODO
        cbChooseTag.getSelectionModel().selectFirst();
    }

    public void closeWindow(){
        ((Stage)btnCancel.getScene().getWindow()).close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        FileHelper jsonFileHelper = new FileHelper(getDataFolderPath() + "\\info.json");
        JSONObject jsonInfo = jsonFileHelper.readToJSONObj();
        JSONArray tags = new JSONArray();

        for(File f : Objects.requireNonNull(new File(getNoteFolderPath()).listFiles())){ // TODO: you should maybe do this somewhere else...
            FileHelper fh = new FileHelper(f.getPath());
            System.out.println((fh.searchFileForTags()));
            for (String tag : fh.searchFileForTags()){
                if(!tags.contains(tag)){
                    tags.add(tag);
                }
            }
        }

        jsonInfo.put("tags", tags);
        jsonFileHelper.writeToFile(jsonInfo.toJSONString());

        for(Object tag : tags) {
            cbChooseTag.getItems().add(tag);
        }
    }


}
