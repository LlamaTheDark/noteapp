package internal.assessment.cs;

import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
//javafx imports

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Main extends Application {

    public static void main(String[] args) {
        File tmp = new File("C:\\NoteAppData"); // creates a set place for files to be stored TODO: maybe find a way to let the user change this??
                                                         // allows for non-volatile information storage
        FileHelper fhInfo = new FileHelper(tmp.getPath() + "\\info.json");
        FileHelper fhTemplates = new FileHelper(tmp.getPath() + "\\templates.json");

        if (!tmp.exists()){
            tmp.mkdir();
            JSONObject jsonINFO = new JSONObject();
            jsonINFO.put("noteFolderPath", "C:\\");
            jsonINFO.put("accessToken", "");
            jsonINFO.put("tags", new JSONArray());
            fhInfo.writeFile(jsonINFO.toJSONString());


            JSONObject templates = new JSONObject();
            JSONArray exampleTemplate = new JSONArray();
            JSONArray templateNames = new JSONArray();
            templateNames.add("example template");
            exampleTemplate.add("description");
            exampleTemplate.add("mathematics");
            exampleTemplate.add("date");
            templates.put("example template", exampleTemplate);
            templates.put("Template Names", templateNames);
            fhTemplates.writeFile(templates.toJSONString());
        }

        launch(args);
/*
        DropboxHelper dh = new DropboxHelper();
        dh.uploadFile();
*/
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        //Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));

        FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
        Parent root = loader.load();
        ViewController controller = loader.getController();
        Scene mainScene = new Scene(root, 719, 701);
        primaryStage.setScene(mainScene);
        primaryStage.show();

        mainScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode()==KeyCode.S && event.isControlDown()){
                    controller.handleSaveFileAction(new ActionEvent());
                }
                if(event.getCode()==KeyCode.S
                        && event.isShiftDown()
                        && event.isControlDown()
                ){
                    controller.handleSaveFileAsAction(new ActionEvent());
                }
                if(event.getCode()==KeyCode.W && event.isControlDown()){
                    controller.closeTab();
                }
                if (event.getCode() == KeyCode.Q && event.isControlDown()) {
                    controller.handleExitApplicationAction(new ActionEvent());
                }
            }
        }); // key pressed events (e.g. ctrl+S -> save file)
    }
}


/*
saying throws IOException is the same as saying
try {...} catch(IOException e) {throw new RuntimeException(e);} except when you throw it in a method def you still have to
                                                                try/catch it when you call the method
 */

// todo: implement folder support. For uploads/downloads/general syncing AND for searching files. (may have to implement recursive function)
//