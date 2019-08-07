package internal.assessment.cs;
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import javafx.scene.web.WebView;
import javafx.scene.control.ScrollPane;
import javafx.stage.FileChooser;
import javafx.scene.web.WebEngine;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
// web view imports

import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.options.MutableDataSet;
// markdown imports

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static spark.Spark.*;

public class ViewController extends InfoHelper{

    Model model = new Model();

    //under sync
    public MenuItem menuBtnAuthDropbox;
    public MenuItem menuBtnSyncFiles;

    //under edit
    public MenuItem menuBtnDeleteFile; //TODO: VERY IMPORTANT rename all methods associated with a button or action to 'handle...Action'

    //under file
    public MenuItem menuBtnCreateNewNote;
    public MenuItem menuBtnSave;
    public MenuItem menuBtnOpenFile;
    public MenuItem menuBtnSetNotesFolder;

    public Button btnSearch;
    public Button btnPlaintext;
    public Button btnRender;

    public MenuBar menu;
    public TabPane tabPane;


    //
    KeyCombination kcSaveFile = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN); // TODO: add key shortcuts to save, delete, and close tabs
    // shortcuts

    //
    MutableDataSet options;
    Parser parser;
    HtmlRenderer renderer;
    // markdown variables

    //
    public FileChooser fc;
    private File selectedFile;
    public int tabCount = 0;
    //private String noteFolderPath; // had to change this from the file to the String because files are immutable objects
                                   // now it just creates a new file with the noteFolderPath directory whenever it is needed.
    // for saving and opening files (the window helper from the OS) // TODO: eventually sync the files from the notes folder

    //constructor//
    public ViewController(){ // constructor to set defaults for fileChooser and the noteFolder location
        fc = new FileChooser();
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("text documents", "*.txt"),
                new FileChooser.ExtensionFilter("markdown documents", "*.md"),
                new FileChooser.ExtensionFilter("html documents", "*.html")
        );
        fc.setInitialDirectory(new File(getNoteFolderPath()));

        options = new MutableDataSet();
        options.set(Parser.EXTENSIONS, Arrays.asList(TablesExtension.create(), StrikethroughExtension.create()));
        parser = Parser.builder(options).build();
        renderer = HtmlRenderer.builder(options).build();
    }
    //constructor//

    public void handleSetNotesFolderAction(ActionEvent actionEvent) {
        try {
            Stage noteFolderStage = new Stage();
            noteFolderStage.setTitle("Set Notes Folder");
            noteFolderStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("folderPathScene.fxml")), 466, 97));
            noteFolderStage.show();
        }catch(IOException e){
            System.out.println("Error: missing resource...");
        }
    }

    public void createNewNote(String content, String title){ // for when a specified file is requested to be opened
        if(tabCount==1){
            tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.SELECTED_TAB);
            tabPane.setTabDragPolicy(TabPane.TabDragPolicy.REORDER);
        }
        Tab newTab = new Tab(title);
        TextArea newTabTxt = new TextArea();
        newTabTxt.setFont(Font.font("Consolas", FontWeight.NORMAL, 12));
        newTabTxt.setText(content);
        newTab.setContent(newTabTxt);
        tabPane.getTabs().add(newTab);
    }
    public void handleCreateNewNoteAction(ActionEvent actionEvent) { // for when a new note is requested to be created
        if(tabCount==1){ // allows the tab to be closed when it is selected, TODO: Figure out how to have this happen automatically (w/ constructor maybe)
            tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.SELECTED_TAB);
            tabPane.setTabDragPolicy(TabPane.TabDragPolicy.REORDER);
        }
        Tab newTab = new Tab("Tab_" + ++tabCount); // TODO: have way to change tab name (maybe in the save / save as... function)
        TextArea newTabTxt = new TextArea(); // creates text area
        newTabTxt.setFont(Font.font("Consolas", FontWeight.NORMAL, 12)); // sets text area font

        newTab.setContent(newTabTxt); // places the new text area inside of the previously created tab's content
        tabPane.getTabs().add(newTab); // places the new tab created with the text inside into the tabs list of the tabPane

    }

    public void handleSaveFileAction(ActionEvent actionEvent) {
        Tab tmp = getCurrentTab();
        if(new File(getNoteFolderPath() + "\\" + tmp.getText()).exists()) {
            FileHelper fh = new FileHelper(getNoteFolderPath() + "\\" + tmp.getText());
            fh.writeToFile(((TextArea) tmp.getContent()).getText());
        }else{
            handleSaveFileAsAction(new ActionEvent());
        }
    }
    public void handleSaveFileAsAction(ActionEvent actionEvent){

        System.out.println(getNoteFolderPath());

        Tab tmp = getCurrentTab();
        selectedFile = openFileChooser("Save as...", "save", tmp.getText()); // saves lines of code to use seperate method
        if (selectedFile != null){
            FileHelper fh = new FileHelper(getNoteFolderPath() + "\\" + selectedFile.getName());
            fh.writeFile(((TextArea) tmp.getContent()).getText());
            tmp.setText(selectedFile.getName());
        }else{
            System.out.println("This is not a valid file name");
        }
    }
    public void handleOpenFileAction(ActionEvent actionEvent) {
        selectedFile = openFileChooser("Open...", "open", ""); // shows the open dialogue and sets selectedFile to whatever file is selected
        if (selectedFile != null){
            FileHelper fh = new FileHelper(selectedFile.getPath());
            createNewNote(model.stringArrToString(fh.readFile()), selectedFile.getName()); // going to have to change all 'readFile' bits
        }else{
            System.out.println("This is not a valid file name");
        }
    }
    public void handleDeleteFileAction(ActionEvent actionEvent) {
        if (!bTabPaneIsEmpty()){
            System.out.println(getNoteFolderPath() + "\\" + getCurrentTab().getText());
            Alert confirmDelete = new Alert(Alert.AlertType.CONFIRMATION);
            confirmDelete.setHeaderText("Are you sure you want to delete this file, \'" + getCurrentTab().getText() + "\'?");
            confirmDelete.setContentText("Press \'OK\' to confirm.");
            if (confirmDelete.showAndWait().get() == ButtonType.OK) {
                File tmp = new File(getNoteFolderPath() + "\\" + getCurrentTab().getText());
                if(tmp.delete()){
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setHeaderText("Success");
                    successAlert.setContentText(getCurrentTab().getText() + " successfully deleted from directory path.");
                    successAlert.showAndWait();
                    tabPane.getTabs().remove(getCurrentTab());
                }else{
                    model.showErrorMsg("An unknown error occurred.", "Please try again.");
                }
            }
        }else{
            model.showErrorMsg("No File to Delete", "Open a file to delete it.");
        }
    }

    public void handleShowPlainTextAction(ActionEvent actionEvent) { // Hides WebView and shows plain text editor // to test if the tabPane is empty first, use the BooleanBinding class
        System.out.println("this doesn't do anything yet"); // TODO: have this do something or get rid of it.
    }
    public void handleShowRenderedTextAction(ActionEvent actionEvent){ // Hides plain text editor and renders/shows WebView
        if(!bTabPaneIsEmpty()) { // ensures there is a tab to render...
            String tabContent = ((TextArea)getCurrentTab().getContent()).getText();

            Stage stage = new Stage(); // the following code is from https://wiki.openjdk.java.net/display/OpenJFX/Projects+and+Components (with the modification of tabContent)

            stage.setTitle("Preview"); // TODO: fix this stupidly long code, put another .fxml file in resources or something
            stage.setWidth(500);
            stage.setHeight(500);
            Scene scene = new Scene(new Group());

            VBox root = new VBox();

            final WebView browser = new WebView();
            final WebEngine webEngine = browser.getEngine();

            ScrollPane scrollPane = new ScrollPane();
            browser.setFontScale(0.8);
            scrollPane.setContent(browser);
            webEngine.loadContent(markdownToHTML(tabContent));

            root.getChildren().addAll(scrollPane);
            scene.setRoot(root);

            stage.setScene(scene);
            stage.show();

        }else{ // TODO: fix this garbage idk why it doesn't just BLOODy work!!!
            model.showErrorMsg("No Document Selected", "Please open a document to render it and try again.");
        }
        /*
        WebEngine htmlEngine = (((WebView) ((ScrollPane) tmp.getContent()).getContent()).getEngine()); // finds the webengine in the corresponding scrollpane/webview
        htmlEngine.loadContent("<b>biggie cheese</b>");
        ((TextArea)tmp.getContent()).setText("");*/

    }

    public void handleSearchAction(ActionEvent actionEvent) {

    }
    public void handleAuthDropboxAction(ActionEvent actionEvent) {
        DropboxHelper dh = new DropboxHelper();
        if(!dh.accountHasBeenLinked()){
            try {
                Stage noteFolderStage = new Stage();
                noteFolderStage.setTitle("Authorize Dropbox");
                noteFolderStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("authorizeDropboxScene.fxml")), 472, 474));
                noteFolderStage.show();
            }catch(IOException e){
                e.printStackTrace();
                System.out.println("Error: missing resource...");
            }
        }else{
            Model.showInformationMsg("An account has already been linked.", "Account Name: " + dh.getClientName());
        }
    }

    public void handleOpenSparkServerAction(ActionEvent actionEvent) { get("/hello", (req, res) -> "Hello World"); }
    public void handleCloseSparkServerAction(ActionEvent actionEvent) { stop(); }



//functions called past this point are not called as a direct result of interaction with the GUI//
//-------------------------------------------------------------------------------------------------------------------//
    public Tab getCurrentTab(){ return tabPane.getSelectionModel().getSelectedItem(); }
    public boolean bTabPaneIsEmpty(){ return tabPane.getTabs().size() < 1; }

    public File openFileChooser(String title, String type, String initialFileName){
        fc.setTitle(title);
        fc.setInitialFileName(initialFileName);
        fc.setInitialDirectory(new File(getNoteFolderPath()));
        switch(type){
            case "save":
                return fc.showSaveDialog(null);
            case "open":
                return fc.showOpenDialog(null);
            default:
                System.out.println("uuuuh you made a typo in the function call");
        }
        return new File(getNoteFolderPath());
    }

    public String markdownToHTML(String markdown){ // code from flexmark github homepage
        Node doc = parser.parse(markdown);
        String renderedHTML = renderer.render(doc);
        //System.out.println(renderedHTML);
        return renderedHTML;
    } // the code must manually place line breaks (<br>) for tags.
    // commonmark markdown does not support soft line breaks as <br>
}

/*
TODO: start server: get("/hello", (req, res) -> "Hello World");
TODO: stop server: stop();
 */