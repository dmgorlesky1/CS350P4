import java.io.*;
import java.sql.Array;
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

    private ArrayList<ArrayList<Integer>> indexMap;

    private ArrayList<ArrayList<Integer>>  tagMap;

    private ArrayList<ArrayList<String>> addressMap;


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
        int num = Integer.parseInt(this.numSet);

        indexMap = new ArrayList<ArrayList<Integer>>(num);
        tagMap = new ArrayList<ArrayList<Integer>>(num);
        addressMap = new ArrayList<ArrayList<String>>(num);

        for(int i = 0; i < num; i++){
            int j = 9000;
            ArrayList<Integer> c = new ArrayList<>(num);
            c.add(j);
            ArrayList<Integer> d = new ArrayList<>(num);
            d.add(j);
            ArrayList<String> s = new ArrayList<>(num);
            s.add("9999");

            indexMap.add(c);
            tagMap.add(d);
            addressMap.add(s);
        }
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
        int num = Integer.parseInt(this.numSet);

        indexMap = new ArrayList<ArrayList<Integer>>(num);
        tagMap = new ArrayList<ArrayList<Integer>>(num);
        addressMap = new ArrayList<ArrayList<String>>(num);

        for(int i = 0; i < num; i++) {
            int j = 9000;
            ArrayList<Integer> c = new ArrayList<>(num);
            c.add(j);
            ArrayList<Integer> d = new ArrayList<>(num);
            d.add(j);
            ArrayList<String> s = new ArrayList<>(num);
            s.add("9999");

            indexMap.add(c);
            tagMap.add(d);
            addressMap.add(s);
        }
    }

    public void go(){
        String message = "";
        getInfoLength();
        message = firstOutput(getCache());
        System.out.println(firstOutput(getCache()));
        message += doWork();
        message += printSummary();
        System.out.println(printSummary());
        //if(param.equals("F")){
            message += finalState();
            System.out.println(finalState());
            message += printMap();
            System.out.println(printMap());
        //}
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
        Line line;
        String[] lineVal = new String[8];
        String address, binary, result, offset, memRef = "";
        for(int i = 0; i < infoLen - 1; i++){
            line = new Line(numSet, lineSize);
            line.accessName(info[i][1]);
            address = info[i][0];
            bin = Integer.parseInt(address, 16); //turn into int
            binary = Integer.toBinaryString(bin);//int into binary string\
            line.setAddress(address);
            //Get tag
            tag = line.getTag(binary);
            line.setTag(tag[0] +"");
            //Get index
            index = line.getIndexLength(tag[1]);//Index
            index = index.replaceAll("\\s", "");
            line.setIndex(index);
            //Get offset
            offset = line.getOffsetLength(tag[2]);//Offset
            line.setOffset(offset);
            //Get hit or miss
            result = getHitorMiss(tag[0]+"", index, address);
            lineVal[0] = result; //Result
            //Get mem reference
            memRef = getMemRef(result, tag[0]+"", index);
            lineVal[1] = "       " + memRef;
            data += line.buildLine(lineVal);
        }
        System.out.println(data);
        return data;
    }

    public String getHitorMiss(String tag, String index, String address){
        index = index.replaceAll("\\s", "");
        int ind = Integer.parseInt(index);
        int tags = Integer.parseInt(tag);
        String val = "MISS";
        int a = 0;
        for(int i = 0; i < memLocations.length; i++){
            if(memLocations[i][0] != null){
                if(memLocations[i][0].equals(tag)){
                    if(memLocations[i][1].equals(index)) {
                        if(Integer.parseInt(setSize) == 1) {
                            a = directMap(index);
                            memRef[a] = 5;
                        }
                        updateMaps(tags, ind, address);
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
        if(indexMap.get(ind).get(0) >= 9000){
            indexMap.get(ind).add(0,ind);
            indexMap.get(ind).remove(1);
            tagMap.get(ind).add(0,tags);
            tagMap.get(ind).remove(1);
            addressMap.get(ind).add(0, address);
            addressMap.get(ind).remove(1);
        } else {
            updateMaps(tags, ind, address);
        }
        usedAddress[a][0] = tag;
        usedAddress[a][1] = index;
        return val;
    }

    public void updateMaps(int tags, int ind, String address){
        if(tagMap.get(ind).contains(tags)){
            int indexOf = tagMap.get(ind).indexOf(tags);
            indexMap.get(ind).remove(indexOf);
            tagMap.get(ind).remove(indexOf);
            addressMap.get(ind).remove(indexOf);
        }
        indexMap.get(ind).add(ind);
        tagMap.get(ind).add(tags);
        addressMap.get(ind).add(address);
        if(tagMap.get(ind).size() > Integer.parseInt(setSize)){
            indexMap.get(ind).remove(0);
            tagMap.get(ind).remove(0);
            addressMap.get(ind).remove(0);
        }
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

        val += "-----------------------------";
        val += "\nTotal hits                : " + this.hit;
        val += "\nTotal misses              : " + this.miss;
        val += "\nTotal accesses            : " + (hit+miss);
        val += "\nTotal memory references   : " + refer;
        val += "\nHit ratio                 : " + hitRatio;
        val += "\nMiss ratio                : " + missRatio;
        return val;
    }

    public String finalState(){
        String val = "\n\n\tFinal Data Cache State";
        val += "\n-----------------------------";
        return val;
    }

    public String printMap(){
        String empty;
        String total = "";
        int size = Integer.parseInt(setSize);
        for(int i = 0; i < indexMap.size(); i++){
            total += "set " + i + "\n";
            for(int j = 0; j < indexMap.get(i).size(); j++){
                if(addressMap.get(i).size() == 0){
                    empty = "invalid";
                } else if(addressMap.get(i).get(0).equals("9999")) {
                    empty = "invalid";
                } else {
                    empty = "byte address " + addressMap.get(i).get(j) + ", tag " +
                            tagMap.get(i).get(j) + ", lru";
                    if(j+1 == indexMap.get(i).size()){
                        empty += " 1";
                    } else {
                        if (addressMap.get(i).get(j + 1) == null) {
                            empty += " 1";
                        } else {
                            empty += " 0";
                        }
                    }
                }
                total += "\tline " + j + " = " + empty + "\n";
                if(indexMap.get(i).size() < size){
                    for(int k = indexMap.get(i).size(); k < size; k++){
                        total += "\tline " + k + " = " + empty + "\n";
                    }
                }
            }
        }
        return total;
    }
}
