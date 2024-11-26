package xyz.nyc1;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * @author NgaiYeanCoi,canyie,Aasling
 * */



public class GUI extends JFrame {
    public static void creatGUI(){
    // 创建主窗口
        JFrame mainFrame = new JFrame("云移");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 设置窗口关闭操作
        mainFrame.setSize(400, 300);
        mainFrame.setResizable(false); // 禁止窗口调整大小
        mainFrame.getContentPane().setBackground(new Color(240, 240, 240)); // 设置窗口的背景颜色浅灰色背景
        mainFrame.addComponentListener(new ComponentAdapter() { // 使窗口始终保持居中
            @Override
            public void componentResized(ComponentEvent e) {
                mainFrame.setLocationRelativeTo(null);
            }
        });

        //-- 设置mainFrame容器
        var mainFrameContentPane = mainFrame.getContentPane();
            mainFrameContentPane.setLayout(null);   // 使用绝对布局


        mainFrame.setBackground(Color.white);

        // 创建标签
        JLabel label1 = new JLabel("\u8bf7\u9009\u62e9\u4f60\u7684\u6a21\u5f0f");
        label1.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 20));
        mainFrame.getContentPane().add(label1);
        label1.setBounds(125, 30, 150, 40);




        //---- serverBtn ----
        JButton serverBtn = new JButton("\u670d\u52a1\u7aef");
        mainFrameContentPane.add(serverBtn);
        serverBtn.setBounds(new Rectangle(new Point(155, 100), serverBtn.getPreferredSize()));
        //serverBtn点击事件
        serverBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //
                System.out.println("server");
            }
        });


        //---- clientBtn ----
        JButton clientBtn = new JButton("\u5ba2\u6237\u7aef");
        mainFrameContentPane.add(clientBtn);
        clientBtn.setBounds(new Rectangle(new Point(155, 155), clientBtn.getPreferredSize()));
        //clientBtn点击事件
        clientBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //
                System.out.println("client1");
            }
        });




        // 设置布局管理器为null，以便使用绝对定位
        mainFrame.setLayout(null);

        // 显示窗口
        mainFrame.setVisible(true); // 使窗口可见
        mainFrame.setLocationRelativeTo(null);
}

}
