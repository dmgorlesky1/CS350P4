import java.io.*;
import java.util.*;
/**
 *
 */
public class Driver {

    /**
     *
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
        Scanner scan = new Scanner(System.in);
        String[][] infoArray = new String[5000][3];
        String[] array = new String[3];

        String fileName = scan.nextLine();
        String[] returned = fileName.split(":");

        array[0] = returned[1]; //number of sets
        fileName = scan.nextLine();

        returned = fileName.split(":");
        array[1] = returned[1]; //set size

        fileName = scan.nextLine();
        returned = fileName.split(":");
        array[2] = returned[1]; //line size

        conditionChecking(array);
        int i = 0;
        while(scan.hasNext()){
            String[] innerArray = new String[3];
            String data = scan.next();
            String[] split = data.split(":");

            for(int j = 0; j < split.length; j++){
                innerArray[j] = split[j];   //Making inner array
            }

            for(int a = 0; a < innerArray.length; a++){
                infoArray[i][a] = innerArray[a];    //put into infoArray
            }
            i++;
        }
        try {
            //Used for print testing
            Cache cache = new Cache(infoArray, array[0], array[1], array[2]);
            /**Cache cache;
            if(cma.equals("")) {
                cache = new Cache(infoArray, array[0], array[1], array[2]);
            } else {
                cache = new Cache(infoArray, array[0], array[1], array[2], cma);
            }*/
            cache.go();
        } catch (Exception e) {
            //ADD MESSAGE HERE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            scan.close();
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


    public static void conditionChecking(String[] values){
        String[] check = values[0].split(" ");
        String check2 = check[1];
        int checking = Integer.parseInt(check2);
        if(checking > 5000){    //Making sure the number of sets isn't > 5000
            System.out.println("Number of sets exceeds 8,000");
            System.exit(1);
        }
        check = values[1].split(" ");
        check2 = check[1];
        checking = Integer.parseInt(check2);
        if(checking > 8){   //Making sure the set size isn't above 8
            System.out.println("Associativity level exceeds 8!");
            System.exit(1);
        }
        check = values[2].split(" ");
        check2 = check[1]; //Making sure the line size isn't less than 4 bytes
        checking = Integer.parseInt(check2);
        if(checking < 4){
            System.out.println("Line size is less than 4!");
            System.exit(1);
        }
        check = values[0].split(" ");
        check2 = check[1];
        checking = Integer.parseInt(check2);
        if(checking%2 != 0){    //Making sure the number of sets is a power of 2
            System.out.println("Number of sets is not a power of two!");
            System.exit(1);
        }
        check = values[2].split(" ");
        check2 = check[1];
        checking = Integer.parseInt(check2);
        if(checking%2 != 0){ //Making sure the line size is a power of 2
            System.out.println("Line size is not a power of two!");
            System.exit(1);
        }
        return;
    }
}
