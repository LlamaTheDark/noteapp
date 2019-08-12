package internal.assessment.cs;
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.scene.web.WebEngine;
import javafx.stage.Stage;
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
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

import static spark.Spark.*;

public class ViewController extends InfoHelper implements Initializable {


    Model model = new Model();

    //under sync
    public MenuItem menuBtnAuthDropbox;
    public MenuItem menuBtnSyncFiles;
    public MenuItem menuBtnAuthNewAcc;

    //under edit
    public MenuItem menuBtnDeleteFile; //TODO: VERY IMPORTANT rename all methods associated with a button or action to 'handle...Action'
    public MenuItem menuBtnRender;

    //under file
    public MenuItem menuBtnCreateNewNote;
    public MenuItem menuBtnSave;
    public MenuItem menuBtnSaveAs;
    public MenuItem menuBtnOpenFile;
    public MenuItem menuBtnSetNotesFolder;
    //----------------------------------//
    public MenuItem menuBtnExit;
    //

    public Button btnSearch;

    public MenuBar menu;
    public TabPane tabPane;
    public WebView wbvPreview;

    //
    public ProgressBar pbSyncProgress;
    public ImageView imgLoading;
    public Label lblSyncUpdate;
    // sync bar

    //

    //KeyCombination kcSaveFile = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN); // TODO: add key shortcuts to save, delete, and close tabs
    // shortcuts

    //
    MutableDataSet options;
    Parser parser;
    HtmlRenderer renderer;
    WebEngine webEngine;
    // markdown variables

    //
    public FileChooser fc;
    private File selectedFile;
    public int tabCount = 0;
    //private String noteFolderPath; // had to change this from the file to the String because files are immutable objects
                                   // now it just creates a new file with the noteFolderPath directory whenever it is needed.
    // for saving and opening files (the window helper from the OS) // TODO: eventually sync the files from the notes folder

    //
    DropboxHelper dh = new DropboxHelper(this);
    // for dropbox

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

    public void handleCreateNewNoteAction(ActionEvent actionEvent) { // for when a new note is requested to be created
        Tab newTab = new Tab("Tab_" + ++tabCount); // TODO: have way to change tab name (maybe in the save / save as... function)
        TextArea newTabTxt = new TextArea(); // creates text area
        newTabTxt.setFont(Font.font("Consolas", FontWeight.NORMAL, 12)); // sets text area font

        newTab.setContent(newTabTxt); // places the new text area inside of the previously created tab's content
        tabPane.getTabs().add(newTab); // places the new tab created with the text inside into the tabs list of the tabPane

        tabPane.getSelectionModel().select(newTab);

        menuBtnSave.setDisable(false);
        menuBtnSaveAs.setDisable(false);

        newTabTxt.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
                handleShowRenderedTextAction(new ActionEvent());
            }
        });
    }

    public void handleSaveFileAction(ActionEvent actionEvent) {
        Tab tmp = getCurrentTab();
        if(new File(getNoteFolderPath() + "\\" + tmp.getText()).exists()) { // if a file already exists then overwrite it
            FileHelper fh = new FileHelper(getNoteFolderPath() + "\\" + tmp.getText());
            fh.writeToFile(((TextArea) tmp.getContent()).getText());
        }else{
            handleSaveFileAsAction(new ActionEvent()); // if the file doesn't exist then go to 'save as...'
        }
    }
    public void handleSaveFileAsAction(ActionEvent actionEvent){
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
        if (!tabPaneIsEmpty()){
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

    public void handleShowRenderedTextAction(ActionEvent actionEvent){ // Hides plain text editor and renders/shows WebView
        if(!tabPaneIsEmpty()) { // ensures there is a tab to render...
            String tabContent = ((TextArea)getCurrentTab().getContent()).getText();
            webEngine.loadContent(markdownToHTML(tabContent)); // has to parse the html from markdown first, then split the tags
        }else{                                                                                      // bc markdownToHTML depends on the \n which the model.parse...Breaks removes
            Model.showErrorMsg("No Document Selected", "Please open a document to render it and try again.");
        }
        /*
        WebEngine htmlEngine = (((WebView) ((ScrollPane) tmp.getContent()).getContent()).getEngine()); // finds the webengine in the corresponding scrollpane/webview
        htmlEngine.loadContent("<b>biggie cheese</b>");
        ((TextArea)tmp.getContent()).setText("");*/

    }

    public void handleSearchAction(ActionEvent actionEvent) {
        try {
            setTmpInfo("");
            Stage searchStage = new Stage();
            searchStage.setTitle("Search Files");
            searchStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("searchScene.fxml")), 420, 465));
            searchStage.showAndWait();
            if(!getTmpInfo().equals("")){
                createNewNote(model.stringArrToString((new FileHelper(getNoteFolderPath()+"\\"+ getTmpInfo())).readFile()), getTmpInfo());
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void handleAuthDropboxAction(ActionEvent actionEvent) {
        if(!dh.accountHasBeenLinked()){
            try {
                Stage authDbxStage = new Stage();
                authDbxStage.setTitle("Authorize Dropbox");
                authDbxStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("authorizeDropboxScene.fxml")), 472, 474));
                authDbxStage.showAndWait();
                if(getTmpInfo().equals("successful authorization")){
                    menuBtnSyncFiles.setDisable(false);
                }
            }catch(IOException e){
                e.printStackTrace();
                System.out.println("Error: missing resource...");
            }
        }else{
            Model.showInformationMsg("An account has already been linked.", "Account Name: " + dh.getClientName());
        }
    }
    public void handleSyncFilesAction(ActionEvent actionEvent) {
        pbSyncProgress.setVisible(true);
        dh.uploadFiles();
    }

    public void handleExitApplicationAction(ActionEvent actionEvent) {
        ((Stage)btnSearch.getScene().getWindow()).close();
    }

    public void handleOpenSparkServerAction(ActionEvent actionEvent) { get("/hello", (req, res) -> "Hello World"); }
    public void handleCloseSparkServerAction(ActionEvent actionEvent) { stop(); }

    //functions called past this point are not called as a direct result of interaction with the GUI//
//-------------------------------------------------------------------------------------------------------------------//
    public Tab getCurrentTab(){ return tabPane.getSelectionModel().getSelectedItem(); }
    public boolean tabPaneIsEmpty(){ return tabPane.getTabs().size() < 1; }

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
        //System.out.println(renderedHTML);
        return model.parseTextForTags(renderer.render(doc));
    } // the code must manually place line breaks (<br>) for tags.
    // commonmark markdown does not support soft line breaks as <br>

    public void createNewNote(String content, String title){ // for when a specified file is requested to be opened
        Tab newTab = new Tab(title);
        TextArea newTabTxt = new TextArea();
        newTabTxt.setFont(Font.font("Consolas", FontWeight.NORMAL, 12));
        newTabTxt.setText(content);
        newTab.setContent(newTabTxt);
        tabPane.getTabs().add(newTab);

        tabPane.getSelectionModel().select(newTab); // select the new tab

        menuBtnSave.setDisable(false);
        menuBtnSaveAs.setDisable(false);


        newTabTxt.textProperty().addListener(new ChangeListener<String>() { // adds event listener to detect when a change has been made to the text
            @Override                                                       // upon hearing a change, the new markdown text is rendered
            public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
                handleShowRenderedTextAction(new ActionEvent());
            }
        });
    }

    public void closeTab() {
        tabPane.getTabs().remove(tabPane.getSelectionModel().getSelectedItem());
        if(tabPaneIsEmpty()){
            menuBtnSave.setDisable(true);
            menuBtnSaveAs.setDisable(true);
        }
    }

//
    void rebindProgressBar(ReadOnlyDoubleProperty syncProgress){
        pbSyncProgress.progressProperty().unbind();
        pbSyncProgress.progressProperty().bind(syncProgress);
    }
    void rebindSyncLabel(ReadOnlyStringProperty syncLabel){
        lblSyncUpdate.textProperty().unbind();
        lblSyncUpdate.textProperty().bind(syncLabel);
    }
    void unBindProgressBar(){ pbSyncProgress.progressProperty().unbind(); }
    void resetProgressBar(){ pbSyncProgress.setProgress(0.0); }
    void startLoadingAnimation(){ imgLoading.setImage(new Image("/loading.gif")); }
    void stopLoadingAnimation(){ imgLoading.setImage(null); }
    void hideProgressBar(){ pbSyncProgress.setVisible(false); }
// progress bar action


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //can't find this setting in scenebuilder, TODO: add it to fxml file just before you finish this whole thing
        tabPane.setTabDragPolicy(TabPane.TabDragPolicy.REORDER);
        wbvPreview.setFontScale(0.85);
        webEngine = wbvPreview.getEngine();
        if (dh.accountHasBeenLinked()){
            menuBtnSyncFiles.setDisable(false);
        }

        tabPane.getSelectionModel().selectedItemProperty().addListener( // change listener to detect a tab selection change
                new ChangeListener<Tab>() {
                    @Override
                    public void changed(ObservableValue<? extends Tab> ov, Tab t, Tab t1) {
                        if(tabPaneIsEmpty()){
                            menuBtnSave.setDisable(true);
                            menuBtnSaveAs.setDisable(true);
                            webEngine.loadContent("");
                        }else{
                            handleShowRenderedTextAction(new ActionEvent());
                        }
                    }
                }
        );
    }


}

/*
TODO: start server: get("/hello", (req, res) -> "Hello World");
TODO: stop server: stop();
 */