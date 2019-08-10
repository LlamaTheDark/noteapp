package internal.assessment.cs;

import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
//javafx imports

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.List;

public class Main extends Application {

    public static void main(String[] args) {
        File tmp = new File("C:\\NoteAppData"); // creates a set place for files to be stored TODO: maybe find a way to let the user change this??
                                                         // allows for non-volatile information storage
        FileHelper fhInfo = new FileHelper(tmp.getPath() + "\\info.txt");
        FileHelper fhTags = new FileHelper(tmp.getPath() + "\\tags.txt");
        if (!tmp.exists()){
            tmp.mkdir();
            fhInfo.writeFile("C:\\\n");
            fhTags.writeFile("");
        }

        launch(args);
/*
        DropboxHelper dh = new DropboxHelper();
        dh.uploadFile();
*/
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        primaryStage.setTitle("Notes");
        primaryStage.setScene(new Scene(root, 719, 701));
        primaryStage.show();
    }
}


/*
saying throws IOException is the same as saying
try {...} catch(IOException e) {throw new RuntimeException(e);} except when you throw it in a method def you still have to
                                                                try/catch it when you call the method
 */