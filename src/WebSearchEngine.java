import java.util.ArrayList;
import java.util.Scanner;

public class WebSearchEngine {
    public static void main(String[] args){
        Scanner in = new Scanner(System.in);
        String baseUrl;
        String query;

        /*
        System.out.println("Enter a website to search from:");
        baseUrl = in.nextLine();
        System.out.println("Enter words to search for:");
        query = in.nextLine();
        */

        baseUrl = "http://www.osss.cs.tsukuba.ac.jp/kato/codeconv/CodeConvTOC.doc.html";
        query = "convention";

        Crawler c = new Crawler(baseUrl, query);

        ArrayList<Page> pages = c.crawl();

    }
}