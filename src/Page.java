import java.util.Comparator;

public class Page implements Comparable{
    public String url;
    public String content;
    public int hit;

    Page(String url, String content){
        this.url = url;
        this.content = content;
    }

    public int compareTo(Object o){
        Page p = (Page)o;
        return p.hit - this.hit;
    }
}