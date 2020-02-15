package internal.assessment.cs;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.awt.*;
import java.net.URI;

public class Model {

    public static void main(String[] args){

    }
    String stringArrToString(String[] arr){
        String str = "";
        for (String a: arr){
            str += a + "\n";
        }
        return str;
    }
    String[] shortenStringArrToIndex(String[] arr, int i){ // index 'i' starts at 0
        String[] newArr = new String[i];
        for (int j = 0; j < i; j++){
            newArr[j] = arr[j];
        }
        return newArr;
    }

    static void showErrorMsg(String error, String subError){ //Uses an Alert object to create an error message
        Alert errorMsg = new Alert(Alert.AlertType.ERROR);
        errorMsg.setHeaderText(error);
        errorMsg.setContentText(subError);
        errorMsg.showAndWait();
    }
    static void showInformationMsg(String information, String subText){
        Alert informationMsg = new Alert(Alert.AlertType.INFORMATION);
        informationMsg.setHeaderText(information);
        informationMsg.setContentText(subText);
        informationMsg.showAndWait();
    }
    static boolean showConfirmationMsg(String header, String content){
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setHeaderText(header);
        confirmation.setContentText(content);
        return confirmation.showAndWait().get() == ButtonType.OK;
    }

    static String getSurroundingText(String[] arr, int lowerIndex, int upperIndex) { // the two ints describe
        String surroundingText = "";                                                 // the start and end of
        int lowerLimit = lowerIndex, upperLimit = upperIndex;                        // the phrase
        while(lowerLimit-1 >= 0 && lowerIndex-lowerLimit < 10) { // gets the lowest possible
            lowerLimit--;                                        // positive index that is within 10 indices
        }
        while(upperLimit+1 <= arr.length && upperLimit-upperIndex < 10) { // gets highest possible
            upperLimit++;                                                 //  positive index within 10 indices
        }
        for (int i = lowerLimit; i <= upperLimit-1; i++) { // adds all text in between two indices to the
            surroundingText += arr[i];                     // value to be returned
        }
        return surroundingText;
    }

    static int[] getTagLimits(String tag, String[] text){
        
        boolean endTagExists = false;
        if (tag.equals("")){
            return new int[]{0, text.length-1};
        }
        String[][] splitText = new String[text.length][0]; // first splits the text by words (as defined by spaces)
        for (int i = 0; i < text.length; i++) {
            splitText[i] = text[i].split(" ");
        }
        int[] limits = new int[2]; // [0] is the upper limit, [1], is the lower limit
        limits[0] = text.length-1;
        limits[1] = text.length-1; // the two limits will be the bounds by which the search will be limited

        try {
            while(!splitText[limits[0]][0].equals("#" + tag + "#")) {  // searches for the tag format #abc# and for the next blank line after that.
                if (splitText[limits[0]][0].equals("#/" + tag + "#")){ // the algorithm starts from the end of the doc to maximize efficiency
                    limits[1] = limits[0];
                    endTagExists = true;
                } else if(splitText[limits[0]].length-1==0 && !endTagExists) { // isEndTagFound allows the algorithm to say "stop searching for
                    limits[1] = limits[0];                                      // blank lines because this person has set an end tag location
                }
                limits[0]--; // because the search starts from the end, the index of searching goes back one each time '--' instead of forward '++'
            }
        }catch(ArrayIndexOutOfBoundsException e){ // if the limits[0] variable goes too low, it will throw an exception, and we will know that
            // the program did not find any matches for the tag, and will by default search the whole document
            //System.out.println("\t[The tag does not exist in this file... searching whole file instead.]\n"); // and not narrow it by the tag
            return new int[] {0, -1}; // won't search the file.
        }
        limits[0]++; // the upper limit for the tag (the point at which the search will start) must be appended by one as otherwise it will include the tag
                     // itself in the search
        return limits;
    }


    String arrayToString(String[] arr) {
        String toString = "";
        for (String a: arr) {
            toString+=a;
        }
        return toString;
    }
    public String arrayToString (String[][] arr) {
        String toString = "";
        for (int i = 0; i < arr.length; i++) {
            for(int j = 0; j < arr[i].length; j++) {
                toString+=arr[i][j];
            }
            toString+="\n";
        }
        return toString;
    }

    public static boolean openWebpage(URI uri) {
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(uri);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    String parseTextForTags(String text, boolean showTags){
        String[] splitArr = text.split("((?<=#)|(?=#))");
        String[] tmp;

        //System.out.println(arrayToString(splitArr));

        for(int i = 0; i <= splitArr.length-3; i++){
            if(splitArr[i].equals("#") && splitArr[i+2].equals("#")){
                if(showTags) {
                    if (i > 0 && !splitArr[i - 1].contains("</p>")) {
                        splitArr[i] = "<br><span style=\"background-color: #DCDCDC;font-family: Consolas,monaco,monospace;font-size: 12.25\">";
                    } else {
                        splitArr[i] = "<span style=\"background-color: #DCDCDC;font-family: Consolas,monaco,monospace;font-size: 12.25\">";
                    }
                    splitArr[i + 2] = "</span><br>";
                }else{
                    if (i > 0 && !splitArr[i - 1].contains("</p>")) {
                        splitArr[i] = "<br>";
                    }else {
                        splitArr[i] = "";
                    }
                    for(int j = i+1; j <= i + 2; j++){
                        splitArr[j] = "";
                    }
                }
            }
        }

        text = arrayToString(splitArr);
        //text = text.replace("\n", "<br>");

        return text;
    }




    static void bubbleSort(String[] list){
        for (int i = 0; i < list.length; i++){ // for each
            for(int j = 0; j < list.length-1-i; j++){
                if(changeNeeded(list[j], list[j+1], 0)){
                    String tmp = list[j];
                    list[j] = list[j+1];
                    list[j+1] = tmp;
                }
            }
        }
    }

    // chars automatically convert to ints as interpreted by ASCII (alphabetical for lower case)
    static boolean changeNeeded(String left, String right, int letterIndex){ // if true, right is smaller -- false otherwise
        if(letterIndex < left.length() && letterIndex < right.length()) {
            if (left.charAt(letterIndex) > right.charAt(letterIndex)) { // is right smaller than left?
                return true;
            } else if (left.charAt(letterIndex) == right.charAt(letterIndex)) { // if they have the same "first" letter
                return changeNeeded(left, right, letterIndex + 1); // recursive call to test the same thing
            } else {                                                         // with the next letters
                return false;
            }
        }else{
            return false;
        }
    }

}