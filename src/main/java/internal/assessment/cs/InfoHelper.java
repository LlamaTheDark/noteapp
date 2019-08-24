package internal.assessment.cs;

import org.json.simple.JSONObject;

public class InfoHelper{
    private String infoFolderPath = "C:\\NoteAppData";
    private FileHelper fh = new FileHelper(infoFolderPath + "\\info.json");

    private static String tmpInfo;
    public static String getTmpInfo() { return tmpInfo; }
    public static void setTmpInfo(String newTmpFileName) { tmpInfo = newTmpFileName; }

    public String getInfoFolderPath(){return infoFolderPath;}

    public void setNoteFolderPath(String noteFolderPath){
        JSONObject tmp = fh.readToJSONObj();
        tmp.put("noteFolderPath", noteFolderPath);
        fh.writeToFile(tmp.toJSONString());

        /*if(fh.readFileToArr().length > 1) {
            fh.writeToFile(noteFolderPath + "\n" + fh.readFileToArr()[1]);
        }else{
            fh.writeToFile(noteFolderPath + "\n");
        }*/
    }
    public String getNoteFolderPath() {
        return (String)fh.readToJSONObj().get("noteFolderPath");
    }

    public String getDbxAccessToken(){
        return (String)fh.readToJSONObj().get("accessToken");
    }
    public void setDbxAccessToken(String dbxAccessToken){
        JSONObject tmp = fh.readToJSONObj();
        tmp.put("accessToken", dbxAccessToken);
        fh.writeToFile(tmp.toJSONString());
    }

    public String pathToURL(String path){
        return "file:///C:/Users/mrsea/InternalAssessmentProjects/csiaWebApp/text.html"; // TODO: make this NOT have text.html
    }
}


/* in info.txt

line 1: location of Note Folder
line 2: dropbox access token



 */