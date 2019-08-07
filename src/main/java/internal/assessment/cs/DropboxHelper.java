package internal.assessment.cs;

import com.dropbox.core.*;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.UploadErrorException;
import com.dropbox.core.v2.users.FullAccount;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;
import java.util.prefs.*;



public class DropboxHelper extends InfoHelper{ //TODO: store the user information in a non-volatile place so the user can keep his/her account linked
    private final String APP_KEY = "xg3bskf1jlnkl4b";
    private final String APP_SECRET = "nslf7h2cxzsykkk";
    private DbxWebAuth webAuth;
    private String accessToken = getDbxAccessToken(); // gets whatever token is already there (or simply "" if no account is linked yet)
    private DbxRequestConfig config = new DbxRequestConfig("csiaNotes");
    private DbxClientV2 client = new DbxClientV2(config ,accessToken);

    // constructor
    public DropboxHelper(){
        DbxAppInfo appInfo = new DbxAppInfo(APP_KEY, APP_SECRET);
        webAuth = new DbxWebAuth(config, appInfo);
    }
    // constructor

    public boolean accountHasBeenLinked() {
        return accessToken!=null&&!accessToken.equals("");
    }

    public String getAuthUrl(){
        DbxWebAuth.Request webAuthRequest = DbxWebAuth.newRequestBuilder()
                .withNoRedirect()
                .withForceReapprove(Boolean.FALSE)
                .build();
        return  webAuth.authorize(webAuthRequest);
    }
    public String getClientName(){
        FullAccount account = null;
        try {
            account = client.users().getCurrentAccount();
        } catch (DbxException e) {
            e.printStackTrace();
        }
        return account.getName().getDisplayName();
    }

    public String[] finishAuthorization(String code){ // finishes the authorization and returns information about the event
        DbxAuthFinish authFinish;
        try {
            authFinish = webAuth.finishFromCode(code);
            accessToken = authFinish.getAccessToken();
            client = new DbxClientV2(config, accessToken);
            setDbxAccessToken(accessToken);
            return new String[]{getClientName(), authFinish.getUserId(), authFinish.getAccessToken()};
        } catch (DbxException ex) {
            System.err.println("Error in DbxWebAuth.authorize: " + ex.getMessage());
        }
        return new String[3];
    }

    public boolean uploadFile(){ // returns whether or not the sync was successful
        // Upload "test.txt" to Dropbox
        try (InputStream in = new FileInputStream(getNoteFolderPath() + "\\Yeetballs.txt")) {
            FileMetadata metadata = client.files().uploadBuilder("/yeetballs.txt")
                    .uploadAndFinish(in);
        } catch (IOException | DbxException e) {
            e.printStackTrace();
        }
        return true;
    }
}

