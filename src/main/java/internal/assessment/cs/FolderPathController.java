package internal.assessment.cs;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class FolderPathController extends InfoHelper implements Initializable { // inheritance to access the note folder path

    public TextField txtFldPath;
    public Button btnFindFolder;
    public Button btnDone;

    public void openFindFolderWindow(ActionEvent actionEvent) {
        DirectoryChooser findNoteFolder = new DirectoryChooser();
        findNoteFolder.setTitle("Find folder...");
        findNoteFolder.setInitialDirectory(new File(getNoteFolderPath()));
        File selectedDirectory = findNoteFolder.showDialog(null);
        if (selectedDirectory != null) {
            txtFldPath.setText(selectedDirectory.getPath());
        }
    }

    public void closeScene(ActionEvent actionEvent) {
        //System.out.println(txtFldPath.getText());
        if(!txtFldPath.getText().equals("")){setNoteFolderPath(txtFldPath.getText());}
        ((Stage)btnDone.getScene().getWindow()).close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        txtFldPath.setText(getNoteFolderPath());
    }
}
