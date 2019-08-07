package internal.assessment.cs;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.ResourceBundle;

public class SearchSceneController implements Initializable {

    public Button btnShowResults;
    public ComboBox cbChooseTag;
    public ListView<String> lsvResults;
    public Button btnEdit;
    public Button btnView;

    public void handleShowResultsAction(ActionEvent actionEvent) {

        lsvResults.getItems().add("uhuh");
    }

    public void handleEditAction(ActionEvent actionEvent) {

    }

    public void handleViewAction(ActionEvent actionEvent) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
