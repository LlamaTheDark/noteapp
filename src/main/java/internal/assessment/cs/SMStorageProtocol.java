package internal.assessment.cs;
import java.util.LinkedList;

//TODO: implement deleting an item from the linkedList

public class SMStorageProtocol {

    LinkedList<Object> items = new LinkedList<>();

    public static SMStorageProtocol parseStringToSMSP(String StringSMSP){
        SMStorageProtocol SMSP = new SMStorageProtocol();
        String[] splitByValue; // splits name from value
        String[] splitByItems = StringSMSP.split("\\>"); // splits string into declarations of items.

        for(int i = 1; i < splitByItems.length; i++){
            String s = splitByItems[i];
            if(s.contains("[") || s.contains("]")){ // if it has brackets then it has to be an array
                splitByValue = s.split("[=]\\[");
                String[] arrayString = splitByValue[1].split(",");

                String[] nv;
                SMArrayItem SMAI = new SMArrayItem();
                for(String a : arrayString){
                    nv = Model.getNameAndValue(a);
                    SMAI.add(nv[0], nv[1]);
                }

                //splitByValue[0].replaceAll("=", "");
                SMSP.put(splitByValue[0], SMAI); // splitByValue[0] holds the name
            }else{
                String[] nv = Model.getNameAndValue(s);
                SMSP.put(nv[0], nv[1]);
            }
        }
        return SMSP;
    }

    @Override
    public String toString(){
        String parsedString = "";
        for(Object item : items){
            parsedString += ">" + item.toString() + "";
        }
        return parsedString;
    }

    public void put(String name, String value) { // polymorphism -- outputs the successs of teh addition
        int count = 0;
        if(isEmpty()) {
            items.add(new SMStringItem(name, value));
            return;
        }else{
            while(count < items.size()) {
                Object o = items.get(count);
                try{
                    if(((SMStringItem)o).getName().equals(name)){
                        items.set(count, new SMStringItem(name, value));
                        return;
                    }else{
                        count++;
                    }
                }catch(ClassCastException e){
                    count++;
                }
            }
            items.add(new SMStringItem(name, value));
        }
        return;
    }
    public void put(String name, SMArrayItem arrayItem){ // TODO: this doesn't work
        int count = 0;
        if(isEmpty()) {
            items.add(new SMArrayItem(name, arrayItem));
            return;
        }else{
            while(count < items.size()) {
                Object o = items.get(count);
                try{
                    if(((SMArrayItem)o).getName().equals(name)){
                        items.set(count, new SMArrayItem(name, arrayItem));
                        return;
                    }else{
                        count++;
                    }
                }catch(ClassCastException e){
                    count++;

                }
            }
            items.add(count, new SMArrayItem(name, arrayItem));
        }
        return;
    }

    public void remove(String name){
        int count = 0;
        if(isEmpty()) {
            return;
        }else{
            while(count < items.size()) {
                Object o = items.get(count);
                try{
                    if(((SMArrayItem)o).getName().equals(name)){
                        items.remove(count);
                        return;
                    }else{
                        count++;
                    }
                }catch(ClassCastException e){
                    try{
                        if(((SMStringItem)o).getName().equals(name)){
                            items.remove(count);
                            return;
                        }else{
                            count++;
                        }
                    }catch(ClassCastException exception){
                        count++;
                    }

                }
            }
            items.remove(count);
        }
        return;
    }
    public Object get(String name){
        int count = 0;
        if(isEmpty()) {
            return null;
        }else{
            while(count < items.size()) {
                Object o = items.get(count);
                try{
                    if(((SMArrayItem)o).getName().equals(name)){
                        return o;
                    }else{
                        count++;
                    }
                }catch(ClassCastException e){
                    try{
                        if(((SMStringItem)o).getName().equals(name)){
                            return o;
                        }else{
                            count++;
                        }
                    }catch(ClassCastException exception){
                        count++;
                    }
                }
            }
        }
        return null;
    }


    private boolean isEmpty(){return items.size()==0;}
}

class SMStringItem {
    private String name, value;

    public SMStringItem (String name, String value){
        this.name = name;
        this.value = value;
    }

    public String getName(){ return name; }
    public String getValue(){ return value; }

    @Override
    public String toString(){
        return name + "=" + value;
    }
}