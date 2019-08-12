package internal.assessment.cs;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.*;
import javafx.concurrent.Task;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

public class SyncTask extends Task {

    private InfoHelper ih = new InfoHelper();
    private DbxClientV2 client;

    public SyncTask(DbxClientV2 client){
        this.client = client;
    }

    @Override
    protected Object call() throws Exception {
        File dir = new File(ih.getNoteFolderPath());
        File[] localFiles = dir.listFiles();
        int totalFiles = localFiles.length;
        int uploadedFiles = 0;

        // test to see if file already exists {
        //      if so, delete it and then upload it
        // }else{
        //      upload the file
        // }
        // test to see if a file that is in dropbox is found locally. {
        //      if it's not, delete it from dropbox. (only do this if you are updating the local library with dropbox upon launching the application)
        // }


        // first deletes all files not existing locally.
        boolean existsLocally;
        for (Metadata m : getDropboxFiles()){
            existsLocally = false;
            for (File f : localFiles){
                if(f.getName().equals(m.getName())){
                    existsLocally = true;
                }
            }
            if(!existsLocally){
                this.updateMessage("Deleting from Dropbox: " + m.getName());
                deleteDropboxFile(m.getName());
            }
        }

        for (File f : localFiles) {
            this.updateMessage("Syncing file: " + f.getName());
            if(fileExistsInDropbox(f)){
                this.updateMessage("Overwriting: " + f.getName());
                deleteDropboxFile(f.getName());
            }
            if(uploadFile(f)){
                uploadedFiles++;
            }else{
                this.updateMessage("Error syncing: " + f.getName());
            }
            this.updateProgress((double)uploadedFiles, (double)totalFiles);
        }
        this.updateMessage("Done!");
        return null;
    }

    public boolean uploadFile(File f){ // returns success state;
        try (InputStream in = new FileInputStream(f.getAbsolutePath())) {
            FileMetadata metadata = client.files().uploadBuilder("/myNotesFolder/" + f.getName())
                    .uploadAndFinish(in);
        } catch (IOException | DbxException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean fileExistsInDropbox(File f){
        try {
            client.files().getMetadata("/myNotesFolder/" + f.getName());
        } catch (GetMetadataErrorException e){
            if (e.errorValue.isPath() && e.errorValue.getPathValue().isNotFound()) {
                //System.out.println("File not found.");
                return false;
            } else {
                e.printStackTrace();
            }
        } catch (DbxException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean deleteDropboxFile(String filename){
        try {
            DeleteResult metadata = client.files().deleteV2("/myNotesFolder/" + filename);
        } catch (DbxException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public List<Metadata> getDropboxFiles(){
        ListFolderResult metadata;
        try {
            metadata = client.files().listFolder("/myNotesFolder/");
            return metadata.getEntries();
        } catch (DbxException e) {
            e.printStackTrace();
        }
        return null;
    }
}
