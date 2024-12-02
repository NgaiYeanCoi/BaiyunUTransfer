package xyz.nyc1;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

import static xyz.nyc1.SetBtnImage.setBtnImage;

import xyz.nyc1.backend.Request;

/**
 * @author NgaiYeanCoi
 * */

public class ReceiveRequestUI {
    private JDialog receiveFileRequestDialog;
    private JButton progressBtn;
    private JProgressBar progressBar;
    private JDialog progressDialog;

    public void show(JFrame mainFrame,String filename, String address, Request request) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        //mainFrame = new JFrame("接收请求");
        //mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 设置窗口关闭操作
        //mainFrame.setSize(500, 400);
        //mainFrame.setResizable(false); // 禁止窗口调整大小
        //mainFrame.setLocationRelativeTo(null);
        //mainFrame.getContentPane().setBackground(new Color(255, 255, 255)); // 设置窗口的背景颜色为白色

        String lookAndFeel = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
        UIManager.setLookAndFeel(lookAndFeel);

        // 创建模态对话框
        receiveFileRequestDialog = new JDialog(mainFrame, "接收文件", true); // 设置为模态
        receiveFileRequestDialog.setSize(900, 600);
        receiveFileRequestDialog.setLocationRelativeTo(null); // 居中显示
        receiveFileRequestDialog.setResizable(false); // 禁止调整大小
        receiveFileRequestDialog.isAlwaysOnTop();
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
        Image computerImage = new ImageIcon(Objects.requireNonNull(ReceiveRequestUI.class.getResource("/images/computerIcon.png"))).getImage();//获取图片
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
        receiveIpInfoLabel.setText(address);
        receiveIpInfoLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // 使标签居中
        centerPanel.add(receiveIpInfoLabel);
        centerPanel.add(Box.createVerticalStrut(30)); //设置垂直间距

        // 添加消息标签
        JLabel receiveMessageInfoLabel = new JLabel("想要发送给你一个文件 " + filename);
        receiveMessageInfoLabel.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 30));
        receiveMessageInfoLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // 使标签居中
        centerPanel.add(receiveMessageInfoLabel);
        //设置centerPanel与bottomPanel的垂直间距
        centerPanel.add(Box.createVerticalStrut(130));

        // 接受按钮控件
        JButton receiveAcceptBtn = new JButton();
        Image receiveAcceptBtnImage = new ImageIcon(Objects.requireNonNull(ReceiveRequestUI.class.getResource("/images/acceptBtnIcon.png"))).getImage();
        setBtnImage(receiveAcceptBtn, receiveAcceptBtnImage,89,40);



        // 拒绝按钮控件
        JButton receiveRejectBtn = new JButton();
        Image receiveRejectBtnImage = new ImageIcon(Objects.requireNonNull(ReceiveRequestUI.class.getResource("/images/rejectBtnIcon.png"))).getImage();
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

        receiveAcceptBtn.addActionListener(e -> {
            System.out.println("accept");
            request.accept();
            creatProgressBar(mainFrame);
            receiveFileRequestDialog.dispose();
        });
        receiveRejectBtn.addActionListener(e -> {
            System.out.println("decline");
            int result = JOptionPane.showConfirmDialog(receiveFileRequestDialog,"你确定要取消接收吗？","提示",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
            if (result == JOptionPane.YES_OPTION){
                System.out.println("yes");
                request.decline();
                receiveFileRequestDialog.dispose();
            }
            else if(result == JOptionPane.NO_OPTION||result == JOptionPane.CLOSED_OPTION){
                System.out.println("cancel");

            }
            //receiveFileRequestDialog.dispose();
        });


        // 显示主框架
        mainFrame.setVisible(true);

        // 显示对话框
        receiveFileRequestDialog.setVisible(true);

    }
    private void creatProgressBar(Component parentComponent){
        progressDialog = new JDialog((Frame) parentComponent, "接收文件", true); // 设置为模态
        progressDialog.setSize(300, 100);
        progressDialog.setLocationRelativeTo(null); // 居中显示
        progressDialog.setResizable(false); // 禁止调整大小
        progressDialog.setAlwaysOnTop(true);
        progressDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());
        progressBar = new JProgressBar();// 创建进度条对象
        progressBar.setStringPainted(true);// 设置显示提示信息
        progressBar.setIndeterminate(true);// 设置采用不确定进度条
        progressBar.setString("正在传输中...");// 设置提示信息
        progressBtn = new JButton("取消");

        //TODO:当文件传输完之后需要接回调弹出窗口表示传输成功然后关闭该界面所有窗口

        // 将进度条和取消按钮添加到面板
        panel.add(progressBar, BorderLayout.CENTER);
        panel.add(progressBtn, BorderLayout.SOUTH);
        // 设置取消按钮的点击事件
        progressBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(progressDialog,"你确定要取消接收吗？","提示",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                if (result == JOptionPane.YES_OPTION){
                    System.out.println("yes");
                    progressDialog.dispose();
                    receiveFileRequestDialog.dispose();
                    //TODO：需接后端API

                }
                else if(result == JOptionPane.NO_OPTION||result == JOptionPane.CLOSED_OPTION){
                    System.out.println("cancel");
                }
            }
        });
        // 将面板添加到对话框
        progressDialog.add(panel);
        progressDialog.setVisible(true);
    }

    public void onTransferSuccess(File outputFile) {
        // TODO Implement
        progressBtn.setText("确定");
        progressBar.setString("完成");
        // 移除现有的监听器
        ActionListener[] listeners = progressBtn.getActionListeners();
        for (ActionListener listener : listeners) {
            progressBtn.removeActionListener(listener);
        }
        // 监听新的事件
        progressBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Desktop desktop = Desktop.getDesktop();
                    if (desktop.isSupported(Desktop.Action.OPEN)) {
                        desktop.open(outputFile.getParentFile());
                        progressDialog.dispose();
                        receiveFileRequestDialog.dispose();
                    } else {
                        System.out.println("打开文件夹的功能不被支持");
                    }
                } catch (IOException m) {
                    throw new RuntimeException(m);
                }
            }
        });

    }

        public void onTransferFailed(JFrame mainFrame) {
        // TODO Implement
            new ErrorDialog(mainFrame,"文件传输失败！");
            progressDialog.dispose();
            receiveFileRequestDialog.dispose();
    }

}
