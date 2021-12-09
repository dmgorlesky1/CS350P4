public class Line {
    public int index;
    public int tag;
    public int offset;
    public String address;
    public String numSet;
    public String lineSize;


    public Line(String numSet, String lineSize){
        this.address = "";
        this.index = 0;
        this.tag = 0;
        this.offset = 0;
        this.numSet = numSet;
        this.lineSize = lineSize;
    }

    public String accessName(String value){
        if(value.equalsIgnoreCase("R")){
            return "read";
        }
        return "write";
    }

    public void setAddress(String address) {
        this.address = address;
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

    public int getIndex(){
        int set = Integer.parseInt(numSet);
        double index = Math.log(set)/Math.log(2);//Math for index
        return (int)index;
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

    public int getOffset(){
        int size = Integer.parseInt(lineSize);
        double offset = Math.log(size)/Math.log(2); //Calculating the offset into double
        return (int)offset;
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

    public static String padLeft(String s, int n) {
        return String.format("%" + n + "s", s);
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
}
