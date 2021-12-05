import java.io.*;
import java.util.*;

/**
 *
 */
public class Cache {

    /** Set Size */
    private final String setSize;

    /** number of sets */
    private final String numSet;

    /** line size */
    private final String lineSize;

    /** num of hits */
    private int hit;

    /** num of misses */
    private int miss;

    /** access array of data */
    private String[][] info;

    /** number of arrays in info */
    private int infoLen;

    /** Parameter of program */
    private final String param;

    /** Number of accesses */
    private int access;

    /** Number of memory references */
    private int refer;

    public Cache(String[][] info, String numSet, String setSize, String lineSize){
        this.setSize = setSize;
        this.numSet = numSet;
        this.lineSize = lineSize;
        this.info = info;
        this.hit = 0;
        this.miss = 0;
        this.refer = 0;
        this.access = 0;
        this.param = "";
    }

    public Cache(String[][] info, String numSet, String setSize, String lineSize, String param){
        this.setSize = setSize;
        this.numSet = numSet;
        this.lineSize = lineSize;
        this.info = info;
        this.hit = 0;
        this.miss = 0;
        this.refer = 0;
        this.access = 0;
        this.param = param;
    }

    /**
     * Constructor just used for testing!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     * @param setSize
     * @param numSet
     * @param lineSize
     */
    public Cache(String setSize, String numSet, String lineSize){
        this.setSize = setSize;
        this.numSet = numSet;
        this.lineSize = lineSize;
        this.hit = 0;
        this.miss = 0;
        this.refer = 0;
        this.access = 1;
        this.param = "F";
    }


    public void go(){
        String message = "";
       // getInfoLength();
        message = firstOutput();
        System.out.println(firstOutput());
        message += doWork();
        message += printSummary();
        System.out.println(printSummary());
        if(param.equals("F")){
            message += finalState();
            System.out.println(finalState());
        }
        //Do something with message
        //print message or write to file?????????????????????
    }

    public void getInfoLength(){
        int cnt = 1;
        for(int i = 0; i < this.info.length; i++){
            if(this.info[i][0] != null){
                cnt++;
            }
        }
        this.infoLen = cnt;
    }


    public String firstOutput(){
        String val = "Cache Configuration\n\n";
        val += "\t\t" + "2-way" + " entries\n";
        val += "\t\t" + setSize + " sets total\n";
        val += "\t\t" + "8 words per set\n";
        val += "\n\nResults for Each Reference\n";
        val += "\nAccess Address   Tag   Index Offset Result Memrefs";
        val += "\n------ ------- ------- ----- ------ ------ -------\n";
        return val;
    }

    public String doWork(){
        String data = "";
        int maxSize = largestAddress();
        int bin = 0;
        int[] tag;
        String access, address, binary, val, result, newTag, memRef = "";
        for(int i = 0; i < infoLen - 1; i++){
            access = accessName(info[i][1]);
            data += " " + access;
            address = info[i][0];
            bin = Integer.parseInt(address, 16); //turn into int
            binary = Integer. toBinaryString(bin);//int into binary string
            data += " " + binary;
            data += " " + info[i][0];
            //Get tag
            tag = getTag(binary);
            //Get index

            //Get offset

            //Get hit or miss

            //Get mem reference
        }
        return data;
    }

    public int[] getTag(String binary){
        int[] values = new int[3];
        int index = calcIndex(binary);
        int offset = calcOffset(binary);
        values[1] = index;
        values[2] = offset;
        //Get offset values
        int indexBit = getIndex();   //Getting the bit amounts for index
        int offsetBit = getOffset(); //Getting bit amount for offset
        int min = 0;
        int max = binary.length() - (indexBit + offsetBit);
        String binaryValue = "";
        //Grabbing the right bits for the tag
        for(int i = 0; i < max; i++){
            binaryValue += Character.toString(binary.charAt(min));
            min++;
        }
        String padded = String.format("%0"+(8- binaryValue.length())+"d%s", 0, binaryValue);
        values[0] = Integer.parseInt(padded, 2);//Padding and converting tag to int
        return values;
    }

    public int calcIndex(String binary){
        int indexBit = getIndex();  //how many bits are in index
        int offsetBit = getOffset();//how many bits are in offset
        String padded = addZeros(binary);   //pads the binary
        String binaryValue = "";
        int min = padded.length() - (indexBit + offsetBit);
        for(int i = 0; i < indexBit; i++){
            binaryValue += Character.toString(padded.charAt(min));
            min++;
        }
        return Integer.parseInt(binaryValue, 2);
    }

    public String addZeros(String binary){
        int max = largestBinary();
        String zero = "0";
        for(int i = 0; i < max + 1; i++){
            if(binary.length() < max){
                binary = zero + binary;
            }
        }
        return binary;
    }

    public int getIndex(){
        //splitting it up because it has a space before it
        String[] check = this.numSet.split(" ");
        String value = check[1];//getting the string value of teh index
        int set = Integer.parseInt(value);
        double index = Math.log(set)/Math.log(2);//Math ofr infex
        return (int)index;
    }

    public int calcOffset(String binary){
        int offsetBit = getOffset();
        int length = binary.length() - offsetBit;
        String binaryValue = "";
        for(int i = 0; i < offsetBit; i++){
            binaryValue += Character.toString(binary.charAt(length));
            length++;//bits for offset in one string
        }
        String padded = String.format("%0"+(8-binaryValue.length())+"d%s",0,binaryValue);
        return Integer.parseInt(padded, 2);
    }

    public int getOffset(){
        String check[] = this.lineSize.split(" ");
        //Splitting information properly
        String value = check[1];
        int size = Integer.parseInt(value);
        double offset = Math.log(size)/Math.log(2); //Calculating the offset into double
        return (int)offset;
    }

    public String accessName(String value){
        if(value.equalsIgnoreCase("R")){
            return "read";
        }
        return "write";
    }

    public int largestAddress(){
        int max = 0;
        for(int i = 0; i < this.infoLen; i++){
            if(this.info[i][0] != null){
                int addLength = this.info[i][0].length() - 1;
                if(addLength > max){
                    max = addLength;
                }
            }
        }
        return max;
    }

    public int largestBinary(){
        int max = 0;
        for(int i = 0; i < infoLen; i++){
            if(info[i][0] != null){
                int bin = Integer.parseInt(info[i][0], 16);//String to int
                String binary = Integer.toBinaryString(bin);    //Int to binary
                if(binary.length() > max){
                    max = binary.length();
                }
            }
        }
        return max;
    }

    public String printSummary(){
        String val = "\nSimulation Summary Statistics\n";
        val += "---------------------------------\n";
        val += "Total hits                   : " + this.hit;
        val += "\nTotal misses                 : " + this.miss;
        val += "\nTotal accesses               : " + access;
        val += "\nTotal memory references      : " + refer;
        val += "\nHit ratio                    : " + (hit/access);
        val += "\nMiss ratio                   : " + (1-miss);
        return val;
    }

    public String finalState(){
        String val = "\n\n\tFinal Data Cache State";
        val += "\n-----------------------------\n";
        return val;
    }
}
