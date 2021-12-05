import java.io.*;
import java.util.*;

/**
 *
 */
public class Cache {

    /** Set Size */
    private String setSize;

    /** number of sets */
    private String numSet;

    /** line size */
    private String lineSize;

    /** num of hits */
    private int hit;

    /** num of misses */
    private int miss;

    /** access array of data */
    private String[][] info;

    /** number of arrays in info */
    private int infoLen;

    /** Parameter of program */
    private String param;

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

            //Get index

            //Get offset

            //Get hit or miss

            //Get mem reference
        }
        return data;
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
