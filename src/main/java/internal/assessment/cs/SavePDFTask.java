package internal.assessment.cs;

import javafx.concurrent.Task;

/*

THIS DOES NOT RENDER MATHJAX -- ONLY MARKDOWN

 */

public class SavePDFTask extends Task {
    private FileHelper fh;
    private String HTML;

    SavePDFTask(FileHelper fh, String HTML){ this.fh = fh; this.HTML = HTML; }

    @Override
    protected Object call() throws Exception {
        if(fh.exportAsPDF(HTML)){
            this.updateProgress(1, 1);
        }
        return null;
    }
}
