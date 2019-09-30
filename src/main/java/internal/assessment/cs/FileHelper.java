package internal.assessment.cs;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


class FileHelper {

    private Model model = new Model();
    private JSONParser parser = new JSONParser();

    private final String WITH_DELIMITERS = "\\t|,|;|!|-|:|@|_|\\*|";

    private final String FILEPATH; // each FileHelper class is specific to one file.
    // Thus, the FILEPATH variable is final and cannot be
    // changed after instantiation in the constructor

    FileHelper(String filePath){
        FILEPATH = filePath; // File name is instantiated specific to each object
    }

    String[] readFileToArr(){ // will return a single string file read from a document with each line separation shown through '\n'
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
    String readFileToStr(){
        String fileRead = ""; // fileRead file is the final output value (the read string)
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
    JSONObject readToJSONObj(){
        try {
            return (JSONObject) parser.parse(model.arrayToString(readFileToArr()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new JSONObject();
    }

    boolean deleteFile(){
        return ((new File(FILEPATH)).delete());
    }

    void writeToFile(String fileContent){ // will replace the specified file's content with the String passed
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(FILEPATH));
            writer.write(fileContent);
            writer.close();
        } catch (IOException e) {
            System.out.println("Could not write to file. Invalid path.");
        }
    }
    void writeFile(String fileContent){ // will create a new file with the specified file content
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

    MatchList searchFileForPhraseByTag(String keyphrase, String tag) {
        MatchList ml = new MatchList(new File(FILEPATH).getName());

        boolean isMatch;
        String[] text = readFileToArr();
        int[] limits = Model.getTagLimits(tag, text); // limits[0] = start, [1] = end
        String[][] splitText = new String[text.length][0];
        for (int i = 0; i < text.length; i++) {
            text[i] = text[i].replaceAll(WITH_DELIMITERS, "");
            splitText[i] = text[i].split(""); // splits the whole text into each individual letter
        }


        String[] splitKeyphrase = keyphrase.split(""); // splits entire keyphrase into each individual letter.

        for (int i = limits[0]; i <= limits[1]; i++) { // searches from the lower index to the upper index
            for (int j = 0; j < splitText[i].length - 1; j++) { // for each line, it searches each letter
                isMatch = true;
                int keyPhraseIndex = 0; // to keep track of matched letters in a row (index of the keyphrase)
                int lineIndex = j; // to keep track of the index within the line

                while (keyPhraseIndex <= splitText[i].length - 1
                        && keyPhraseIndex <= splitKeyphrase.length - 1
                        && lineIndex <= splitText[i].length - 1
                        && isMatch) { // makes sure everything is within the bounds and that there is still a match to be had
                    if (!splitKeyphrase[keyPhraseIndex].toLowerCase().equals(splitText[i][lineIndex].toLowerCase())) {
                        isMatch = false; // if the letter in the keyphrase doesn't match the letter of the line (at the same
                    } else {             // index relative to j, then there cannot be a match for this increment of j.
                        keyPhraseIndex++;// otherwise, the letters match and the next letters in the line can be tested
                        lineIndex++;
                    }
                }
                if (keyPhraseIndex == splitKeyphrase.length && keyPhraseIndex != 0) { // if the keyPhraseIndex got to the end
                    ml.addNode(ml.newNode(                                            // of the keyword, it is a match
                            Model.getSurroundingText(
                                    splitText[i],
                                    j,
                                    j + splitKeyphrase.length - 1),
                            i + 1
                    ));
                }
            }
        }
        return ml;
    }
    LinkedList<String> searchFileForTags(){
        LinkedList<String> tagsInFile = new LinkedList<>();
        String stringFile = readFileToStr();
        Pattern p = Pattern.compile("#([a-zA-Z0-9_ ']+)#"); // https://regex101.com/ is a lifesaver
        Matcher tagMatch = p.matcher(stringFile);

        while(tagMatch.find()){
            tagsInFile.add(tagMatch.group(1));
        }

        return tagsInFile;

    }
    boolean containsTag(String tag){
        return readFileToStr().contains("#" + tag + "#");
    }
}