package internal.assessment.cs;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.skin.ComboBoxListViewSkin;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class SearchSceneController extends InfoHelper implements Initializable {

    public Button btnShowResults;
    public ComboBox cbChooseTag;
    public ListView<String> lsvResults;
    public Button btnEdit;
    public Button btnCancel;
    public TextField txtKeyword;
    public Text txtAlert;

    private LinkedList<String> resultsFileNames = new LinkedList<>();

    public void handleShowResultsAction(ActionEvent actionEvent) {
        lsvResults.getItems().clear();
        txtAlert.setText("");

        if(!txtKeyword.getText().equals("")) {
            String tag;
            if(cbChooseTag.getValue()!=null){
                tag = (String)cbChooseTag.getValue();
            }else{
                tag = "";
            }
            File dir = new File(getNoteFolderPath());
            for (File f : dir.listFiles()) {
                FileHelper fh = new FileHelper(f.getPath());
                MatchList ml = fh.searchFileForPhrase(txtKeyword.getText(), tag);
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
        setTmpFileName(resultsFileNames.get(lsvResults.getSelectionModel().getSelectedIndex()));
        ((Stage)btnCancel.getScene().getWindow()).close();
    }

    public void handleViewAction(ActionEvent actionEvent) {

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
        cbChooseTag.getSelectionModel().selectFirst();
    }

    public void closeWindow(){
        ((Stage)btnCancel.getScene().getWindow()).close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //cbChooseTag.getItems().addAll(new String[]{"yes please", "do it hard"});
    }


}
