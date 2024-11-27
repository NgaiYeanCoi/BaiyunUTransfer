package xyz.nyc1;

import xyz.nyc1.backend.Callback;
import xyz.nyc1.backend.Request;
import xyz.nyc1.backend.TransferPoint;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

/**
 * @author NgaiYeanCoi,canyie,Aasling
 * */

public class ClientUI implements Callback {
    /**
     * 瀹㈡埛绔疷I鐣岄潰
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
        mainFrame = new JFrame("BaiyunUTransferClient");
        mainFrame.setSize(800, 600);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setResizable(false);
        mainFrame.getContentPane().setBackground(new Color(255, 255, 255)); // 设置窗口的背景颜色白色背景

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
        selectFileButton = new JButton("选择发送的文件");
        sendFileButton = new JButton("确认发送");

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

        // 初始为不可见
        sendFileButton.setVisible(false);
        selectFileButton.setVisible(false);
        disconnectButton.setVisible(false);

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
                // 判断ip以及port的合法性
                if((ipSegment1.getText().isEmpty()) || ipSegment2.getText().isEmpty()||ipSegment3.getText().isEmpty()||ipSegment4.getText().isEmpty()) {
                    new ErrorDialog(mainFrame, "IP地址不能为空！");
                }
                else if(port.isEmpty()){
                    new ErrorDialog(mainFrame,"端口不能为空");
                }
                else if( Integer.parseInt(port) < 1024)
                {
                    new ErrorDialog(mainFrame,"端口不得小于1024");
                }
                else {
                    // TODO:添加连接服务器的代码，需要连接后端API



                    logTextArea.append("正在连接到 " + ip + ":" + port + "中...\n");
                    logTextArea.append("已成功连接！\n");
                    connectButton.setVisible(false);
                    selectFileButton.setVisible(true);
                    disconnectButton.setVisible(true);


                }

            }
        });

        disconnectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String ip = ipSegment1.getText() + "." + ipSegment2.getText() + "." + ipSegment3.getText();
                String port = portTextField.getText();
                // TODO:断开连接，需要后端api
                logTextArea.append(ip + ":"+ port +" 已断开连接\n");
                disconnectButton.setVisible(false);
                selectFileButton.setVisible(false);
                sendFileButton.setVisible(false);
                connectButton.setVisible(true);
            }
        });

        selectFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                 // 创建模态化文件选择窗口
                JDialog selectFileFrame = new JDialog((JFrame) SwingUtilities.getWindowAncestor(mainFrame), "选择文件", true);
                selectFileFrame.setSize(400, 300);
                selectFileFrame.setLocationRelativeTo(null);
                selectFileFrame.setResizable(false);
                selectFileFrame.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                selectFileFrame.setLayout(new BorderLayout());
                // 创建文件拖放面板
                JPanel fileDropPanel = new JPanel();
                fileDropPanel.setBorder(BorderFactory.createTitledBorder("拖放文件到此处"));
                fileDropPanel.setPreferredSize(new Dimension(400, 200));
                fileDropPanel.setLayout(new BorderLayout());

            // 设置拖放目标
            new DropTarget(fileDropPanel, DnDConstants.ACTION_COPY_OR_MOVE, new DropTargetAdapter() {
                public void drop(DropTargetDropEvent evt) {
                    try {
                        evt.acceptDrop(DnDConstants.ACTION_COPY);
                        Transferable transferable = evt.getTransferable();
                        if (transferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                            List<File> files = (List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);
                            for (File file : files) {
                                logTextArea.append(file + "\n");
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            // 布局界面
            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout());
            panel.add(fileDropPanel, BorderLayout.CENTER);
            selectFileFrame.getContentPane().add(fileDropPanel, BorderLayout.CENTER);

            JButton innerSelectBtn = new JButton("选择文件");
            JButton InnerSelectConfirmBtn = new JButton("确定");
            JPanel bottomPanel = new JPanel();
            bottomPanel.setLayout(new FlowLayout());
            bottomPanel.add(innerSelectBtn);
            selectFileFrame.getContentPane().add(bottomPanel, BorderLayout.SOUTH);

            JFileChooser fileChooser = new JFileChooser();

            // 文件选择按钮事件
            innerSelectBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int result = fileChooser.showOpenDialog(selectFileFrame);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        String filePath = fileChooser.getSelectedFile().getAbsolutePath();
                        logTextArea.append("选择文件: " + filePath + "\n");
                        if (!filePath.isEmpty()) {
                            //TODO:
                            sendFileButton.setVisible(true);
                        }
                    }
                }
            });

    // 显示窗口
    selectFileFrame.setVisible(true);
            }
        });

        sendFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO:发送文件，需要后端api
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

    @Override
    public void onNewConnection(TransferPoint transferPoint, String address, Request request) {
        request.accept();
        request.decline();
    }

    @Override
    public void onLostConnection(TransferPoint transferPoint, String address) {

    }

    @Override
    public void onReceiveFile(TransferPoint transferPoint, String filename, Request request) {

    }

    @Override
    public void onTransferSuccess(TransferPoint transferPoint, File outputFile) {

    }

    @Override
    public void onTransferFailed(TransferPoint transferPoint, String filename) {

    }
}
