import java.util.ArrayList;

public class Query {
    public String query;
    public ArrayList<String> or;
    public ArrayList<String> and;
    public ArrayList<String> not;

    public Query(String query) {
        this.query = query;
        this.or = new ArrayList<String>();
        this.and = new ArrayList<String>();
        this.not = new ArrayList<String>();

        String[] tokens = query.split(" ");

        for (String t : tokens) {
            if(t.length() == 1){
                this.or.add(t);
                continue;
            }

            switch(t.charAt(0)){
                case '&':
                    this.and.add(t.substring(1));
                    break;
                case '-':
                    this.not.add(t.substring(1));
                    break;
                default:
                    this.or.add(t);
                    break;
            }
        }
    }
}