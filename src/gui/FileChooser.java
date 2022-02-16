
package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import static gui.AutoRenamer.*;

//Reading is more correct when files was formated like /\w+\\.w+/
public class FileChooser extends JFrame{
    private  JButton  btnConfigFile   = null;
    private  JButton  btnOpenDir      = null;
    private  JButton  btnRun          = null;
    private  JFileChooser fileChooser = null;
    private final static FileChooser SINGLE = new FileChooser();
    
    private  FileChooser() {
        super("Пример FileChooser");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Кнопка создания диалогового окна для выбора директории
        btnOpenDir = new JButton("Открыть директорию");
        // Кнопка создания диалогового окна для сохранения файла
        btnConfigFile = new JButton("Открыть файл конфигурации");
        // Кнопка для переименования файла
        btnRun = new JButton("Переименовать");

        // Создание экземпляра JFileChooser 
        fileChooser = new JFileChooser();
        
        // Подключение слушателей к кнопкам
        btnOpenDir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fileChooser.setDialogTitle("Выбор директории");
                // Определение режима - только каталог
                
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int result = fileChooser.showOpenDialog(FileChooser.this);
                // Если директория выбрана, покажем ее в сообщении
                if (result == JFileChooser.APPROVE_OPTION ){
                    setRootPath( fileChooser.getSelectedFile().toString() );
                    System.out.println( getRootPath() ); 
                }
            }
        });
        
        btnConfigFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fileChooser.setDialogTitle("Выбор файла конфигурации");
                // Определение режима - только каталог
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                int result = fileChooser.showOpenDialog(FileChooser.this);
                // Если директория выбрана, покажем ее в сообщении
                if (result == JFileChooser.APPROVE_OPTION ){
                    setConfPath( fileChooser.getSelectedFile().toString() );
                    System.out.println( getConfPath() );
                }
            }
        });
        
        btnRun.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(getConfPath()!=null&&getRootPath()!=null)
                    gui.AutoRenamer.htmlRenamer();
                JOptionPane.showMessageDialog(FileChooser.this, "Renamed");
                System.exit(0);
            }
        });

        // Размещение кнопок в интерфейсе
        JPanel contents = new JPanel();
        contents.add( btnOpenDir   );
        contents.add( btnConfigFile );
        contents.add( btnRun );

        setContentPane(contents);
        // Вывод окна на экран
        setSize(300, 140);
        setVisible(true);
    }
      
    public static FileChooser getInstance(){
        return SINGLE;
    }
    
    public static void main(String[] args){
        FileChooser.getInstance();
    }
}