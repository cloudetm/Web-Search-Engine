import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

// Runner
public class WebSearchEngine {
    public static void main(String[] args){
        String baseUrl = "http://www.osss.cs.tsukuba.ac.jp/kato/codeconv/CodeConvTOC.doc.html";
        Query query = new Query("Java &Programming -Practices");

        System.out.printf("Searching from %s\n", baseUrl);
        System.out.printf("Searching for: %s\n", query.query);

        Crawler c = new Crawler(baseUrl);

        ArrayList<Page> pages = c.crawl();

        for(Page p : pages){
            p.found = SuffixArray.searchFromPage(p, query);
        }

        Collections.sort(pages);

        for(Page p : pages){
            if(p.found > 0) {
                System.out.printf("%d\tfound in %s\n", p.found, p.url);
            }
        }
    }
}