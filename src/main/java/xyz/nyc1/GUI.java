package xyz.nyc1;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * @author NgaiYeanCoi
 * 暂时弃用，做保留
 * */



public class GUI extends JFrame {
    public static void creatMainGUI(){
        // 创建主窗口
        JFrame mainFrame = new JFrame("BaiyunUTransfer");
        //mainFrame.setDefaultLookAndFeelDecorated(true);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 设置窗口关闭操作
        mainFrame.setSize(400, 300);
        mainFrame.setResizable(false); // 禁止窗口调整大小
        mainFrame.getContentPane().setBackground(new Color(255, 255, 255)); // 设置窗口的背景颜色白色色背景
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
        JLabel label1 = new JLabel("请选择你的模式");
        label1.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 20));
        mainFrame.getContentPane().add(label1);
        label1.setBounds(125, 30, 150, 40);




        //---- serverBtn ----
        JButton serverBtn = new JButton("我是第一台设备");
        mainFrameContentPane.add(serverBtn);
        serverBtn.setBounds(new Rectangle(new Point(135, 100), serverBtn.getPreferredSize()));
        //serverBtn点击事件
        serverBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //

                System.out.println("server");
            }
        });


        //---- clientBtn ----
        JButton clientBtn = new JButton("我是第二台设备");
        mainFrameContentPane.add(clientBtn);
        clientBtn.setBounds(new Rectangle(new Point(135, 155), clientBtn.getPreferredSize()));
        //clientBtn点击事件
        clientBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //
                mainFrame.dispose();
                new ClientUI();
                System.out.println("client");
            }
        });


        // 设置布局管理器为null，以便使用绝对定位
        mainFrame.setLayout(null);

        // 显示窗口
        mainFrame.setVisible(true); // 使窗口可见
        mainFrame.setLocationRelativeTo(null);


}

}
