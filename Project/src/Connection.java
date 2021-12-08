public class Connection {
    private int tag;
    private int index;
    private Connection next;

    public Connection(int tag, int index){
        this.tag = tag;
        this.index = index;
        this.next = null;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public Connection getNext(){
        return this.next;
    }

    public void setNext(Connection conn){
        this.next = conn;
    }

    public boolean hasNext(){
        return this.next != null;
    }
}
