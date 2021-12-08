import java.io.*;
import java.text.DecimalFormat;
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
    private String[][] memLocations = new String[5000][5000];

    private int[] memRef = new int[5000];

    /** String array to hold any used addresses */
    public String[][] usedAddress = new String[10000][10000];

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
        String index = "";
        int[] tag;
        String[] lineVal = new String[8];
        String access, address, binary, result, newTag, memRef = "";
        for(int i = 0; i < infoLen - 1; i++){
            access = accessName(info[i][1]);
            lineVal[0] = " " + access;//Access
            address = info[i][0];
            bin = Integer.parseInt(address, 16); //turn into int
            binary = Integer.toBinaryString(bin);//int into binary string
            lineVal[1] = " " + info[i][0]; //Address
            //Get tag
            tag = getTag(binary);
            lineVal[2] = tag[0] + "";//Tag
            //Get index
            lineVal[3] = getIndexLength(tag[1]);//Index
            index = getIndexLength(tag[1]);//Index
            index = index.replaceAll("\\s", "");
            //Get offset
            lineVal[4] = getOffsetLength(tag[2]);//Offset
            //Get hit or miss
            result = getHitorMiss(tag[0]+"", index);
            lineVal[5] = result; //Result
            //Get mem reference
            memRef = getMemRef(result, tag[0]+"", index);
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
        if(indexBit == 0){
            return 0;
        }
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

    public String getIndexLength(int index){
        String val = " ";
        int maxSpace = 5;
        //if index is max characters length
        if(index == maxSpace){
            return "5";
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
        int max = Integer.parseInt(lineSize);
        String zero = "0";
        for(int i = 0; i < max; i++){
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
            return "5";
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
        return "write";
    }

    public String getHitorMiss(String tag, String index){
        index = index.replaceAll("\\s", "");
        String val = "MISS";
        int a = 0;
        for(int i = 0; i < memLocations.length; i++){
            if(memLocations[i][0] != null){
                if(memLocations[i][0].equals(tag)){
                    if(memLocations[i][1].equals(index)) {
                        if(Integer.parseInt(setSize) == 1) {
                            a = directMap(index);
                            memRef[a] = 5;
                        } else {

                        }
                        hit++;
                        return "HIT";
                    }
                }
                a++;
            }
        }
        if(Integer.parseInt(setSize) == 1) {
            a = directMap(index);
        }

        miss++;
        //storing tag into memory locations
        memLocations[a][0] = tag;
        //storing index into memory locations
        memLocations[a][1] = index;

        usedAddress[a][0] = tag;
        usedAddress[a][1] = index;
        return val;
    }

    public int directMap(String index){
        for(int i = 0; i < memLocations.length; i++){
            if(memLocations[i][1] != null) {
                if (memLocations[i][1].equalsIgnoreCase(index)) {
                    return i;
                }
            }
        }
        return 0;
    }

    public String getMemRef(String result, String tag, String index){
        String val = "1";
        if(result.equalsIgnoreCase("hit")){
            return "0";
        }
        if(accessedBefore(tag, index)){
            refer += 2;
            return "2";
        }
        refer += 1;
        return val;
    }

    public boolean accessedBefore(String tag, String index){
        if(setSize.equals("1")){
            return doDirect(index);
        }
        boolean val = false;
        int cnt  = 0;
        for(int i = 0; i < usedAddress[0].length; i++){
            if(usedAddress[i][0] != null){
                if(usedAddress[i][0].equals(tag) && usedAddress[i][1].equals(index)){
                    if(memRef[i] > 0){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean doDirect(String index){
        boolean val = false;
        int cnt  = 0;
        for(int i = 0; i < usedAddress.length; i++){
            if(usedAddress[i][1] != null){
                if(usedAddress[i][1].equals(index)){
                    if(memRef[i] > 0){
                        return true;
                    }
                }
            }
        }
        return val;
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
        String val = "Simulation Summary Statistics\n";

        int total = hit+miss;
        float hitRatio = (float) hit/total;
        float missRatio = (float) 1 - hitRatio;

        val += "---------------------------------";
        val += "\nTotal hits                     : " + this.hit;
        val += "\nTotal misses                   : " + this.miss;
        val += "\nTotal accesses                 : " + (hit+miss);
        val += "\nTotal memory references        : " + refer;
        val += "\nHit ratio                      : " + hitRatio;
        val += "\nMiss ratio                     : " + missRatio;
        return val;
    }

    public String finalState(){
        String val = "\n\n\tFinal Data Cache State";
        val += "\n-----------------------------\n";
        return val;
    }
}
