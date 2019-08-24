package internal.assessment.cs;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class FileHelper{

    Model model = new Model();
    JSONParser parser = new JSONParser();

    final String WITH_DELIMITERS = "\\t|,|;|!|-|:|@|_|\\*|/";

    final String FILEPATH; // each FileHelper class is specific to one file.
    // Thus, the FILENAME variable is final and cannot be
    // changed after instantiation in the constructor

    public FileHelper(String filePath){
        FILEPATH = filePath; // File name is instantiated specific to each object
    }

    public String[] readFileToArr(){ // will return a single string file read from a document with each line separation shown through '\n'
        String[] fileRead = new String[500]; // fileRead file is the string containing the read file to be returned by the readFileToArr() method.
        int count = 0;
        String line;   // will be set equal to each line to append to fileRead and test for the end of the document
        try{
            FileReader fileReader = new FileReader(FILEPATH); // creates FileReader object used to read the file
            BufferedReader bufferedReader = new BufferedReader(fileReader); // creates BufferedReader object used to read
                                                                            // the lines of the file specified in fileReader
            while ((line = bufferedReader.readLine()) != null){ // makes sure the end of the document has not been reached
                fileRead[count] = line; // appends the current line (and a new line: '\n')
                count++;
            }
            bufferedReader.close(); // closes the bufferedReader
        }
        catch (FileNotFoundException a){System.out.println("Could not find file " + "\'" + (new File(FILEPATH).getName()) + "\'");} // catches potential errors
        catch (IOException e){System.out.println("Error reading file"  + "\'" + (new File(FILEPATH).getName()) + "\'");}
        return model.shortenStringArrToIndex(fileRead, count); // returns the final output of the read string in array form
    }
    public String readFileToStr(){
        String fileRead = ""; // fileRead file is the string containing the read file to be returned by the readFileToArr() method.
        int count = 0;
        String line;   // will be set equal to each line to append to fileRead and test for the end of the document
        try{
            FileReader fileReader = new FileReader(FILEPATH); // creates FileReader object used to read the file
            BufferedReader bufferedReader = new BufferedReader(fileReader); // creates BufferedReader object used to read
            // the lines of the file specified in fileReader
            while ((line = bufferedReader.readLine()) != null){ // makes sure the end of the document has not been reached
                fileRead += line+"\n"; // appends the current line (and a new line: '\n')
                count++;
            }
            bufferedReader.close(); // closes the bufferedReader
        }
        catch (FileNotFoundException a){System.out.println("Could not find file " + "\'" + (new File(FILEPATH).getName()) + "\'");} // catches potential errors
        catch (IOException e){System.out.println("Error reading file"  + "\'" + (new File(FILEPATH).getName()) + "\'");}
        return fileRead;
    }
    public JSONObject readToJSONObj(){
        try {
            return (JSONObject) parser.parse(model.arrayToString(readFileToArr()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }

    public boolean deleteFile(){
        return ((new File(FILEPATH)).delete());
    }

    public void writeToFile(String fileContent){ // will replace the specified file's content with the String passed
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(FILEPATH));
            writer.write(fileContent);
            writer.close();
        } catch (IOException e) {
            System.out.println("Could not write to file. Invalid path.");
        }
    }
    public void writeFile(String fileContent){ // will create a new file with the specified file content
        try {
            File f = new File(FILEPATH);
            f.createNewFile();
            FileWriter writer = new FileWriter(f);
            writer.write(fileContent);
            writer.close();
        } catch(IOException e){
            System.out.println("Could not write to file. Invalid path. Or something else. I don't really know.");
        }
    }

    public MatchList searchFileForPhrase(String keyphrase, String tag) {
        MatchList ml = new MatchList(new File(FILEPATH).getName());

        boolean isMatch;
        String[] text = readFileToArr();
        int[] limits = Model.getTagLimits(tag, text); // limits[0] = start, [1] = end
        String[][] splitText = new String[text.length][0];
        for (int i = 0; i < text.length; i++) {
            text[i] = text[i].replaceAll(WITH_DELIMITERS, "");
            splitText[i] = text[i].split("");
        }


        String[] splitKeyphrase = keyphrase.split("");

        for (int i = limits[0]; i <= limits[1]; i++) {
            for (int j = 0; j < splitText[i].length - 1; j++) {
                isMatch = true;
                int keyPhraseCount = 0;
                int tmpCount = j;

                while (keyPhraseCount <= splitText[i].length - 1
                        && keyPhraseCount <= splitKeyphrase.length - 1
                        && tmpCount <= splitText[i].length - 1
                        && isMatch) {
                    if (!splitKeyphrase[keyPhraseCount].toLowerCase().equals(splitText[i][tmpCount].toLowerCase())) {
                        isMatch = false;
                    } else {
                        keyPhraseCount++;
                        tmpCount++;
                    }
                }
                if (keyPhraseCount == splitKeyphrase.length && keyPhraseCount != 0) {
                    //System.out.println(Model.getSurroundingText(splitText[i], j, j + splitKeyphrase.length - 1));
                    ml.addNode(ml.newNode(
                            Model.getSurroundingText(
                                    splitText[i],
                                    j,
                                    j + splitKeyphrase.length - 1),
                            i + 1));
                }
            }
        }

        return ml;
    }
    public LinkedList<String> searchFileForTags(){
        LinkedList<String> tagsInFile = new LinkedList<>();
        String stringFile = readFileToStr();
        Pattern p = Pattern.compile("#(\\w+)#");
        Matcher tagMatch = p.matcher(stringFile);

        while(tagMatch.find()){
            tagsInFile.add(tagMatch.group(1));
        }

        return tagsInFile;

    }

}