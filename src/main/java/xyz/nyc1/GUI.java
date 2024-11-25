package xyz.nyc1;
import javax.swing.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;

/**
 * @author NgaiYeanCoi,canyie,Aasling
 * */

public class GUI extends JFrame {
    public static void creatGUI(){

    JFrame.setDefaultLookAndFeelDecorated(true);

    //创建主窗口
    JFrame mainFrame = new JFrame("云移");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //设置窗口关闭操作
        mainFrame.setSize(800, 600);


    JLabel test = new JLabel("test and hello");
        mainFrame.getContentPane().add(test);
        mainFrame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                mainFrame.setLocationRelativeTo(null);
            }
        });

    //显示窗口
        mainFrame.setVisible(true); //使窗口可见
        mainFrame.setLocationRelativeTo(null);

}
}
