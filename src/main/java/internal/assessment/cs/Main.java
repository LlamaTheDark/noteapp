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
        FileHelper README = new FileHelper(tmp.getPath() + "\\README.txt");

        README.writeFile("# WELCOME to the csia Note App\n" +
                "### Before you can sync files with Dropbox you must authorize your account through the 'Sync' menu.\n" +
                "\n" +
                "* Files will sync automatically from Dropbox upon launch (if there is wifi).\n" +
                "* You can manually sync up with Dropbox from the 'Sync' menu.\n" +
                "\n" +
                "Supported Keyboard shortcuts:\n" +
                "\n" +
                "\tCtrl+S: Saves current file (will default to 'Save as'... if there is no existing file with that name.)\n" +
                "\n" +
                "\tCtrl+Shift+S: Brings up the 'Save as...' window.\n" +
                "\n" +
                "\tCtrl+W: Closes current tab.\n" +
                "\n" +
                "\tCtrl+Q: Closes the main application window.\n" +
                "\n" +
                "Files also have support for **markdown** through the *flexmark* java library extension\n" +
                "\n" +
                "For help with using markdown, see [this helpful guide](https://github.com/adam-p/markdown-here/wiki/Markdown-Cheatsheet).\n" +
                "\n" +
                "\n" +
                "\n" +
                "You can also #tag# information to be searched through later.\n" +
                "\n" +
                "To tag a file place a '#' before and after the what you want to call your tag.\n" +
                "\n" +
                "This will tag any information you place directly below the tag, as seperated by two new lines.\n" +
                "\n" +
                "If you would like to tag more information than this, utilize the *end tag* feature. in which\n" +
                "you create another tag of the same name where you wish it to end, but mark this with a '/' just before the tag name.\n");

        if (!tmp.exists()){
            tmp.mkdir();
            JSONObject jsonINFO = new JSONObject();
            jsonINFO.put("noteFolderPath", "C:\\");
            jsonINFO.put("accessToken", "");
            jsonINFO.put("tags", new JSONArray());
            fhInfo.writeFile(jsonINFO.toJSONString());
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