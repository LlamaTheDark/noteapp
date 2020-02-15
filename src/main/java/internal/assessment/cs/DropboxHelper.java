package internal.assessment.cs;

import com.dropbox.core.*;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.*;
import com.dropbox.core.v2.users.FullAccount;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;

public class DropboxHelper extends InfoHelper { //TODO: store the user information in a non-volatile place so the user can keep his/her account linked
    private final String APP_KEY = "xg3bskf1jlnkl4b";
    private final String APP_SECRET = "nslf7h2cxzsykkk";
    private DbxWebAuth webAuth;
    private String accessToken = getDbxAccessToken(); // gets whatever token is already there (or simply "" if no account is linked yet)
    private DbxRequestConfig config = new DbxRequestConfig("csiaNotes");
    private DbxClientV2 client = new DbxClientV2(config, accessToken);
    private ViewController progressControl;

    // constructor
    DropboxHelper() {
        DbxAppInfo appInfo = new DbxAppInfo(APP_KEY, APP_SECRET);
        webAuth = new DbxWebAuth(config, appInfo);
    }
    DropboxHelper(ViewController progressControl){
        DbxAppInfo appInfo = new DbxAppInfo(APP_KEY, APP_SECRET);
        webAuth = new DbxWebAuth(config, appInfo);

        this.progressControl = progressControl;
    }
    // constructor

    boolean accountHasBeenLinked() {
        return accessToken != null && !accessToken.equals("");
    }

    String getAuthUrl() {
        DbxWebAuth.Request webAuthRequest = DbxWebAuth.newRequestBuilder()
                .withNoRedirect()
                .withForceReapprove(Boolean.FALSE)
                .build();
        return webAuth.authorize(webAuthRequest);
    }

    String getClientName() {
        FullAccount account = null;
        try {
            account = client.users().getCurrentAccount();
        } catch (DbxException e) {
            e.printStackTrace();
        }
        return account.getName().getDisplayName();
    }

    String[] finishAuthorization(String code) { // finishes the authorization and returns information about the event
        DbxAuthFinish authFinish;
        try {
            authFinish = webAuth.finishFromCode(code);
            accessToken = authFinish.getAccessToken();
            client = new DbxClientV2(config, accessToken);
            setDbxAccessToken(accessToken);
            setTmpInfo("successful authorization");
            createFolder();
            return new String[]{getClientName(), authFinish.getUserId(), authFinish.getAccessToken()};
        } catch (DbxException ex) {
            setTmpInfo("oof");
            System.err.println("Error in DbxWebAuth.authorize: " + ex.getMessage());
        }
        return new String[3];
    }

    void uploadFiles() { // returns whether or not the sync was successful
        createFolder(); // if the folder has not already been created, it will create the folder;

        UploadTask sync = new UploadTask(client);

        progressControl.rebindProgressBar(sync.progressProperty()); // binds the progress bar to the completion state of the task
        progressControl.rebindSyncLabel(sync.messageProperty());
        progressControl.startLoadingAnimation();

        sync.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, // this code runs when the progress bar hits a progress of '1'
                new EventHandler<WorkerStateEvent>(){
                    @Override
                    public void handle(WorkerStateEvent event) {
                        progressControl.unBindProgressBar();
                        progressControl.resetProgressBar();
                        progressControl.stopLoadingAnimation();
                        progressControl.hideProgressBar();
                        progressControl.resetProgressLabel();
                    }
                });
        new Thread(sync).start();
    }

    void downloadFiles(){
        DownloadTask sync = new DownloadTask(client);

        progressControl.rebindProgressBar(sync.progressProperty()); // binds the progress bar to the completion state of the task
        progressControl.rebindSyncLabel(sync.messageProperty());
        progressControl.startLoadingAnimation();

        sync.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, //
                new EventHandler<WorkerStateEvent>(){

                    @Override
                    public void handle(WorkerStateEvent event) {
                        progressControl.unBindProgressBar();
                        progressControl.resetProgressBar();
                        progressControl.stopLoadingAnimation();
                        progressControl.hideProgressBar();
                    }
                });

        new Thread(sync).start();
    }

    private void createFolder(){
        try {
            CreateFolderResult folder = client.files().createFolderV2("/myNotesFolder");
        } catch (CreateFolderErrorException err) {
            if (err.errorValue.isPath() && err.errorValue.getPathValue().isConflict()) {
            } else {
                err.printStackTrace();
            }
        } catch (Exception err) {
            err.printStackTrace();
        }
    }
}

