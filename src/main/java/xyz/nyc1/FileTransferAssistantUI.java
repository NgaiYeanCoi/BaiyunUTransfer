package xyz.nyc1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FileTransferAssistantUI {

    private JFrame mainFrame;
    private JTextField ipTextField;
    private JTextField portTextField;
    private JTextArea logTextArea;
    private JButton connectButton;
    private JButton disconnectButton;
    private JButton selectFileButton;
    private JButton sendFileButton;

    public FileTransferAssistantUI() {
        createUI();
    }

    private void createUI() {
        // 创建主窗口
        mainFrame = new JFrame("文件传输助手");
        mainFrame.setSize(800, 600);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setResizable(false);

        // 创建面板
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // 创建顶部面板
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(3, 2, 10, 10));

        // 创建标签和文本框
        JLabel ipLabel = new JLabel("IP地址:");
        ipTextField = new JTextField();
        JLabel portLabel = new JLabel("端口:");
        portTextField = new JTextField();

        // 创建按钮
        connectButton = new JButton("连接");
        disconnectButton = new JButton("断开连接");
        selectFileButton = new JButton("选择文件");
        sendFileButton = new JButton("发送文件");

        // 添加组件到顶部面板
        topPanel.add(ipLabel);
        topPanel.add(ipTextField);
        topPanel.add(portLabel);
        topPanel.add(portTextField);
        topPanel.add(connectButton);
        topPanel.add(disconnectButton);

        // 创建日志文本区域
        logTextArea = new JTextArea();
        logTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logTextArea);

        // 创建底部面板
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout());
        bottomPanel.add(selectFileButton);
        bottomPanel.add(sendFileButton);

        // 添加面板到主面板
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        // 添加主面板到窗口
        mainFrame.add(panel);

        // 添加按钮事件监听器
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String ip = ipTextField.getText();
                String port = portTextField.getText();
                // 这里可以添加连接服务器的代码
                logTextArea.append("连接到 " + ip + ":" + port + "\n");
            }
        });

        disconnectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 这里可以添加断开连接的代码
                logTextArea.append("断开连接\n");
            }
        });

        selectFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int result = fileChooser.showOpenDialog(mainFrame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                    logTextArea.append("选择文件: " + filePath + "\n");
                }
            }
        });

        sendFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 这里可以添加发送文件的代码
                logTextArea.append("发送文件\n");
            }
        });

        // 显示窗口
        mainFrame.setVisible(true);
    }

}
