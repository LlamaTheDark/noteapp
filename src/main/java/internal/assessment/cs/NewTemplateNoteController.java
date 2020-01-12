package internal.assessment.cs;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.net.URL;
import java.util.ResourceBundle;

public class NewTemplateNoteController extends InfoHelper implements Initializable {

    public ListView lstvwTemplates;
    public Button btnCreate;
    public Button btnCancel;
    public Button btnDelete;

    public void handleCreateNoteAction(ActionEvent actionEvent) {
        if(lstvwTemplates.getSelectionModel().getSelectedItems()!=null){
//            System.out.println(lstvwTemplates.getSelectionModel().getSelectedItem().toString()); // hoping this toString works...
            setTmpInfo(lstvwTemplates.getSelectionModel().getSelectedItem().toString());
            close();
        }
    }

    public void handelCancelAction(ActionEvent actionEvent) {
        close();
    }
    private void close(){
        ((Stage)btnCancel.getScene().getWindow()).close();
    }

    public void handleDeleteTemplateAction(ActionEvent actionEvent) { // does not actually delete the template, just the availability
        if(lstvwTemplates.getSelectionModel().getSelectedItem()!=null){
            FileHelper templatesFile = new FileHelper(getDataFolderPath() + "/templates.json");
/*
            SMStorageProtocol SMTemplates = SMStorageProtocol.parseStringToSMSP(templatesFile.readFileToStr());
            SMArrayItem SMTemplateNames = (SMArrayItem)SMTemplates.get("Template Names");
            SMTemplateNames.remove(lstvwTemplates.getSelectionModel().getSelectedItem().toString());
            SMTemplates.put("Template Names", SMTemplateNames);
            templatesFile.writeToFile(SMTemplates.toString());
            reloadListView();
            */
            JSONObject jsonTemplates = templatesFile.readToJSONObj();
            JSONArray templateNames = (JSONArray)jsonTemplates.get("Template Names");
            templateNames.remove(lstvwTemplates.getSelectionModel().getSelectedItem().toString());
            jsonTemplates.put("Template Names", templateNames);
            templatesFile.writeToFile(jsonTemplates.toJSONString());
            reloadListView();
        }
    }

    public void handleKeyPressed(KeyEvent keyEvent) {
        if(keyEvent.getCode()==KeyCode.DELETE){
            handleDeleteTemplateAction(new ActionEvent());
        }
    }
    private void reloadListView(){
        lstvwTemplates.getItems().clear();
        FileHelper templatesFile = new FileHelper(getDataFolderPath() + "/templates.json");

        /*
        SMStorageProtocol SMTemplates = SMStorageProtocol.parseStringToSMSP(templatesFile.readFileToStr());
        SMArrayItem SMTemplateNames = (SMArrayItem)SMTemplates.get("Template Names");
        for(int i = 0; i < SMTemplateNames.getLength(); i++){
            lstvwTemplates.getItems().add(SMTemplateNames.get(i).getName());
        }
        */
        JSONObject jsonTemplates = templatesFile.readToJSONObj();
        JSONArray templateNames = (JSONArray) jsonTemplates.get("Template Names");
        lstvwTemplates.getItems().addAll(templateNames);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        reloadListView();
    }

    public void handleDoubleClickAction(MouseEvent mouseEvent) {
        if (mouseEvent.getButton().equals(MouseButton.PRIMARY)
                && mouseEvent.getClickCount()==2
        ){
            handleCreateNoteAction(new ActionEvent());
        }
    }
}
