package xyz.nyc1;


import javax.swing.*;
import java.awt.*;

public class Demo {
    static JFrame mainFrame = new JFrame("test");
    private  JButton receiveAcceptBtn = new JButton("接受");
    private JButton receiveDeclineBtn = new JButton("拒绝");

    public static void DemoTest() {

        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 设置窗口关闭操作
        mainFrame.setSize(400, 300);
        mainFrame.setResizable(false); // 禁止窗口调整大小
        mainFrame.setLocationRelativeTo(null);
        mainFrame.getContentPane().setBackground(new Color(255, 255, 255)); // 设置窗口的背景颜色为白色



        //

        // 创建模态对话框
        JDialog receiveFileRequestDialog = new JDialog(mainFrame, "接收文件", true); // 设置为模态
        receiveFileRequestDialog.setSize(600, 400);
        receiveFileRequestDialog.setLocationRelativeTo(null); // 居中显示
        receiveFileRequestDialog.setResizable(false); // 禁止调整大小
        receiveFileRequestDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE); // 设置关闭操作
        receiveFileRequestDialog.setLayout(new BorderLayout()); // 设置布局
        receiveFileRequestDialog.setBackground(new Color(255, 255, 255));

        String ipPort="127.0.0.1:10086";
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(new Color(255, 255, 255));
        centerPanel.add(receiveFileRequestDialog);
        centerPanel.setLayout(new BorderLayout());
        JLabel centerInfo = new JLabel();
        centerInfo.setText(ipPort+"\n想要给你给你发送一个文件");
        centerPanel.add(centerInfo, BorderLayout.CENTER);


        //按钮控件
            JButton receiveAcceptBtn = new JButton("接收");
            JButton receiveDeclineBtn = new JButton("拒绝");
            JPanel bottomPanel = new JPanel();
            bottomPanel.setLayout(new FlowLayout());
            bottomPanel.add(receiveAcceptBtn);
            bottomPanel.add(receiveDeclineBtn);
            receiveFileRequestDialog.getContentPane().add(bottomPanel, BorderLayout.SOUTH);


        // 显示主框架
        mainFrame.setVisible(true);

        // 显示对话框
        receiveFileRequestDialog.setVisible(true);


    }
}
