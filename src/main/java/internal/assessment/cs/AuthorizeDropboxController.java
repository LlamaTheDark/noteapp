package internal.assessment.cs;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

public class AuthorizeDropboxController extends DropboxHelper implements Initializable {
    public WebView wbvwAuthWebpage;
    public TextField txtAuthCode;
    public Button btnDone;
    private WebEngine webEngine;
    public Button btnCancel;

    public void handleDoneAction(ActionEvent actionEvent) {
        String authCode = txtAuthCode.getText();
        if(!authCode.equals("")){
            String[] finishInfo = finishAuthorization(authCode);
            Model.showInformationMsg("Account Successfully Linked", "Authorization complete." +
                    "\n - User Name: " + finishInfo[0] +
                    "\n - User ID: " + finishInfo[1] +
                    "\n - Access Token: " + finishInfo[2]);
            cancel();
        }else{
            Model.showErrorMsg("No code has been entered.", "Copy and paste the given code into the space above and try again.");
        }
    }
    public void handleCancelAction(ActionEvent actionEvent) {
        cancel();
    }

    public void cancel(){
        ((Stage)btnDone.getScene().getWindow()).close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) { // runs AFTER controller and fxml has been initialized
        webEngine = wbvwAuthWebpage.getEngine();
        webEngine.load(getAuthUrl());
    }
}