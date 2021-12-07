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
    private int refer = 0;

    /** Array to see if value was read or write */
    private String[] memLocations = new String[5000];

    /** String array to hold any used addresses */
    public String[] usedAddress = new String[10000];

    public Cache(String[][] info, String numSet, String setSize, String lineSize){
        this.numSet = numSet.replaceAll("\\s", "");
        this.setSize = setSize.replaceAll("\\s", "");
        this.lineSize = lineSize.replaceAll("\\s", "");
        this.info = info;
        this.hit = 0;
        this.miss = 0;
        this.refer = 0;
        this.access = 0;
        this.param = "";
    }

    public Cache(String[][] info, String numSet, String setSize, String lineSize, String param){
        this.setSize = setSize.replaceAll("\\s", "");
        this.numSet = numSet.replaceAll("\\s", "");
        this.lineSize = lineSize.replaceAll("\\s", "");
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
        this.setSize = setSize.replaceAll("\\s", "");
        this.numSet = numSet.replaceAll("\\s", "");
        this.lineSize = lineSize.replaceAll("\\s", "");
        this.hit = 0;
        this.miss = 0;
        this.refer = 0;
        this.access = 1;
        this.param = "F";
    }


    public void go(){
        String message = "";
        getInfoLength();
        message = firstOutput(getCache());
        System.out.println(firstOutput(getCache()));
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
        for(int i = 0; i < info.length; i++){
            if(info[i][0] != null){
                cnt++;
            }
        }
        infoLen = cnt;
    }


    public String doWork(){
        String data = "";
        int bin = 0;
        int[] tag;
        String[] lineVal = new String[8];
        String access, address, binary, value, result, newTag, memRef = "";
        for(int i = 0; i < infoLen - 1; i++){
            access = accessName(info[i][1]);
            lineVal[0] = " " + access;//Access
            address = info[i][0];
            bin = Integer.parseInt(address, 16); //turn into int
            binary = Integer. toBinaryString(bin);//int into binary string
            lineVal[1] = " " + info[i][0]; //Address
            //Get tag
            tag = getTag(binary);
            lineVal[2] = tag[0] + "";//Tag
            //lineVal[3] = " " + tag[0];
            //Get index
            lineVal[3] = getIndexLength(tag[1]);
            //Get offset
            lineVal[4] = getOffsetLength(tag[2]);
            //Get hit or miss
            result = getHitorMiss(info[i][0]);
            lineVal[5] = result;
            //Get mem reference
            newTag = "" + tag[0];
            memRef = getMemRef(result, newTag);
            lineVal[6] = "       " + memRef;
            data += buildLine(lineVal);
        }
        System.out.println(data);
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
        if(indexBit == 0){
            return 0;
        }
        return Integer.parseInt(binaryValue, 2);
    }

    public String getIndexLength(int index){
        String val = " ";
        int maxSpace = 5;
        //if index is max characters length
        if(index == maxSpace){
            return "";
        }
        String dumb = index + "";
        int left = maxSpace - dumb.length();
        for(int i = 0; i < left; i++){
            val +=" ";    //Adding spaces to output
        }
        val += index;
        return val;
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
        int set = Integer.parseInt(numSet);
        double index = Math.log(set)/Math.log(2);//Math for index
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
        int size = Integer.parseInt(lineSize);
        double offset = Math.log(size)/Math.log(2); //Calculating the offset into double
        return (int)offset;
    }

    public String getOffsetLength(int offset){
        String val = " ";
        int maxSpace = 5;
        //if offset is the max length
        if(offset == maxSpace){
            return "";
        }
        int left = maxSpace - offset;
        for(int i = 0; i < left; i++){
            val += " ";   //adding spaces
        }
        val += offset;
        return val;
    }

    public String accessName(String value){
        if(value.equalsIgnoreCase("R")){
            return "read";
        }
        refer++;
        return "write";
    }

    public String getHitorMiss(String tag){
        String val = "MISS";
        int a = 0;
        for(int i = 0; i < memLocations.length; i++){
            if(memLocations[i] != null){
                if(memLocations[i].equals(tag)){
                    val = "HIT";//changing result
                    this.hit++;
                    return val;
                }
                a++;
            }
        }
        miss++;
        refer++;
        //storing tag into access
        memLocations[a] = tag;
        return val;
    }

    public String getMemRef(String result, String tag){
        String val = "1";
        if(result.equalsIgnoreCase("hit")){
            val = "0";
        }
        if(accessedBefore(tag)){
            val = "2";
        }
        return val;
    }

    public boolean accessedBefore(String tag){
        boolean val = false;
        int cnt = 0;
        for(int i = 0; i < usedAddress.length; i++){
            if(cnt == 2){
                val = true;
                deleteAddress(tag);
            }
            if(usedAddress[i] != null){
                if(usedAddress[i].equals(tag)){
                    cnt++;
                }
            }
        }
        return val;
    }

    public void deleteAddress(String tag){
        for(int i = 0; i < usedAddress.length; i++){
            if(usedAddress[i] != null){
                if(usedAddress[i].equals(tag)){
                    usedAddress[i] = null;
                }
            }
        }
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

    public String buildLine(String[] lineVal){
        String line = padLeft(lineVal[0], 6);//Access
        line += padLeft(lineVal[1], 8);//Address
        line += padLeft(lineVal[2], 8);//Tag
        line += padLeft(lineVal[3], 6);//Index
        line += padLeft(lineVal[4], 7);//Offset
        line += padLeft(lineVal[5], 7);//Result
        line += padLeft(lineVal[6], 8);//Memref
        line += "\n";
        return line;
    }

    public static String padLeft(String s, int n) {
        return String.format("%" + n + "s", s);
    }

    public static String padRight(String s, int n) {
        return String.format("%-" + n + "s", s).replace(' ', '0');
    }

    public String firstOutput(String cache){
        String val = "Cache Configuration\n\n";
        val += cache;
        val += "\nResults for Each Reference\n";
        val += "\nAccess Address   Tag   Index Offset Result Memrefs";
        val += "\n------ ------- ------- ----- ------ ------ -------";
        return val;
    }

    public String getCache(){
        String cache = "";
        int words = Integer.parseInt(lineSize)/4;
        cache += "\t\t" + setSize + "-way set associative entries\n";
        cache += "\t\t" + numSet + " sets total\n";
        cache += "\t\t" + words + " words per set\n";
        if(setSize.equals("1")){
            cache += "\n\t\tDIRECT MAPPED CACHE\n";
        }
        return cache;
    }

    public String printSummary(){
        String val = "\nSimulation Summary Statistics\n";
        int total = hit+miss;
        float hitRatio = (float) hit/total;
        float missRatio = (float) 1 - hitRatio;
        val += "---------------------------------\n";
        val += "Total hits                     : " + this.hit;
        val += "\nTotal misses                   : " + this.miss;
        val += "\nTotal accesses                 : " + (hit+miss);
        val += "\nTotal memory references        : " + refer;
        val += "\nHit ratio                      : " + padRight(hitRatio + "", 8);
        val += "\nMiss ratio                     : " + padRight(missRatio + "", 8);
        return val;
    }

    public String finalState(){
        String val = "\n\n\tFinal Data Cache State";
        val += "\n-----------------------------\n";
        return val;
    }
}
