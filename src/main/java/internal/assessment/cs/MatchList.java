package internal.assessment.cs;

public class MatchList {
    Node head;
    String filename;
    private int length = 0;

    public int getLength() {return length;}

    public MatchList(Node head) { this.head = head; length++; }
    public MatchList(String filename) {this.filename = filename;}

    public String getFilename(){ return filename;}

    public void addNode(Node node) {
        if (!isEmpty()) {
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
    public Node newNode(String sampleText, int lineNum) { return new Node(sampleText, lineNum); }

    private boolean isEmpty() {return head==null;}

    public void printList() {
        if (!isEmpty()) {
            Node tmp = head;
            System.out.println("In file at " + filename);
            System.out.print("Line number:\t" + tmp.getLineNum() + "\nSample Text:\t..." + tmp.getSampleText() + "...\n\n");
            while(tmp.next!=null) {
                System.out.print("Line number:\t" + tmp.next.getLineNum() + "\nSample Text:\t..." + tmp.next.getSampleText() + "...\n\n");
                tmp = tmp.next;
            }
        }else { System.out.println("There were no matches found in the file at " + filename + "\n"); }
        System.out.println("-------------------------------------------------------------------------------\n");
    }

    public String toSearchResult(int index){
        return "In \'" + getFilename() + "\', line " + getLineNum(index) + ": \"..." + getSampleText(index) + "...\"";
    }

    public String getSampleText(int index){
        int i = 0;
        if (!isEmpty()){
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
    public int getLineNum(int index){
        int i = 0;
        if (!isEmpty()){
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

    public Node(String sampleText, int lineNum) {
        this.sampleText = sampleText;
        this.lineNum = lineNum;
    }
    public Node(){}

    public String getSampleText() { return sampleText;}
    public void setSampleText(String sampleText) {this.sampleText = sampleText;}

    public int getLineNum() {return lineNum;}
    public void setLineNum(int lineNum) {this.lineNum = lineNum;}

    public Node getNext() {return next;}
    public void setNext(Node next) {this.next = next;}
}