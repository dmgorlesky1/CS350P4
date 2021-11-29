import java.io.*;
import java.util.*;
/**
 *
 */
public class Driver {

    /**
     * @param args command line arguments
     */
    public static void main (String[] args) {
        //Checking if CMA is present & equals F
        String cma = "";
        if(args.length  > 0){
            if(args[0].equalsIgnoreCase("F")){
                cma = "F";
            }
        }

      /**  Scanner sc = new Scanner(System.in);
        String[][] info = new String[8000][3];
        String fileName = sc.nextLine();

        String[] returned = fileName.split(":");
        String numSets = returned[1]; //Getting number of sets
        if(!isInteger(numSets)){
            //Print error, NaN!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
          //  sc.close();
          //  System.exit(1);
        }

        fileName = sc.nextLine();
        returned = fileName.split(":");
        String setSize = returned[1]; //Getting set size
        if(!isInteger(setSize)){
            //Print error, NaN!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
          //  sc.close();
           // System.exit(1);
        }

        fileName = sc.nextLine();
        returned = fileName.split(":");
        String lineSize = returned[1]; //Getting line size
        if(!isInteger(lineSize)){
            //Print error, NaN!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            //sc.close();
           // System.exit(1);
        }

        int i = 0;
        while (sc.hasNext()) {
            String[] innerArray = new String[3];
            String data = sc.next();
            String[] split = data.split(":");
            for (int j = 0; j < split.length; j++) {
                innerArray[j] = split[j];   //Making the inner array
            }
            for (int a = 0; a < innerArray.length; a++) {
                info[i][a] = innerArray[a];    //putting it into info
            }
            i++;
        }*/
        try {
            //Used for print testing
            Cache cache = new Cache("2", "4", "4");
            /**Cache cache;
            if(cma.equals("")) {
                cache = new Cache(info, setSize, numSets, lineSize);
            } else {
                cache = new Cache(info, setSize, numSets, lineSize, cma);
            }*/
            cache.go();
        } catch (Exception e) {
            //ADD MESSAGE HERE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            //sc.close();
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    public static boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        if (length == 0) {
            return false;
        }
        int i = 0;
        if (str.charAt(0) == '-') {
            if (length == 1) {
                return false;
            }
            i = 1;
        }
        for (; i < length; i++) {
            char c = str.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }
}
