package xyz.nyc1;
import xyz.nyc1.backend.Server;

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
    public static void creatClientGUI() {
        // 创建主窗口
        JFrame serverFrame = new JFrame("云服务端");
        serverFrame.setSize(800, 600);
        serverFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        serverFrame.setLocationRelativeTo(null);
        serverFrame.setResizable(false);

        // 创建面板
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 2, 10, 10)); // 使用网格布局

        // 创建标签和文本框
        JLabel ipLabel = new JLabel("IP\u5730\u5740:");
        JTextField ipTextField = new JTextField();
        JLabel portLabel = new JLabel("端口:");
        JTextField portTextField = new JTextField();

        // 创建按钮
        JButton connectButton = new JButton("连接");
        JButton disconnectButton = new JButton("断开连接");

        // 添加组件到面板
        panel.add(ipLabel);
        panel.add(ipTextField);
        panel.add(portLabel);
        panel.add(portTextField);
        panel.add(connectButton);
        panel.add(disconnectButton);

        // 添加面板到窗口
        serverFrame.add(panel);

        // 添加按钮事件监听器
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String ip = ipTextField.getText();
                String port = portTextField.getText();
                // 这里可以添加连接服务器的代码
                JOptionPane.showMessageDialog(serverFrame, "连接到 " + ip + ":" + port);
            }
        });

        disconnectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 这里可以添加断开连接的代码
                JOptionPane.showMessageDialog(serverFrame, "断开连接");
            }
        });

        // 显示窗口
        serverFrame.setVisible(true);
    }
    public static void creatMainGUI(){
    // 创建主窗口
        JFrame mainFrame = new JFrame("云移");
        //mainFrame.setDefaultLookAndFeelDecorated(true);
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
                mainFrame.dispose();
                creatClientGUI();
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
