package internal.assessment.cs;

public class InfoHelper{
    private String infoFilePath = "C:\\NoteAppData\\info.txt";
    private FileHelper fh = new FileHelper(infoFilePath);
    private String[] infoFileContent = (fh.readFile());

    private static String tmpFileName;
    public static String getTmpFileName() { return tmpFileName; }
    public static void setTmpFileName(String newTmpFileName) { tmpFileName = newTmpFileName; }

    public String getInfoFilePath(){return infoFilePath;}
    public String[] getInfoContent(){return infoFileContent;}

    public void setNoteFolderPath(String noteFolderPath){
        FileHelper infoHelp = new FileHelper(infoFilePath);
        if(infoFileContent.length > 1) {
            infoHelp.writeToFile(noteFolderPath + "\n" + infoFileContent[1]);
        }else{
            infoHelp.writeToFile(noteFolderPath + "\n");
        }
        infoFileContent = fh.readFile(); // refreshes information
    }
    public String getNoteFolderPath() {
        return infoFileContent[0]; // the 0th index is the value of the path
    }

    public String getDbxAccessToken(){
        if(infoFileContent.length > 1){
            return infoFileContent[1];
        }else{
            return "";
        }
    }
    public void setDbxAccessToken(String dbxAccessKey){
        FileHelper infoHelp = new FileHelper(infoFilePath);
        infoHelp.writeToFile(infoFileContent[0] + "\n" + dbxAccessKey);
        infoFileContent = fh.readFile();
    }


}


/* in info.txt

line 1: location of Note Folder
line 2: dropbox access token



 */