
package configuration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONObject;

public class HTMLConfiguration implements Configurable {
    private static final List<String> META = new ArrayList<>();
    private static final List<String> LIST = new ArrayList<>();
    private final String htmlPath;
    
    public HTMLConfiguration(String path){
        this.htmlPath = path;
    }
    
    private static String read(String path){
        String result = "";
        
        try(BufferedReader rd = new BufferedReader( new FileReader(path))){
            String el;
            while( (el=rd.readLine())!=null){
                result+="\r\n"+el;
            } 
        }catch(IOException e){System.out.println("I/O error");}
            
        return result;
    }
    
    private static void getMeta(String html){
//        Pattern pattern = Pattern.compile("\\{\"title\": \".+\"\\},");
        Pattern pattern = Pattern.compile("<script type=\\\"application/ld\\+json\\\">(.*)</script>");
        Matcher matcher = pattern.matcher(html);
        
        while(matcher.find()){
            String elem = html.substring(matcher.start(), matcher.end()-1);
            
            META.add(elem);
        }
        System.out.println("----Metadata-----");
        System.out.println(META);
        System.out.println("----Metadata-----");
    }
    private static void getFileNames(){
        Consumer<String> action = (String el) -> {
            Pattern pattern = Pattern.compile("\\{.*\\}");
            Matcher matcher = pattern.matcher(el);
            if(matcher.find()){
                String jsonStr = el.substring(matcher.start(), matcher.end());
                JSONObject jsObject = new JSONObject(jsonStr);
                JSONArray jsArray = (JSONArray)jsObject.get("@graph");
                for (Iterator<Object> it = jsArray.iterator(); it.hasNext();) {
                    JSONObject js = (JSONObject) it.next();
                    LIST.add( js.get("name").toString().replaceAll("\\\\|\\/|\\:|\\*|\\?|<|>|\\|", "") );
                }
                System.out.println(jsArray);
            }
        };
                
        META.stream().forEachOrdered(action);
        
        System.out.println("----Listdata-----");
        System.out.println(LIST);
        System.out.println("----Listdata-----");
    }
    
    @Override
    public List<String> getFileNamesList(){
        String html = read(htmlPath);
        
        getMeta(html);
        
        getFileNames();
        
        return LIST;
    }
    
    
    public static void main(String[] args) {
        String path = "C:\\Users\\Dovran\\Downloads\\COURSES\\Основы TypeScript (Todd Motto) - Видеоуроки.html";
        
        String html = read(path);
        System.out.println(html);
        
        getMeta(html);
        System.out.println(META.toString());
        
        getFileNames();
        System.out.println(LIST.toString());
        System.out.println(LIST.size());        
    }
}
