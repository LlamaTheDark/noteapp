package internal.assessment.cs;

import org.json.simple.JSONObject;

class InfoHelper{
    private String dataFolderPath = "NoteAppData";
    private FileHelper fh = new FileHelper(dataFolderPath + "/info.json");

    private static String tmpInfo;
    static String getTmpInfo() { return tmpInfo; }
    static void setTmpInfo(String newTmpFileName) { tmpInfo = newTmpFileName; }

    String getDataFolderPath(){return dataFolderPath;}

    void setNoteFolderPath(String noteFolderPath){
        JSONObject tmp = fh.readToJSONObj();
        tmp.put("noteFolderPath", noteFolderPath);
        fh.writeToFile(tmp.toJSONString());
    }
    String getRepositoryPath() {
        return (String)fh.readToJSONObj().get("noteFolderPath");
    }

    String getDbxAccessToken(){
        return (String)fh.readToJSONObj().get("accessToken");
    }
    void setDbxAccessToken(String dbxAccessToken){
        JSONObject tmp = fh.readToJSONObj();
        tmp.put("accessToken", dbxAccessToken);
        fh.writeToFile(tmp.toJSONString());
    }

    void setStyleType(String newStyle){
        JSONObject tmp = fh.readToJSONObj();
        tmp.put("style", newStyle);
        fh.writeToFile(tmp.toJSONString());
    }
    String getStyleType(){
        return (String)fh.readToJSONObj().get("style");
    }
}


/* in info.txt

line 1: location of Note Folder
line 2: dropbox access token



 */