import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class WebSearchEngine {
    public static void main(String[] args){
        Scanner in = new Scanner(System.in);
        String baseUrl;
        Query query;

        System.out.println("Enter a website to search from:");
        baseUrl = in.nextLine();
        System.out.println("Enter words to search for:");
        query = new Query(in.nextLine());

        Crawler c = new Crawler(baseUrl);

        ArrayList<Page> pages = c.crawl();

        for(Page p : pages){
            p.hit = SuffixArray.searchFromPage(p, query);
        }

        Collections.sort(pages);

        for(Page p : pages){
            if(p.hit > 0) {
                System.out.printf("%d\tfound in %s\n", p.hit, p.url);
            }
        }
    }
}