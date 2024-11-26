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
     * 客户端UI界面
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
    private JButton cancelSelectedButton;
    private String globalfilePath;

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
        selectFileButton = new JButton("选择发送文件");
        sendFileButton = new JButton("确认发送");
        cancelSelectedButton = new JButton("取消选择");

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
        bottomPanel.add(cancelSelectedButton);

        // 初始为不可见
        sendFileButton.setVisible(false);
        selectFileButton.setVisible(false);
        disconnectButton.setVisible(false);
        cancelSelectedButton.setVisible(false);

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
                cancelSelectedButton.setVisible(false);
                globalfilePath = null;
            }
        });

        selectFileButton.addActionListener(new ActionListener() {
            /**
            * 新增模态化，选择文件发送的窗口
             * */
            @Override
            public void actionPerformed(ActionEvent e) {
                 // 创建模态化文件选择窗口
                JDialog selectFileDialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(mainFrame), "选择文件", true);
                selectFileDialog.setSize(400, 300);
                selectFileDialog.setLocationRelativeTo(null);
                selectFileDialog.setResizable(false);
                selectFileDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                selectFileDialog.setLayout(new BorderLayout());
                // 创建文件拖放面板
                JPanel fileDropPanel = new JPanel();
                fileDropPanel.setBorder(BorderFactory.createTitledBorder("拖放文件到此处"));
                fileDropPanel.setPreferredSize(new Dimension(400, 200));
                fileDropPanel.setLayout(new BorderLayout());



            // 布局界面
            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout());
            panel.add(fileDropPanel, BorderLayout.CENTER);
            selectFileDialog.getContentPane().add(fileDropPanel, BorderLayout.CENTER);

             //按钮控件
            JButton innerSelectBtn = new JButton("选择文件");
            JPanel bottomPanel = new JPanel();
            bottomPanel.setLayout(new FlowLayout());
            bottomPanel.add(innerSelectBtn);
            selectFileDialog.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
            JFileChooser fileChooser = new JFileChooser();


                // 设置拖放目标
                new DropTarget(fileDropPanel, DnDConstants.ACTION_COPY_OR_MOVE, new DropTargetAdapter() {
                    public void drop(DropTargetDropEvent evt) {
                        try {
                            evt.acceptDrop(DnDConstants.ACTION_COPY);
                            Transferable transferable = evt.getTransferable();
                            if (transferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                                List<File> files = (List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);
                                if(files.size()==1){
                                    File file = files.get(0);
                                    globalfilePath = file.getAbsolutePath();
                                    logTextArea.append("已选择文件 "+globalfilePath + "\n");
                                    selectFileDialog.dispose();
                                    selectFileButton.setVisible(false);
                                    cancelSelectedButton.setVisible(true);
                                    sendFileButton.setVisible(true);

                                }
                                else{
                                    new ErrorDialog(selectFileDialog,"一次只能发送一个文件,请重新操作！");
                                    evt.rejectDrop();

                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

            // 内部视窗文件选择按钮呼出文件浏览器
            innerSelectBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int result = fileChooser.showOpenDialog(selectFileDialog);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        globalfilePath = fileChooser.getSelectedFile().getAbsolutePath();
                        logTextArea.append("已选择文件: " + globalfilePath + "\n");
                        if (!globalfilePath.isEmpty()) {
                            selectFileDialog.dispose();
                            selectFileButton.setVisible(false);
                            cancelSelectedButton.setVisible(true);
                            sendFileButton.setVisible(true);
                        }
                    }
                }
            });

    // 显示窗口
    selectFileDialog.setVisible(true);
            }
        });




        sendFileButton.addActionListener(new ActionListener() {
            /**
            *  发送文件按钮监听事件
            * */
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO:发送文件，需要后端api
                logTextArea.append("发送文件\n");
            }
        });


        cancelSelectedButton.addActionListener(new ActionListener() {
            /**
             *  "取消选择"按钮监听事件
             * */
            @Override
            public void actionPerformed(ActionEvent e) {
                logTextArea.append("已取消选取文件"+globalfilePath+"\n");
                globalfilePath = null;
                selectFileButton.setVisible(true);
                sendFileButton.setVisible(false);
                cancelSelectedButton.setVisible(false);



            }
        });

        // 显示窗口
        mainFrame.setVisible(true);
    }


    @Override
    public void onNewConnection(TransferPoint transferPoint, String address, Request request) {

    }

    @Override
    public void onLostConnection(TransferPoint transferPoint, String address) {

    }

    @Override
    public void onReceiveFile(TransferPoint transferPoint, String filename, Request request) {
         JDialog receiveFileRequest = new JDialog((JFrame) SwingUtilities.getWindowAncestor(mainFrame), "接收文件", true);
         receiveFileRequest.setSize(400, 300);
         receiveFileRequest.setLocationRelativeTo(null);
         receiveFileRequest.setResizable(false);
         receiveFileRequest.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);




    }

    @Override
    public void onTransferSuccess(TransferPoint transferPoint, File outputFile) {

    }

    @Override
    public void onTransferFailed(TransferPoint transferPoint, String filename) {

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
