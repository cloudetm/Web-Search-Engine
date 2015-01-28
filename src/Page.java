public class Page implements Comparable{
    public String url;
    public String content;
    public int found; // this shouldn't really be here but it's here for the ease of implementation

    Page(String url, String content){
        this.url = url;
        this.content = content;
    }

    public int compareTo(Object o){
        Page p = (Page)o;
        return p.found - this.found;
    }
}