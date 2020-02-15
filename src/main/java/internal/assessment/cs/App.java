package internal.assessment.cs;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class App {
    public static void main(String[] args){
//        refreshCss("terminal");
        Main.main(args);

//        String css = makeCss("#63b7af", "#347474", "#347474", "#35495e", "#ee8572", "#b7472a");
//        StringSelection selection = new StringSelection(css);
//        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
//        clipboard.setContents(selection, selection);
    }

    private static void refreshCss(String cssType){
        FileHelper fh = new FileHelper("C:\\Users\\mrsea\\InternalAssessmentProjects\\NoteApp\\src\\main\\resources\\css\\style_main_" + cssType + ".css");
        fh.writeToFile(makeCss("#1F1F1F", "#1F1F1F", "#1F1F1F", "#1F1F1F", "#7cfc00", "#7cfc00"));
    }

    private static String makeCss(String primary, String secondary, String thirdColor, String fourthColor, String outlineColor, String textColor){

        String css = "/*********************************************************************************************************\n" +
                "\n" +
                "TEXT AREA\n" +
                "\n" +
                "*********************************************************************************************************/\n" +
                ".text-area {\n" +
                "    -fx-control-inner-background: " + primary + "; /* background color of the text area */\n" +
                "    -fx-background-color: " + secondary + "; /* outline color of the text area */\n" +
                "    -fx-text-color: " + textColor + "; /* color of the text */\n" +
                "}\n" +
                ".text-area:focused {\n" +
                "    /*-fx-faint-focus-color: transparent;*/\n" +
                "    -fx-faint-focus-color:" + primary + "; /* here rgba (corrected) */\n" +
                "}\n" +
                "\n" +
                "/*********************************************************************************************************\n" +
                "\n" +
                "TAB PANE\n" +
                "\n" +
                "*********************************************************************************************************/\n" +
                ".tab-pane *.tab-header-background {\n" +
                "    -fx-background-color: " + secondary + "; /* color of area behind the tab */\n" +
                "}\n" +
                ".tab-pane *.tab {\n" +
                "    -fx-background-color: " + secondary + ";   /* background color of tab*/\n" +
                "    -fx-text-base-color: " + textColor + "; /* tab name color */\n" +
                "}\n" +
                ".tab-pane *.tab:selected {\n" +
                "    -fx-background-color: " + thirdColor + ";\n" +
                "    -fx-border-color: " + outlineColor + "; /*color of current tab indicator */\n" +
                "    -fx-border-width: 0 0 1 0;    /* top right bottom left*/\n" +
                "}\n" +
                ".tab-pane:focused > .tab-header-area > .headers-region > .tab:selected .focus-indicator {\n" +
                "    -fx-border-color: black; /*color of 'selected' indicator */\n" +
                "    -fx-border-width: 1 1 0 1; /* top right bottom left*/\n" +
                "}\n" +
                ".tab-pane {\n" +
                "    -fx-background-color: " + primary + "; /* color of pane when there are no tabs */\n" +
                "}\n" +
                "\n" +
                "/*********************************************************************************************************\n" +
                "\n" +
                "BUTTON\n" +
                "\n" +
                "*********************************************************************************************************/\n" +
                ".button {\n" +
                "    -fx-background-color: grey, black, black, grey;\n" +
                "    -fx-text-base-color: " + textColor + ";\n" +
                "}\n" +
                ".button:pressed {\n" +
                "    -fx-background-color: " + secondary + ", " + secondary + ", " + secondary + " " + secondary + ";\n" +
                "}\n" +
                "\n" +
                "/*********************************************************************************************************\n" +
                "\n" +
                "MENU\n" +
                "\n" +
                "*********************************************************************************************************/\n" +
                ".menu-bar {\n" +
                "    -fx-background-color: " + fourthColor + ";\n" +
                "}\n" +
                ".menu {\n" +
                "    -fx-text-base-color: " + textColor + ";\n" +
                "}\n" +
                ".menu:hover{\n" +
                "    -fx-background-color: " + thirdColor + ";\n" +
                "}\n" +
                ".menu:selected{\n" +
                "    -fx-background-color: " + thirdColor + ";\n" +
                "}\n" +
                ".menu:showing{\n" +
                "    -fx-background-color: " + outlineColor + "; /* color of the menu when you're sorting through the menu items */\n" +
                "}\n" +
                ".menu-item {\n" +
                "    -fx-background-color: " + primary + ";\n" +
                "}\n" +
                ".menu-item:hover{\n" +
                "    -fx-background-color: #5E5E5E;\n" +
                "}\n" +
                ".menu-item:focused{\n" +
                "    -fx-background-color: " + outlineColor + "; /* color of the menu item you're hovering over */\n" +
                "}\n" +
                ".context-menu{\n" +
                "    -fx-background-color: " + primary + "; /* color of background of the actual dropdown menu*/\n" +
                "    -fx-background-insets: 0, 1, 2;\n" +
                "    -fx-background-radius: 0 6 6 6, 0 5 5 5, 0 4 4 4;\n" +
                "    -fx-padding: 0.333333em 0.083333em 0.666667em 0.083333em; /* 4 1 8 1 */\n" +
                "}\n" +
                ".check-menu-item {\n" +
                "    -fx-mark-color: " + textColor + "; /* color of the check mark */\n" +
                "}\n" +
                ".separator > .line {\n" +
                "    -fx-border-color: " + outlineColor + ";\n" +
                "}\n" +
                "\n" +
                "/*********************************************************************************************************\n" +
                "\n" +
                "SPLIT PANE\n" +
                "\n" +
                "*********************************************************************************************************/\n" +
                ".split-pane{\n" +
                "    -fx-background-color: " + fourthColor + "; /* background behind all the other objects in the split pane */\n" +
                "}\n" +
                ".split-pane:horizontal > .split-pane-divider {\n" +
                "    -fx-background-color: " + thirdColor + "; /* color of the divider */\n" +
                "}\n" +
                "\n" +
                "/*********************************************************************************************************\n" +
                "\n" +
                "PROGRESS BAR\n" +
                "\n" +
                "*********************************************************************************************************/\n" +
                ".progress-bar{\n" +
                "    -fx-accent: " + textColor + ";\n" +
                "}\n" +
                "\n" +
                "/*********************************************************************************************************\n" +
                "\n" +
                "COMBO BOX\n" +
                "\n" +
                "*********************************************************************************************************/\n" +
                ".combo-box {\n" +
                "    -fx-background-color: " + primary + ";\n" +
                "    -fx-control-inner-background: " + primary + ";\n" +
                "    -fx-border-color: " + outlineColor + ";\n" +
                "    -fx-border-width: 0 0 1 0;\n" +
                "\n" +
                "}\n" +
                ".combo-box .arrow {\n" +
                "    -fx-background-color: " + textColor + ";\n" +
                "}\n" +
                ".combo-box-base:editable > .arrow-button {\n" +
                "    -fx-background-color: #00000000, #181818, #282828, #282828;\n" +
                "    -fx-background-insets: 0 0 -1 0, 0, 1, 2;\n" +
                "    -fx-background-radius: 3px, 3px, 2px, 1px;\n" +
                "    -fx-padding: 0.333333em 0.666667em 0.333333em 0.666667em; /* 4 8 4 8 */\n" +
                "    -fx-text-fill: -fx-text-base-color;\n" +
                "    -fx-alignment: CENTER;\n" +
                "    -fx-content-display: LEFT;\n" +
                "}\n" +
                ".combo-box .text-field {\n" +
                "    -fx-prompt-text-fill: #FFFFFF7F;\n" +
                "    -fx-border-color: " + primary + ";\n" +
                "    -fx-border-width: 2 2 2 2;\n" +
                "    -fx-background-color: -fx-text-box-border, -fx-control-inner-background;\n" +
                "}\n" +
                ".combo-box .text-field:focused{\n" +
                "    -fx-background-color: linear-gradient(to bottom, derive(-fx-text-box-border, -10%), -fx-text-box-border),\n" +
                "            linear-gradient(from 0px 0px to 0px 5px, derive(-fx-control-inner-background, -9%), -fx-control-inner-background);\n" +
                "    -fx-background-color: -fx-text-box-border, -fx-control-inner-background;\n" +
                "}\n" +
                "\n" +
                "/*********************************************************************************************************\n" +
                "\n" +
                "ANCHOR PANE\n" +
                "\n" +
                "*********************************************************************************************************/\n" +
                "#searchAnchor, #templateAnchor, #templateNoteAnchor, #folderPathAnchor, #dbxAnchor {\n" +
                "    -fx-background-color: " + primary + ";\n" +
                "}\n" +
                "\n" +
                "/*********************************************************************************************************\n" +
                "\n" +
                "TEXT\n" +
                "\n" +
                "*********************************************************************************************************/\n" +
                "#sg_1, #sg_2, #alertText, #txtPrompt, #nts1, #nts2, #ntns1, #folderPathText{ /* text ids in the search scene controller */\n" +
                "    -fx-fill: " + textColor + ";\n" +
                "}\n" +
                "\n" +
                "#previewTxt { /* the 'Preview' above the WebView */\n" +
                "    -fx-fill: " + textColor + ";\n" +
                "}\n" +
                "\n" +
                "\n" +
                "\n" +
                "/*********************************************************************************************************\n" +
                "\n" +
                "TEXT FIELD\n" +
                "\n" +
                "*********************************************************************************************************/\n" +
                "#txtKeyphrase, #txtName, #txtFldPath { /* all the text fields */\n" +
                "    -fx-background-color: " + primary + ";\n" +
                "    -fx-control-inner-background: " + primary + ";\n" +
                "    -fx-border-color: " + outlineColor + ";\n" +
                "    -fx-border-width: 0 0 1 0;\n" +
                "}\n" +
                "#txtKeyphrase:focused .content {\n" +
                "    -fx-border-width: 0 0 3 0;\n" +
                "}\n" +
                "#txtName, #txtFldPath {\n" +
                "    -fx-prompt-text-fill: #FFFFFF7F;\n" +
                "}\n" +
                "\n" +
                "\n" +
                "\n" +
                "/*********************************************************************************************************\n" +
                "\n" +
                "LIST VIEW\n" +
                "\n" +
                "*********************************************************************************************************/\n" +
                ".list-view {\n" +
                "    -fx-background-color: " + primary + ";\n" +
                "}\n" +
                "\n" +
                ".list-cell{\n" +
                "    -fx-text-fill: " + textColor + ";\n" +
                "    -fx-background-color: " + primary + ";\n" +
                "    /*-fx-border-color: " + outlineColor + ";\n" +
                "    -fx-border-width: 0 0 0 1;*/\n" +
                "\n" +
                "    -fx-background-color: " + fourthColor + ", " + outlineColor + ";\n" +
                "    -fx-background-insets: 0 0 0 0, 7 390 7 2; /* top right bottom left */\n" +
                "\n" +
                "    /* see https://stackoverflow.com/questions/42465850/fx-background-radius-and-fx-background-insets-in-javafx */\n" +
                "}\n" +
                ".list-cell:hover{\n" +
                "    -fx-background-color: " + outlineColor + "7F;\n" +
                "}\n" +
                ".list-cell:selected {\n" +
                "    -fx-background-color: " + outlineColor + "7F;\n" +
                "}";
        return css;
    }
}
