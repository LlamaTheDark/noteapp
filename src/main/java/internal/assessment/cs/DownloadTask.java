package internal.assessment.cs;

import com.dropbox.core.DbxException;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import javafx.concurrent.Task;

import java.io.*;
import java.util.List;
import java.util.Objects;

public class DownloadTask extends Task {
    private InfoHelper ih = new InfoHelper();
    private DbxClientV2 client;

    @Override
    protected Object call() throws Exception {
        File dir = new File(ih.getNoteFolderPath());
        File[] localFiles = dir.listFiles();
        int totalFiles = getFolderMetaData().size();
        int downloadedFiles = 0;

        this.updateMessage("Syncing from dropbox: DO NOT SHUTDOWN");

        boolean existsOnDropbox;
        for (File f : localFiles){
            existsOnDropbox = false;
            for (Metadata m : getFolderMetaData()){
                if(f.getName().equals(m.getName())){
                    existsOnDropbox = true;
                }
            }
            if(!existsOnDropbox){
                this.updateMessage("Deleting local file " + f.getName() + " ...");
                if(deleteLocalFile(f.getAbsolutePath())){
                    this.updateMessage(f.getName() + " successfully deleted.");
                }
            }
        }
        for (File f : localFiles){
            for(Metadata dbxF : Objects.requireNonNull(getFolderMetaData())){
                //System.out.println(f.getName() + ", "+ dbxF.getName());
                if (f.getName().equals(dbxF.getName())){
                    this.updateMessage("Locally overwriting: " + f.getName());
                    if(deleteLocalFile(f.getAbsolutePath())){
                        if(downloadFile(f)){
                            this.updateMessage("Successfully overwritten: " + f.getName());
                            downloadedFiles++;
                            this.updateProgress((double)downloadedFiles, (double)totalFiles);
                        }// the File object should still exist so it will be fine.
                    }/*else{
                        //this.updateMessage("ERROR: Could not delete file.");
                    }*/
                }else if(downloadFile(new File(ih.getNoteFolderPath() + "/" + dbxF.getName()))){
                    this.updateMessage("Successfully downloaded: " + f.getName());
                    downloadedFiles++;
                    this.updateProgress((double)downloadedFiles, (double)totalFiles);
                }
            }
        }
        this.updateMessage("Done!");
        return null;
    }

    DownloadTask(DbxClientV2 client){
        this.client = client;
    }

    private boolean downloadFile(File f){
        OutputStream downloadFile = null;
        try {
            downloadFile = new FileOutputStream(f.getAbsolutePath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            FileMetadata metadata = client.files().downloadBuilder("/myNotesFolder/" + f.getName())
                    .download(downloadFile);
            return true;
        } catch (DbxException | IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteLocalFile(String filePath){ // returns the success of deleting the file
        File f = new File(filePath);
        if (f.delete()){
            return true;
        }else{
            return false;
        }
    }

    private List<Metadata> getFolderMetaData(){
        try {
            ListFolderResult data = client.files().listFolder("/myNotesFolder");
            return data.getEntries();
        } catch (DbxException e) {
            e.printStackTrace();
        }
        return null;
    }
}
