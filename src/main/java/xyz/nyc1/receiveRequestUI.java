package xyz.nyc1;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class receiveRequestUI {
    static JFrame mainFrame = new JFrame("test");


    public static void DemoTest() {

        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 设置窗口关闭操作
        mainFrame.setSize(500, 400);
        mainFrame.setResizable(false); // 禁止窗口调整大小
        mainFrame.setLocationRelativeTo(null);
        mainFrame.getContentPane().setBackground(new Color(255, 255, 255)); // 设置窗口的背景颜色为白色

        // 创建模态对话框
        JDialog receiveFileRequestDialog = new JDialog(mainFrame, "接收文件", true); // 设置为模态
        receiveFileRequestDialog.setSize(900, 600);
        receiveFileRequestDialog.setLocationRelativeTo(null); // 居中显示
        receiveFileRequestDialog.setResizable(false); // 禁止调整大小
        receiveFileRequestDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE); // 设置关闭操作
        receiveFileRequestDialog.setLayout(new BorderLayout()); // 设置布局
        receiveFileRequestDialog.setBackground(new Color(255, 255, 255));

        // 创建主Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(255, 255, 255));
        mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.Y_AXIS));

        // center面板
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(new Color(255, 255, 255));
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS)); // 设置为垂直布局

        // 添加电脑图片
        centerPanel.add(Box.createVerticalStrut(90)); //设置垂直间距
        Image computerImage = new ImageIcon(Objects.requireNonNull(receiveRequestUI.class.getResource("/images/computerIcon.png"))).getImage();//获取图片
        ImageIcon computerIcon = new ImageIcon(ImageReset.resizeImage(computerImage, 133, 93));//重定图片大小
        JLabel imageLabel = new JLabel(computerIcon);
        imageLabel.setSize(computerIcon.getIconWidth(), computerIcon.getIconHeight()); //设置标签的长宽为Icon的长宽
        imageLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // 使图片居中
        centerPanel.add(imageLabel); //添加imageLabel进Panel
        centerPanel.add(Box.createVerticalStrut(30)); //设置垂直间距

        // 添加IP地址标签
        JLabel receiveIpInfoLabel = new JLabel();
        receiveIpInfoLabel.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 25));
        //TODO:需接后端API获取到远端的IP地址
        String ipAddressInfo = "127.0.0.1";
        receiveIpInfoLabel.setText(ipAddressInfo);
        receiveIpInfoLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // 使标签居中
        centerPanel.add(receiveIpInfoLabel);
        centerPanel.add(Box.createVerticalStrut(30)); //设置垂直间距

        // 添加消息标签
        JLabel receiveMessageInfoLabel = new JLabel("想要发送给你一个文件");
        receiveMessageInfoLabel.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 30));
        receiveMessageInfoLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // 使标签居中
        centerPanel.add(receiveMessageInfoLabel);
        //设置centerPanel与bottomPanel的垂直间距
        centerPanel.add(Box.createVerticalStrut(130));

        // 接受按钮控件
        JButton receiveAcceptBtn = new JButton();
        Image receiveAcceptBtnImage = new ImageIcon(Objects.requireNonNull(receiveRequestUI.class.getResource("/images/acceptBtnIcon.png"))).getImage();
        setBtnImage(receiveAcceptBtn, receiveAcceptBtnImage,89,40);



        // 拒绝按钮控件
        JButton receiveRejectBtn = new JButton();
        Image receiveRejectBtnImage = new ImageIcon(Objects.requireNonNull(receiveRequestUI.class.getResource("/images/rejectBtnIcon.png"))).getImage();
        setBtnImage(receiveRejectBtn, receiveRejectBtnImage,89,40);

        // 底部面板
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout());
        bottomPanel.add(receiveRejectBtn);
        bottomPanel.add(receiveAcceptBtn);
        bottomPanel.setBackground(new Color(255, 255, 255));
        //bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));// 移动bottomPanel

        // 添加进主面板
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        receiveFileRequestDialog.add(mainPanel);

        receiveAcceptBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO:接收文件同意，需接后端API
                System.out.println("accept");
                //receiveFileRequestDialog.dispose();
            }
        });
        receiveRejectBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO:接收文件拒绝，需接后端API
                System.out.println("decline");
                //receiveFileRequestDialog.dispose();
            }
        });


        // 显示主框架
        mainFrame.setVisible(true);

        // 显示对话框
        receiveFileRequestDialog.setVisible(true);

    }

    private static void setBtnImage(JButton Btn, Image BtnImage,int targetWidth, int targetHeight) {
        //图片按钮样式
        ImageIcon Icon = new ImageIcon(ImageReset.resizeImage(BtnImage, targetWidth, targetHeight));
        ImageIcon receiveBtnRolloverIcon = ImageReset.createRolloverIcon(Icon,0.7f);
        Btn.setIcon(Icon);
        Btn.setBorderPainted(false);
        Btn.setContentAreaFilled(false);
        Btn.setFocusPainted(false);
        Btn.setRolloverIcon(receiveBtnRolloverIcon);
    }
}
