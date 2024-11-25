package xyz.nyc1;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * @author NgaiYeanCoi,canyie,Aasling
 * */

public class GUI extends JFrame {
    public static void creatGUI(){


    System.setProperty("file.encoding", "UTF-8");

    //创建主窗口
    JFrame mainFrame = new JFrame("云移");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //设置窗口关闭操作
        mainFrame.setSize(800, 600);
        mainFrame.addComponentListener(new ComponentAdapter() { //使窗口始终保持居中
            @Override
            public void componentResized(ComponentEvent e) {
                mainFrame.setLocationRelativeTo(null);
            }
        });
        mainFrame.setBackground(Color.white);

    JLabel test = new JLabel("test and hello");
    mainFrame.getContentPane().add(test);

    JButton serverBtn = new JButton("服务端");
    JButton clientBtn = new JButton("客户端");
    serverBtn.setBounds(10,10,150,50);
    clientBtn.setBounds(150,10,150,50);
    serverBtn.setContentAreaFilled(false);
    clientBtn.setContentAreaFilled(false);
    serverBtn.setBorder(null);
    mainFrame.getContentPane().add(serverBtn);
    mainFrame.getContentPane().add(clientBtn);
    mainFrame.setLayout(null);

    //显示窗口
        mainFrame.setVisible(true); //使窗口可见
        mainFrame.setLocationRelativeTo(null);
}
}
