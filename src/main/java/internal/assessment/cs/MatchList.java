package internal.assessment.cs;

public class MatchList {
    private Node head;
    private String filename;
    private int length = 0;

    int getLength() {return length;}

    public MatchList(Node head) { this.head = head; length++; }
    MatchList(String filename) {this.filename = filename;}

    String getFilename(){ return filename;}

    void addNode(Node node) {
        if (isNotEmpty()) {
            Node tmp = new Node();
            tmp.next = head;
            while(tmp.next.next!=null) {
                tmp.next=tmp.next.next;
            }
            tmp.next.next=node;
        }else {
            head = node;
        }
        length++;
    }
    Node newNode(String sampleText, int lineNum) { return new Node(sampleText, lineNum); }
    private boolean isNotEmpty() {return head != null;}
    String toSearchResult(int index){
        return "In \'" + getFilename() + "\', line " + getLineNum(index) + ": \"..." + getSampleText(index) + "...\"";
    }

    public void printList() {
        if (isNotEmpty()) {
            Node tmp = head;
            System.out.println("In file at " + filename);
            System.out.print("Line number:\t" + tmp.getLineNum() + "\nSample Text:\t..." + tmp.getSampleText() + "...\n\n");
            while(tmp.next!=null) {
                System.out.print("Line number:\t" + tmp.next.getLineNum() + "\nSample Text:\t..." + tmp.next.getSampleText() + "...\n\n");
                tmp = tmp.next;
            }
        }else { System.out.println("There were no matches found in the file at " + filename + "\n"); }
        System.out.println("-------------------------------------------------------------------------------\n");
    } // this was solely for development purposes

    private String getSampleText(int index){
        int i = 0;
        if (isNotEmpty()){
            Node tmp = new Node();
            tmp.next = head;
            while(tmp.next.next!=null && i != index){ // if index is longer than the list it will return the last value
                tmp.next=tmp.next.next;
                i++;
            }
            return tmp.next.getSampleText();
        }else{
            return "This array is empty.";
        }
    }
    private int getLineNum(int index){
        int i = 0;
        if (isNotEmpty()){
            Node tmp = new Node();
            tmp.next = head;
            while(tmp.next.next!=null && i != index){ // if index is longer than the list it will return the last value
                tmp.next=tmp.next.next;
                i++;
            }
            return tmp.next.getLineNum();
        }else{
            return 0;
        }
    }
}

class Node{
    private String sampleText;
    private int lineNum;
    Node next;

    Node(String sampleText, int lineNum) {
        this.sampleText = sampleText;
        this.lineNum = lineNum;
    }
    Node(){}

    String getSampleText() { return sampleText;}
    int getLineNum() {return lineNum;}
}