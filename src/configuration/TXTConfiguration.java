
package configuration;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class TXTConfiguration implements Configurable{
    private List<String> file_names_list;
    
    public TXTConfiguration(String configuration_path) {
        this.file_names_list = makeFileNamesList(configuration_path);
    }

    private TXTConfiguration(){}  
        
    private List< List<String> > createFeaturesList(String listPath){
        List< List<String> > unit = new ArrayList<>();
        List<String> element = new ArrayList<>();
        
        try ( BufferedReader fRead = new BufferedReader( 
                                        new InputStreamReader(
                                                new FileInputStream(listPath))); )
        {
            String line;
            do{
                line = fRead.readLine();

                if ( line!=null && line.length()!=0 ){
                    element.add(line);
                } else if( line==null || !element.isEmpty() && line.length()==0 ){
                    List<String> temporary = element;
                    unit.add(temporary);
                    element = new ArrayList<>();
                }
            } while( line != null);
           
        } catch(IOException e){System.out.println("IOException occured while createFeaturesList");}
        
        return unit;
    }
        
    private String nameMaker(List<String> list, String delimiter){
        String invalid = "[\\?\\\\\\\\|/\"\']";
        String result = "\\";
        for (String element : list) {
            if(element.matches(".*"+invalid+".*")){
                element = element.replaceAll(invalid, "");
                System.out.println("replaced");
            }
            result += element+delimiter;
        }
        return result.substring(0, result.length()-1);
    }
    
    private List<String> makeFileNamesList(String listPath){
        List< List<String> > listList = createFeaturesList(listPath);
        List<String> file_names_list = new ArrayList<>();
        
        for (List<String> element : listList) {
            file_names_list.add( nameMaker(element,"_") );
        }
        
        return file_names_list;
    }
    
    @Override
    public List<String> getFileNamesList(){
        return this.file_names_list;
    }
    
//    public static void main(String[] args) {
//        String listPath ="E:\\installation\\downloads_old\\Build Responsive Website Using HTML5, CSS3, JS And Bootstrap[UDEMY]\\list.txt";
//        ReadConfiguration rc = new ReadConfiguration(listPath);
//        System.out.println(rc.getFileNamesList().toString());
//        rc.getFileNamesList().stream().forEach( (String s) -> System.out.println(s));
//    }
}
