package internal.assessment.cs;

public class SMArrayItem { // TODO: back up current state of files before you change JSON to Seamus Morrison Storage Protocol
    private String name;
    private SMArrayNode head;

    public SMArrayItem(String name, SMArrayItem arrayItem){
        this.name = name;
        head = arrayItem.getHead();
    }
    public SMArrayItem(){}

    public SMArrayNode getHead(){ return head; }
    public String getName(){ return name; }

    public void add(String name, String value){
        if(isEmpty()){
            head = new SMArrayNode(name, value);
        }else{
            SMArrayNode tmp = new SMArrayNode(head);
            while(tmp.getNext()!=null){
                tmp = tmp.getNext();
            }
            tmp.setNext(new SMArrayNode(name, value));
        }
    }
    public void remove(String name){
        if(!isEmpty()){
            if(head.getName().equals(name)){
                head = head.getNext();
            }else {
                SMArrayNode tmp = new SMArrayNode(head);
                while (!tmp.getNext().getName().equals(name)) {
                    System.out.println(tmp.getName());
                    tmp = tmp.getNext();
                }
                tmp.setNext(tmp.getNext().getNext());
            }
        }
    }

    @Override
    public String toString(){
        String parsedString = name + "=[";
        if(isEmpty()){
            return "";
        }else{
            SMArrayNode tmp = head;
            while(tmp.getNext()!=null){
                parsedString += tmp.getName() + "=" + tmp.getValue() + ",";
                tmp = tmp.getNext();
            }
            parsedString += tmp.getName() + "=" + tmp.getValue();
        }
        return parsedString + "]";
    }

    private boolean isEmpty(){return head==null;}
}

class SMArrayNode{
    private SMArrayNode next;
    private String name, value;

    SMArrayNode(String name, String value){
        this.name = name;
        this.value = value;
    }
    SMArrayNode(SMArrayNode next){this.next = next;}

    public SMArrayNode getNext(){ return next; }
    public void setNext(SMArrayNode next){ this.next = next; }

    public String getName(){ return name; }
    public String getValue(){ return value; }

    @Override
    public String toString(){
        return name + ":" + value;
    }

}