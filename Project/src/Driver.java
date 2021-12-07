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
    public static void main (String[] args) throws IOException {
        //Checking if CMA is present & equals F
        /**String cma = "";
         if(args.length  > 0){
            if(args[0].equalsIgnoreCase("F")){
                cma = "F";
            }
         }
         Scanner scan = new Scanner(System.in);*/
        String[][] infoArray = new String[5000][3];
        String[] array = new String[3];

        File file = new File(args[0]);
        Scanner scan = new Scanner(file);
        //String fileName = scan.nextLine();
        //String[] returned = fileName.split(":");

        //array[0] = returned[1]; //number of sets
        //fileName = scan.nextLine();

        //returned = fileName.split(":");
        //array[1] = returned[1]; //set size

        //fileName = scan.nextLine();
        //returned = fileName.split(":");
        //array[2] = returned[1]; //line size
        String data2 = "";
        //conditionChecking(array);
        int i = 0;
        String[] returned;

        //while(scan.hasNext()){
        int m = 0;
        //TEST IF EACH REF ADDRESS IS PROPERLY ALIGNED!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        while (scan.hasNextLine()) {
            if (m == 0) {
                data2 = scan.nextLine();
                returned = data2.split(":");
                array[0] = returned[1];
            } else if (m == 1) {
                data2 = scan.nextLine();
                returned = data2.split(":");
                array[1] = returned[1];
            } else if (m == 2) {
                data2 = scan.nextLine();
                returned = data2.split(":");
                array[2] = returned[1];
                conditionChecking(array);
            } else {
                String[] innerArray = new String[3];
                //String data = scan.next();
                String data = scan.nextLine();
                String[] split = data.split(":");
                if (!split[1].equalsIgnoreCase("r") &&
                        !split[1].equalsIgnoreCase("w")) {
                    //Not a valid input
                    System.out.println("\nWARNING: Improper memory access '" +
                            data + "'. Skipping...\n");
                } else {

                    for (int j = 0; j < split.length; j++) {
                        innerArray[j] = split[j];   //Making inner array
                    }

                    for (int a = 0; a < innerArray.length; a++) {
                        infoArray[i][a] = innerArray[a];    //put into infoArray
                    }
                    i++;
                }
            }
            m++;
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
            e.printStackTrace();
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    public static void conditionChecking(String[] values){
        String[] check = values[0].split(" ");
        String check2 = check[1];
        int checking = Integer.parseInt(check2);
        if(checking > 5000){    //Making sure the number of sets isn't > 5000
            System.out.println("Number of sets exceeds 5,000");
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
            if(checking != 1) {
                System.out.println("Number of sets is not a power of two!");
                System.exit(1);
            }
        }
        check = values[2].split(" ");
        check2 = check[1];
        checking = Integer.parseInt(check2);
        if(checking%2 != 0){ //Making sure the line size is a power of 2
            System.out.println("Line size is not a power of two!");
            System.exit(1);
        }
    }
}
