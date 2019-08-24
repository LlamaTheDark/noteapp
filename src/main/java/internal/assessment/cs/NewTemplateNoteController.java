package internal.assessment.cs;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.net.URL;
import java.util.ResourceBundle;

public class NewTemplateNoteController extends InfoHelper implements Initializable {

    public ListView lstvwTemplates;
    public Button btnCreate;
    public Button btnCancel;

    public void handleCreateNoteAction(ActionEvent actionEvent) {
        if(lstvwTemplates.getSelectionModel().getSelectedItems()!=null){
            System.out.println(lstvwTemplates.getSelectionModel().getSelectedItem().toString()); // hoping this toString works...
            setTmpInfo(lstvwTemplates.getSelectionModel().getSelectedItem().toString());
            close();
        }
    }

    public void handelCancelAction(ActionEvent actionEvent) {
        close();
    }

    public void close(){
        ((Stage)btnCancel.getScene().getWindow()).close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        FileHelper templatesFile = new FileHelper(getDataFolderPath() + "\\templates.json");
        JSONObject jsonTemplates = templatesFile.readToJSONObj();
        JSONArray templateNames = (JSONArray)jsonTemplates.get("Template Names");
        lstvwTemplates.getItems().addAll(templateNames);
    }
}
