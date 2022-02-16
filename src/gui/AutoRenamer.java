package gui;

import configuration.Configurable;
import configuration.TXTConfiguration;
import utils.ReadDirectory;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import configuration.HTMLConfiguration;


public class AutoRenamer {
private static String rootPath=null;
private static String confPath=null;

    public static void setRootPath(String root){
        rootPath=root;
    }
    
    public static void setConfPath(String list){
        confPath=list;
    }
    
    public static String getRootPath(){
        return rootPath;
    }
    
    public static String getConfPath(){
        return confPath;
    }
    
    static class Increment{
        private static int i=0;

        public static void increace(){
            ++i;
        }

        public static int getIncrement(){
            return i;
        }
    }
   
    static boolean validate(ReadDirectory dir, Configurable conf){
        long dirSize = dir.getSortedDirectory().get().count();
        long confSize = conf.getFileNamesList().size();
        System.out.println("configuration size" + confSize + "directory size" + dirSize);
        return dirSize==confSize;
    }
    
    private static void rename(Configurable conf, ReadDirectory dir){
        List<String> fileNames = conf.getFileNamesList();
        Supplier<Stream<Path>> filesStreamSupplier = dir.getSortedDirectory();
        
        Consumer<Path> renaming = new Consumer<Path>() {
            @Override
            public void accept(Path source) {
                try {
                    String format = sourceFormat(source);
                    int increment = Increment.getIncrement();
                    Path target = Paths.get( dir.getRoot() + "/" + (increment+1) + ". " + fileNames.get(increment) + format );
                    Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
                    Increment.increace();
                } catch (IOException e){System.out.println("IOException occurs while moving files");}
            }
            
            private String sourceFormat(Path source){
                String fileName = source.getFileName().toString();
                Pattern formatPattern = Pattern.compile("\\.\\w{2,5}$");
                Matcher matcher = formatPattern.matcher(fileName);
                
                return matcher.find() ? fileName.substring(matcher.start(), matcher.end()) : "undefined";
            }
        };
        
        filesStreamSupplier.get().forEachOrdered(renaming);
    }
    
    public static void txtRenamer(){
        String root = "C:\\Users\\sony.energizer\\Downloads\\Build Responsive Website Using HTML5, CSS3, JS And Bootstrap[UDEMY]";
        String listPath ="C:\\Users\\sony.energizer\\Downloads\\Build Responsive Website Using HTML5, CSS3, JS And Bootstrap[UDEMY]\\list.txt"; 
        
        TXTConfiguration configuration = new TXTConfiguration(listPath);
        ReadDirectory directory = new ReadDirectory(root);
        
        if( validate(directory, configuration))
            rename(configuration, directory);
        else
            System.out.println("Invalid to Rename");
    }
    
    public static void htmlRenamer(){
        HTMLConfiguration configuration = new HTMLConfiguration(confPath);
        ReadDirectory directory = new ReadDirectory(rootPath);

        if( validate(directory, configuration))
            rename(configuration, directory);
        else
            System.out.println("Invalid to Rename");
    }
  
}
