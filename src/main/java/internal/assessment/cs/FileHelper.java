package internal.assessment.cs;

import java.io.*;


public class FileHelper{

    Model model = new Model();

    final String FILEPATH; // each FileHelper class is specific to one file.
    // Thus, the FILENAME variable is final and cannot be
    // changed after instantiation in the constructor

    public FileHelper(String filePath){
        FILEPATH = filePath; // File name is instantiated specific to each object
    }

    public String[] readFile(){ // will return a single string file read from a document with each line separation shown through '\n'
        String[] fileRead = new String[500]; // fileRead file is the string containing the read file to be returned by the readFile() method.
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
        catch (FileNotFoundException a){System.out.println("Could not find file " + "\'FILENAME\'");} // catches potential errors
        catch (IOException e){System.out.println("Error reading file"  + "\'FILENAME\'");}
        return model.shortenStringArrToIndex(fileRead, count); // returns the final output of the read string in array form
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

    public void searchFileForPhrase(String keyphrase, String tag) {

        MatchList ml = new MatchList(FILEPATH);

        boolean isMatch = true;
        String[] text = readFile();
        int[] limits = Model.getTagLimits(tag, text); // limits[0] = start, [1] = end
        String[][] splitText = new String[text.length][0];
        for (int i = 0; i < text.length; i++) {
            splitText[i] = text[i].split("");
        }
        String[] splitKeyphrase = keyphrase.split("");

        for (int i = limits[0]; i <= limits[1]; i++) {
            for (int j = 0; j < splitText[i].length - 1; j++) {
                isMatch = true;
                int keyPhraseCount = 0;
                int tmpCount = j;


                while (keyPhraseCount <= splitText[i].length - 1 && keyPhraseCount <= splitKeyphrase.length - 1 && tmpCount <= splitText[i].length - 1 && isMatch) {
                    if (!splitKeyphrase[keyPhraseCount].equals(splitText[i][tmpCount])) {
                        isMatch = false;
                    } else {
                        keyPhraseCount++;
                        tmpCount++;
                    }
                }
                if (keyPhraseCount == splitKeyphrase.length && keyPhraseCount != 0) {
                    ml.addNode(ml.newNode(Model.getSurroundingText(splitText[i], j, j + splitKeyphrase.length - 1), i + 1));
                }
            }
        }
        ml.printList();
    }
}