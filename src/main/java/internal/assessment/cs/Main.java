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

public class Main extends Application {

    public static void main(String[] args) {

        File tmp = new File("C:\\NoteAppData"); // creates a set place for files to be stored
                                                         // allows for non-volatile information storage
        FileHelper fhInfo = new FileHelper(tmp.getPath() + "\\info.sm");
        FileHelper fhTemplates = new FileHelper(tmp.getPath() + "\\templates.sm");

        if (!tmp.exists()){
            tmp.mkdir();
            SMStorageProtocol SMSPInfo = new SMStorageProtocol();
            SMSPInfo.put("noteFolderPath", "C:\\");
            SMSPInfo.put("accessToken", "");
            SMSPInfo.put("tags", new SMArrayItem());
            fhInfo.writeFile(SMSPInfo.toString());

            SMStorageProtocol SMSPTemplates = new SMStorageProtocol();
            SMArrayItem exampleTemplate = new SMArrayItem();
            SMArrayItem templateNames = new SMArrayItem();
            templateNames.add("example template", "");
            exampleTemplate.add("description", "");
            exampleTemplate.add("mathematics", "");
            exampleTemplate.add("date", "");
            SMSPTemplates.put("example template", exampleTemplate);
            SMSPTemplates.put("Template Names", templateNames);
            fhTemplates.writeFile(SMSPTemplates.toString());

            /*
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
             */
        }

        launch(args);
/*
        SMStorageProtocol test = new SMStorageProtocol();

        test.put("testItem", "check 123");

        SMArrayItem SMArray = new SMArrayItem();
        SMArray.add("number one", "1");
        SMArray.add("number two", "2");
        SMArray.add("number three", "penis haha");

        test.put("testArray", SMArray);
        test.put("testFriend", "testValue");

        test.put("testArray", "balls");

        //System.out.println("\n\n\n" + test.toString());
        //System.out.println(SMArray.toString());

/*

        SMStorageProtocol SMSP = SMStorageProtocol.parseStringToSMSP(">testboi=cheese>eatmyballs=poop>whatareyou=what>arr=[whatwhat=12,whatwho=5]>lastone=nice");

        System.out.println("1. " + SMSP);
        SMSP.put("testboi", "whom'st");
        System.out.println("2. " + SMSP);
        SMSP.remove("testboi");
        System.out.println("3. " + SMSP);

        SMStringItem eatMyBalls = (SMStringItem)SMSP.get("eatmyballs");
        System.out.println("4. eatMyBalls: " + eatMyBalls.getValue());

        SMArrayItem array = (SMArrayItem)SMSP.get("arr");

        array.remove("whatwhat");
        SMSP.put("arr", array);

        System.out.println("5. " + SMSP);

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
                 else if(event.getCode()==KeyCode.S && event.isControlDown()){ // save file
                    controller.handleSaveFileAction(new ActionEvent());
                 }
                 else if(event.getCode()==KeyCode.W && event.isControlDown()){ // close window
                    controller.closeTab();
                 }
                 else if (event.getCode() == KeyCode.Q && event.isControlDown()) { // close main application
                    controller.handleExitApplicationAction(new ActionEvent());
                 }
                 else if(event.getCode() == KeyCode.N && event.isControlDown()){ // create new blank note
                    controller.handleCreateNewNoteAction(new ActionEvent());
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