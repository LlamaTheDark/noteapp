package internal.assessment.cs;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class NewTemplateController extends InfoHelper implements Initializable {

    public TextField txtName;
    public ListView lstvwTags;
    public Button btnFinish;
    public Button btnCancel;
    public Text txtPrompt;

    private String filename;

    public NewTemplateController (String filename){
        this.filename = filename;
    }

    public void handleDoneAction(ActionEvent actionEvent){
        if(txtName.getText().equals("")){
            txtPrompt.setText("Please name your template:");
        }else{
            setTmpInfo(txtName.getText());
        }
        close();
    }

    public void handleCancelAction(ActionEvent actionEvent) {
        close();
    }

    public void close(){
        ((Stage)btnCancel.getScene().getWindow()).close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        FileHelper currentFile = new FileHelper(getNoteFolderPath() + "\\" + filename);
        lstvwTags.getItems().addAll(currentFile.searchFileForTags());
    }
}