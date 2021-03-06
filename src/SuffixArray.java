import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.io.*;

public class SuffixArray {
    // this main method is for task 1-4
    public static void main(String[] args){
        Scanner s = new Scanner(System.in);

        System.out.println("Input files to search from:");
        System.out.println("(comma separated with no space, like file1,file2,...)");
        String[] paths = s.nextLine().split((","));

        System.out.println("Input a string to search for:");
        String query = s.nextLine();

        searchFromFiles(paths, query);
    }

    // search from a page and return the number that a word in the query was found in the page
    public static int searchFromPage(Page page, Query query) {
        int[] sufAry = getSuffixArray(page.content);
        int orFound = 0;
        int andFound = 0;

        HashMap<String, int[]> orOcr = new HashMap<String, int[]>();
        HashMap<String, int[]> andOcr = new HashMap<String, int[]>();

        // return if a "not" keyword is found
        for(String notq : query.not){
            int[] ocrAry = findOccurrences(notq, page.content, sufAry);
            if(ocrAry.length > 0) {
                return 0;
            }
        }

        // find occurrences of "and" keywords
        for(String andq : query.and){
            int[] ocrAry = findOccurrences(andq, page.content, sufAry);
            if(ocrAry.length > 0) {
                andFound += ocrAry.length;
                andOcr.put(andq, ocrAry);
            }
            // return if not found
            else{
                return 0;
            }
        }

        // find occurrences of "or" keywords
        for(String orq : query.or){
            int[] ocrAry = findOccurrences(orq, page.content, sufAry);
            if(ocrAry.length > 0) {
                orFound += ocrAry.length;
                orOcr.put(orq, ocrAry);
            }
        }

        return andFound + orFound;
    }

    // take multiple files, concatenate the contents and search from it
    // not memory efficient at all, but it is the specification for task 1-4
    public static void searchFromFiles(String[] paths, String query){
        StringBuilder sb = new StringBuilder();
        String text;
        int[] lengths = new int[paths.length];

        for(int i=0; i<paths.length; i++) {
            try {
                text = new String(Files.readAllBytes(Paths.get(paths[i])), StandardCharsets.UTF_8);
                sb.append(text);
                sb.append('\0');
                lengths[i] = text.length() + 1;
            }
            catch (IOException e) {
                System.out.printf("Failed opening file: %s\n", paths[i]);
            }
        }

        // str is the string of all files concatenated
        String str = new String(sb);

        int[] sufAry = getSuffixArray(str);

        int[] ocrAry = findOccurrences(query, str, sufAry);

        // stores the sum of the lengths of preceding files
        int fileBeg = 0, fileEnd = 0;

        System.out.println();
        System.out.printf("Occurrences of query: %s\n", query);

        // for each file
        for(int i=0; i<paths.length; i++) {
            System.out.printf("################################\n");
            System.out.printf("In file %s:\n", paths[i]);
            fileEnd = fileBeg + lengths[i];
            // for each occurrence
            for (int o : ocrAry) {
                if(fileBeg <= o && o < fileEnd) {
                    if (o + query.length() + 20 < fileEnd) {
                        System.out.printf("%d: %s\n", o - fileBeg, str.substring(o - 1, o + query.length() + 19));
                    } else {
                        System.out.printf("%d: %s\n", o - fileBeg, str.substring(o - 1, fileEnd));
                    }
                }
            }
            fileBeg += lengths[i];
            System.out.println();
        }
    }

    // create and return a suffix array for a given string
    public static int[] getSuffixArray(String str){
        int[] sufAry = new int[str.length()];
        String[] ary = new String[str.length()];

        // put substrings into ary
        for(int i=0; i<str.length(); i++){
            ary[i] = str.substring(i);
        }

        // sort alphabetically
        Arrays.sort(ary);

        // determine sufAry
        for(int i=0; i<sufAry.length; i++){
            sufAry[i] = str.length() - ary[i].length() + 1;
        }

        return sufAry;
    }

    // return the indices of occurrences as an array
    public static int[] findOccurrences(String query, String str, int[] sufAry){
        ArrayList<Integer> ocrList = new ArrayList<Integer>();

        int min = 0, max = sufAry.length, idx = 0;
        String substr;

        // binary search
        while(min < max){
            idx = (min + max) / 2;

            substr = str.substring(sufAry[idx] - 1);

            // if found
            if(substr.startsWith(query)){
                break;
            }

            // if str is before substr
            if(query.compareTo(substr) < 0){
                max = idx;
            }
            // if str is after substr
            else if(query.compareTo(substr) > 0){
                min = idx + 1;
            }
        }

        // go back and find the first index where query can be found
        do{
            substr = str.substring(sufAry[idx] - 1);
            if(!substr.startsWith(query)){
                idx++;
                break;
            }
            idx--;
        } while(idx > 0);

        // now go ahead and put the indices of occurrence into ocrList
        do{
            substr = str.substring(sufAry[idx] - 1);
            if(!substr.startsWith(query)){
                break;
            }
            ocrList.add(sufAry[idx]);
            idx++;
        } while(idx < sufAry.length);

        // convert ArrayList<Integer> to int[]
        int[] ocrAry = new int[ocrList.size()];

        for(int i=0; i<ocrList.size(); i++){
            ocrAry[i] = ocrList.get(i);
        }

        return ocrAry;
    }
}