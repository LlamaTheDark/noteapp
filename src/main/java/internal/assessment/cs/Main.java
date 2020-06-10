package internal.assessment.cs;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
//javafx imports

import java.io.File;
import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Main extends Application { // TODO: CHANGE ALL DIRECTORIES TO FIT WINDOWS, MAC OSX, AND LINUX

    InfoHelper ih = new InfoHelper();

    public static void main(String[] args) {
        File tmp = new File("NoteAppData"); // creates a set place for files to be stored
                                                       // allows for non-volatile information storage
        FileHelper fhInfo = new FileHelper(tmp.getPath() + "/info.json");
        FileHelper fhTemplates = new FileHelper(tmp.getPath() + "/templates.json");

        if (!tmp.exists()){
            tmp.mkdir();

            JSONObject jsonINFO = new JSONObject();
            jsonINFO.put("noteFolderPath", "/"); // stores note storage folder path
            jsonINFO.put("accessToken", ""); // stores Dropbox auth token
            jsonINFO.put("tags", new JSONArray()); // stores all the tags it detects in the system (for the dropdown menu in search)
            jsonINFO.put("style", "dark");
            fhInfo.writeFile(jsonINFO.toJSONString());

            JSONObject templates = new JSONObject();
            JSONArray exampleTemplate = new JSONArray();
            JSONArray templateNames = new JSONArray();
            templateNames.add("example template"); // has an example template automatically loaded (the user can delete this)
            exampleTemplate.add("description");
            exampleTemplate.add("mathematics");
            exampleTemplate.add("date");
            templates.put("example template", exampleTemplate);
            templates.put("Template Names", templateNames); // it has to use template names to reference the template variables
            fhTemplates.writeFile(templates.toJSONString());

        }

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        //Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        ih.setStyleType("dark");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
        Parent root = loader.load();
        ViewController controller = loader.getController();
        Scene mainScene = new Scene(root, 719, 701);
        mainScene.getStylesheets().add("/css/style_main_" + ih.getStyleType() + ".css");
        primaryStage.setScene(mainScene);
        primaryStage.setTitle("Notes");
        primaryStage.show();

        mainScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                 if(event.getCode() == KeyCode.N // create new note from a template
                        && event.isShiftDown()
                        && event.isControlDown()
                 ){
                    controller.handleNewNoteFromTemplateAction(new ActionEvent());
                 }
                 else if(event.getCode()==KeyCode.S // save file as...
                        && event.isShiftDown()
                        && event.isControlDown()
                 ){
                    controller.handleSaveFileAsAction(new ActionEvent());
                 }
                 else if(event.getCode() == KeyCode.T // create new template from currently selected note
                        && event.isShiftDown()
                        && event.isControlDown()
                 ){
                    controller.handleNewTemplateFromNoteAction(new ActionEvent());
                 }
                 else if(event.getCode()==KeyCode.S // save file
                         && event.isControlDown()
                 ){
                    controller.handleSaveFileAction(new ActionEvent());
                 }
                 else if(event.getCode()==KeyCode.W // close window
                         && event.isControlDown()
                 ){
                    controller.closeTab();
                 }
                 else if (event.getCode() == KeyCode.Q // close main application
                         && event.isControlDown()
                 ){
                    controller.handleExitApplicationAction(new ActionEvent());
                 }
                 else if(event.getCode() == KeyCode.N // create new blank note
                         && event.isControlDown()
                 ){
                     controller.handleCreateNewNoteAction(new ActionEvent());
                 }
                 else if(event.getCode() == KeyCode.O // bring up open file window
                         && event.isControlDown()
                 ){
                     controller.handleOpenFileAction(new ActionEvent());
                 }

            }
        }); // key pressed events (e.g. ctrl+S -> save file)
    }

    private String getRootDir(){
        String systemRoot = System.getenv("SystemDrive");
        if(systemRoot!=null){
            return systemRoot;
        }
        return "/";
    }
}


/*
saying throws IOException is the same as saying
try {...} catch(IOException e) {throw new RuntimeException(e);} except when you throw it in a method def you still have to
                                                                try/catch it when you call the method
 */

// todo: implement folder support. For uploads/downloads/general syncing AND for searching files. (may have to implement recursive function)
//