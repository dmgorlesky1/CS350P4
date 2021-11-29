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

    public Cache(String[][] info, String setSize, String numSet, String lineSize){
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

    public Cache(String[][] info, String setSize, String numSet, String lineSize, String param){
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
        conditionChecking();
       // getInfoLength();
        message = firstOutput();
        System.out.println(firstOutput());
        message += printSummary();
        System.out.println(printSummary());
        if(param.equals("F")){
            message += finalState();
            System.out.println(finalState());
        }
        //Do something with message

    }

    public void conditionChecking () {
        String value = lineSize; //Making sure the line size isn't less than 4 bytes
        int checking = Integer.parseInt(value);
        if (checking < 4) {
            System.out.println("Error: Line size is less than 4. Please retry.");
            System.out.println("Usage: Driver <f|F>");
            System.exit(1);
        }

        if (checking % 2 != 0) { //Making sure the line size is a power of 2
            System.out.println("Error: Line size is not a power of two. Please retry.");
            System.out.println("Usage: Driver <f|F>");
            System.exit(1);
        }

        checking = Integer.parseInt(numSet);
        if (checking % 2 != 0) {    //Making sure the number of sets is a power of 2
            System.out.println("Error: Number of sets is not a power of two. Please retry.");
            System.out.println("Usage: Driver <f|F>");
            System.exit(1);
        }
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
