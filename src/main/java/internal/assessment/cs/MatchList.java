package internal.assessment.cs;

public class MatchList {
    Node head;
    String filename;
    private int length = 0;

    public int getlength() {return length;}

    public MatchList(Node head) { this.head = head; length++; }
    public MatchList(String filename) {this.filename = filename;}

    public void addNode(Node node) {
        if (!isEmpty()) {
            Node tmp = head;
            while(tmp.next!=null) {
                tmp=tmp.next;
            }
            tmp.next=node;
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
}

class Node{
    private String sampleText;
    private int lineNum;
    Node next;

    public Node(String sampleText, int lineNum) {
        this.sampleText = sampleText;
        this.lineNum = lineNum;
    }

    public String getSampleText() { return sampleText;}
    public void setSampleText(String sampleText) {this.sampleText = sampleText;}

    public int getLineNum() {return lineNum;}
    public void setLineNum(int lineNum) {this.lineNum = lineNum;}

    public Node getNext() {return next;}
    public void setNext(Node next) {this.next = next;}
}