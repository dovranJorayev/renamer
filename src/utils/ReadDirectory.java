
package utils;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class ReadDirectory {
    private Supplier<Stream<Path>> sortedDirectory;
    private String root;
    private ReadDirectory(){}
    
    public ReadDirectory(String root){
        this.root=root;
        List<Path> list = this.readFiles(root);
        this.sortedDirectory = this.sortPaths( list );
    }
    
    private List<Path> readFiles(String root) {
        Path directory = Paths.get(root);
        List<Path> pathsList = new ArrayList<>();
        
        try(DirectoryStream<Path> directoryStream = Files.newDirectoryStream(directory)){
            for (Path entry : directoryStream) {
                 BasicFileAttributes atr = Files.readAttributes(entry,BasicFileAttributes.class);
            
                if( !atr.isDirectory() )
                    pathsList.add(entry);
            }

        }catch (InvalidPathException|NotDirectoryException ex) {
            System.out.println("Invalid path or file is not directory");
        }catch(IOException e){
            System.out.println("I/O exception found");
        }
        return pathsList;
    }
    
    private Supplier<Stream<Path>> sortPaths(List<Path> list){
        
        class NumericalSorter implements Comparator<Path>{
            @Override
            public int compare(Path p1, Path p2) {
                String op1 = p1.getFileName().toString().toUpperCase();
                String op2 = p2.getFileName().toString().toUpperCase();
                              
                
                if( isSortable(op1, op2) ){
                    Integer n1 = Integer.valueOf( op1.replaceAll("\\.\\w{2,3}$", "").replaceAll("[^0-9]", "") );
                    Integer n2 = Integer.valueOf( op2.replaceAll("\\.\\w{2,3}$", "").replaceAll("[^0-9]", "") );
                    return n1.compareTo(n2); 
                }
                return op1.compareTo(op2);
            }
            
            public boolean isSortable(String s1, String s2){
//                String regex = ".+\\([0-9]+\\)\\.\\w+";
                String regex = ".+\\.\\w+";

                return ( s1.matches(regex) && s2.matches(regex) );
            }
        }
        return () -> list.stream().sorted( new NumericalSorter() );
    }
    
    public Supplier<Stream <Path>> getSortedDirectory(){
        return this.sortedDirectory;
    }
    
    public String getRoot(){
        return this.root;
    }
    
    public static void main(String[] args) {
        ReadDirectory rd = new ReadDirectory("C:\\Users\\Dovran\\Downloads\\COURSES\\Основы TypeScript (Todd Motto)");
        rd.getSortedDirectory().get().forEachOrdered((Path p) -> System.out.println(p.toString()));
    }
}
