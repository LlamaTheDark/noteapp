package internal.assessment.cs;

public class InfoHelper{
    private String infoFilePath = "C:\\NoteAppData\\info.txt";
    private FileHelper fh = new FileHelper(infoFilePath);

    private static String tmpInfo;
    public static String getTmpInfo() { return tmpInfo; }
    public static void setTmpInfo(String newTmpFileName) { tmpInfo = newTmpFileName; }

    public String getInfoFilePath(){return infoFilePath;}

    public void setNoteFolderPath(String noteFolderPath){
        if(fh.readFile().length > 1) {
            fh.writeToFile(noteFolderPath + "\n" + fh.readFile()[1]);
        }else{
            fh.writeToFile(noteFolderPath + "\n");
        }
    }
    public String getNoteFolderPath() {
        return fh.readFile()[0]; // the 0th index is the value of the path
    }

    public String getDbxAccessToken(){
        FileHelper infoHelp = new FileHelper(infoFilePath);
        if(infoHelp.readFile().length > 1){
            return infoHelp.readFile()[1];
        }else{
            return "";
        }
    }
    public void setDbxAccessToken(String dbxAccessKey){
        fh.writeToFile(fh.readFile()[0] + "\n" + dbxAccessKey);
    }


}


/* in info.txt

line 1: location of Note Folder
line 2: dropbox access token



 */