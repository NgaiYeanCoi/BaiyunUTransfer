package xyz.nyc1;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author NgaiYeanCoi,canyie,Aasling
 * */


public class ClientUI {
    /**
     * 客户端端UI界面
     */

    private JFrame mainFrame;
    private JTextField ipSegment1;
    private JTextField ipSegment2;
    private JTextField ipSegment3;
    private JTextField ipSegment4;
    private JTextField portTextField;
    private JTextArea logTextArea;
    private JButton connectButton;
    private JButton disconnectButton;
    private JButton selectFileButton;
    private JButton sendFileButton;

    public ClientUI() {
        createUI();
    }

    private void createUI() {
        // 创建主窗口
        mainFrame = new JFrame("云移服务端");
        mainFrame.setSize(800, 600);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setResizable(false);

        // 创建面板
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // 创建顶部面板
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());

        // 创建IPV4控件
        JLabel ipLabel = new JLabel("IP地址:");
        ipSegment1 = new JTextField(3);
        ipSegment2 = new JTextField(3);
        ipSegment3 = new JTextField(3);
        ipSegment4 = new JTextField(3);

        // 传入applyIPSegmentPattern检查输入
        applyIPSegmentPattern(ipSegment1);
        applyIPSegmentPattern(ipSegment2);
        applyIPSegmentPattern(ipSegment3);
        applyIPSegmentPattern(ipSegment4);

        JLabel portLabel = new JLabel("端口:");
        portTextField = new JTextField(5);
        applyPortPattern(portTextField);

        // 创建按钮
        connectButton = new JButton("连接");
        disconnectButton = new JButton("断开连接");
        selectFileButton = new JButton("选择文件");
        sendFileButton = new JButton("发送文件");

        // 添加组件到顶部面板
        // IPv4输入面板
        topPanel.add(ipLabel);
        topPanel.add(ipSegment1);
        topPanel.add(new JLabel("."));
        topPanel.add(ipSegment2);
        topPanel.add(new JLabel("."));
        topPanel.add(ipSegment3);
        topPanel.add(new JLabel("."));
        topPanel.add(ipSegment4);
        // 端口输入面板
        topPanel.add(portLabel);
        topPanel.add(portTextField);
        // 按钮面板
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

        // 添加连接按钮事件监听器
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String ip = ipSegment1.getText() + "." + ipSegment2.getText() + "." + ipSegment3.getText() + "." + ipSegment4.getText();
                String port = portTextField.getText();


                if (port.isEmpty()){
                    new ErrorDialog(mainFrame,"端口不能为空");
                }
                else if( Integer.parseInt(port) < 1024)
                {
                    new ErrorDialog(mainFrame,"端口不得小于1024");
                }
                else {
                    // TODO:添加连接服务器的代码，需要连接后端API

                    logTextArea.append("正在连接到 " + ip + ":" + port + "中...\n");
                }


            }
        });

        disconnectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO:断开连接
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
                // TODO:发送文件
                logTextArea.append("发送文件\n");
            }
        });

        // 显示窗口
        mainFrame.setVisible(true);
    }


    private void applyPortPattern(JTextField textField) {
        /* *
         * 端口号输入正则表达匹配检查
         * */
        // 创建一个端口号的部分输入正则表达式
        String portPattern = "^\\d{1,5}$";

        // 创建一个DocumentFilter来过滤输入
        DocumentFilter filter = new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string == null) {
                    return;
                }
                String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
                String newText = currentText.substring(0, offset) + string + currentText.substring(offset);
                if (newText.matches(portPattern) || newText.isEmpty()) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text == null) {
                    return;
                }
                String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
                String newText = currentText.substring(0, offset) + text + currentText.substring(offset + length);
                if (newText.matches(portPattern) || newText.isEmpty()) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        };

        // 获取textField的Document并设置DocumentFilter
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(filter);
    }
    private void applyIPSegmentPattern(JTextField textField) {
        /* *
         * IPv4段的正则表达匹配
         * */
        String ipSegmentPattern = "^(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$";

        // 创建一个DocumentFilter来过滤输入
        DocumentFilter filter = new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string == null) {
                    return;
                }
                String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
                String newText = currentText.substring(0, offset) + string + currentText.substring(offset);
                if (newText.matches(ipSegmentPattern) || newText.isEmpty()) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text == null) {
                    return;
                }
                String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
                String newText = currentText.substring(0, offset) + text + currentText.substring(offset + length);
                if (newText.matches(ipSegmentPattern) || newText.isEmpty()) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        };

        // 获取textField的Document并设置DocumentFilter
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(filter);
    }
}
