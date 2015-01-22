import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Crawler {
    private URL baseUrl;
    private String host;
    private ArrayList<URL> visitedUrlList;
    private int timeoutMillis = 5000;

    public static void main(String[] args) {
        Crawler c = new Crawler("http://www.osss.cs.tsukuba.ac.jp/kato/codeconv/CodeConvTOC.doc.html");
        c.crawl();
    }

    public Crawler(String baseUrl){
        try{
            this.baseUrl = new URL(baseUrl);
        }
        catch(MalformedURLException e){
            e.printStackTrace();
            System.out.printf("%s is a malformed URL.\n", baseUrl);
            return;
        }

        host = this.baseUrl.getHost();
        visitedUrlList = new ArrayList<URL>();
    }

    // start crawling
    public void crawl(){
        crawl(baseUrl);
    }

    // crawl starting from baseUrl
    private void crawl(URL url){
        visitedUrlList.add(url);

        try{
            Document doc = Jsoup.parse(url, timeoutMillis);

            ArrayList<URL> urlList = getUrlList(doc, true);
            System.out.printf("%s contains %d links.\n", url, urlList.size());

            for(URL u : urlList){
                System.out.printf("\t%s\n", u);
            }

            for(URL u : urlList){
                if(u.getHost().equals(host) && !visitedUrlList.contains(u)){
                    crawl(u);
                }
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    // get an ArrayList of all URLs contained in an HTML Document.
    private static ArrayList<URL> getUrlList(Document doc, boolean unique){
        ArrayList<URL> urlList = new ArrayList<URL>();
        Elements links = doc.select("a");
        URL url;
        int hashpos;

        for(Element l : links){
            try {
                // when we want unique addresses avoid duplicates
                if(unique){
                    // strip the URL hash to so that we can compare just the page's URL
                    hashpos = l.attr("abs:href").indexOf("#");
                    if(hashpos > 0) {
                        url = new URL(l.attr("abs:href").substring(0, hashpos));
                    }
                    else{
                        url = new URL(l.attr("abs:href"));
                    }

                    // add if not already in the list
                    if(!urlList.contains(url)){
                        urlList.add(url);
                    }
                }
                // when we don't care about uniqueness just add to list
                else {
                    url = new URL(l.attr("abs:href"));
                    urlList.add(url);
                }
            }
            catch(MalformedURLException e){
                /* ignore malformed URLs */
            }
        }

        return urlList;
    }
}