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
 * @author NgaiYeanCoi 重构成mainUI，弃用但保留
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
    private String globalFilePath;
    private CardLayout cardLayout;
    private JPanel rightPanel;

    public ClientUI() {
        createUI();
    }


    private void createUI() {
        // 创建主窗口
        mainFrame = new JFrame("BaiyunUTransferClient");
        mainFrame.setSize(900, 600);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setResizable(false);
        mainFrame.getContentPane().setBackground(new Color(255, 255, 255)); // 设置窗口的背景颜色白色背景

        // 创建主面板
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        /*创建右侧面板并使用 CardLayout*/
        rightPanel = new JPanel(new CardLayout());
        rightPanel.setPreferredSize(new Dimension(640, 0)); // 设置右侧栏的宽度
        cardLayout = (CardLayout) rightPanel.getLayout();

        /*创建左侧栏面板*/
            JPanel leftPanel = new JPanel();
            leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS)); // 设置为垂直布局
            leftPanel.setPreferredSize(new Dimension(260, 0)); // 设置左侧栏的宽度
            leftPanel.add(Box.createVerticalStrut(10));
            //创建logoLabel控件
            JLabel leftLogo = new JLabel("BaiyunUTransfer");
            leftLogo.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 25));
            leftLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
            leftPanel.add(leftLogo);
            leftPanel.add(Box.createVerticalStrut(40)); //设置垂直间距
        //创建发送按钮控件
        JButton leftSendBtn = new JButton("发送");
        leftSendBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(leftSendBtn);
        leftPanel.add(Box.createVerticalStrut(45));
        //发送监听事件
        leftSendBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO:待补充UI代码
                System.out.println("发送");
                cardLayout.show(rightPanel, "send");
                //刷新UI
                mainFrame.revalidate();
                mainFrame.repaint();
            }
        });
            //创建接收按钮组件
            JButton leftReceiveBtn = new JButton("接收");
            leftReceiveBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
            leftPanel.add(leftReceiveBtn);
            leftPanel.add(Box.createVerticalStrut(45)); //设置垂直间距
            //接收监听事件
            leftReceiveBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //TODO:待补充UI代码
                    System.out.println("接收");
                    cardLayout.show(rightPanel, "receive");
                    //刷新UI
                    mainFrame.revalidate();
                    mainFrame.repaint();
                }
            });
        //创建设置按钮
        JButton leftSettingBtn = new JButton("设置");
        leftSettingBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(leftSettingBtn);
        //设置监听事件
        leftSettingBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO:待补充UI代码
                System.out.println("设置");

            }
        });
        // 将左侧栏添加到主面板的左侧
        mainPanel.add(leftPanel, BorderLayout.WEST);




        /*创建右侧发送面板*/
        JPanel rightSendPanel = new JPanel();
        rightSendPanel.setLayout(new BoxLayout(rightSendPanel, BoxLayout.Y_AXIS));
        rightSendPanel.setPreferredSize(new Dimension(640, 0)); // 设置右侧栏的宽度
        rightSendPanel.add(Box.createVerticalStrut(10)); //设置垂直间距
        /*创建顶部面板*/
            JPanel topPanel = new JPanel();
            topPanel.setLayout(new FlowLayout());
            // 创建IPV4控件
            JLabel ipLabel = new JLabel("IP地址:");
            ipLabel.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 15));
            ipLabel.setBorder(BorderFactory.createEmptyBorder(0,5,0,0));
            ipSegment1 = new JTextField(3);
            ipSegment2 = new JTextField(3);
            ipSegment3 = new JTextField(3);
            ipSegment4 = new JTextField(3);
            // 传入applyIPSegmentPattern检查输入
            applyIPSegmentPattern(ipSegment1);
            applyIPSegmentPattern(ipSegment2);
            applyIPSegmentPattern(ipSegment3);
            applyIPSegmentPattern(ipSegment4);
            // 创建端口Label组件
            JLabel portLabel = new JLabel("端口:");
            portLabel.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 15));
            portTextField = new JTextField(5);
            applyPortPattern(portTextField);
            portLabel.setBorder(BorderFactory.createEmptyBorder(0,10,0,0));//设置portLabel的边框
            // 创建按钮
            connectButton = new JButton("连接");
            disconnectButton = new JButton("断开连接");
            selectFileButton = new JButton("选择发送文件");
            sendFileButton = new JButton("确认发送");
            cancelSelectedButton = new JButton("取消选择");
            /*添加组件到顶部面板*/
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
                //设置topPanel的边框
                topPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
            // 添加面板到主面板
            //mainPanel.add(topPanel, BorderLayout.NORTH);
            rightSendPanel.add(topPanel,BorderLayout.NORTH);//temp
        // 创建日志文本区域
        logTextArea = new JTextArea();
        logTextArea.setEditable(false);
        JScrollPane centerScrollPane = new JScrollPane(logTextArea);
        centerScrollPane.setPreferredSize(new Dimension(640, 400)); // 设置滚动面板的首选大小
            //添加面板到主面板
            //mainPanel.add(centerScrollPane, BorderLayout.CENTER);
            rightSendPanel.add(centerScrollPane,BorderLayout.CENTER);
        /*创建底部面板*/
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
                //设置bottomPanel的边框
                bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
                rightSendPanel.add(bottomPanel,BorderLayout.SOUTH);//添加底部面板到右侧面板
        rightPanel.add(rightSendPanel,"send");
        mainPanel.add(rightSendPanel, BorderLayout.CENTER); //添加右侧面板到主面板
        mainFrame.add(mainPanel);// 添加主面板到窗口

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
                    new ErrorDialog(mainFrame, "端口不能为空");
                }
                else if( Integer.parseInt(port) < 1024)
                {
                    new ErrorDialog(mainFrame, "端口不得小于1024");
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
                logTextArea.append(ip + ":" + port + " 已断开连接\n");
                disconnectButton.setVisible(false);
                selectFileButton.setVisible(false);
                sendFileButton.setVisible(false);
                connectButton.setVisible(true);
                cancelSelectedButton.setVisible(false);
                globalFilePath = null;
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
                                    globalFilePath = file.getAbsolutePath();
                                    logTextArea.append("已选择文件 " + globalFilePath + "\n");
                                    selectFileDialog.dispose();
                                    selectFileButton.setVisible(false);
                                    cancelSelectedButton.setVisible(true);
                                    sendFileButton.setVisible(true);

                                }
                                else{
                                    new ErrorDialog(selectFileDialog, "一次只能发送一个文件,请重新操作！");
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
                        globalFilePath = fileChooser.getSelectedFile().getAbsolutePath();
                        logTextArea.append("已选择文件: " + globalFilePath + "\n");
                        if (!globalFilePath.isEmpty()) {
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
                logTextArea.append("发送文件中...\n");
                logTextArea.append("发送成功！\n");
            }
        });

        cancelSelectedButton.addActionListener(new ActionListener() {
            /**
             *  "取消选择"按钮监听事件
             * */
            @Override
            public void actionPerformed(ActionEvent e) {
                logTextArea.append("已取消选取文件" + globalFilePath + "\n");
                globalFilePath = null;
                selectFileButton.setVisible(true);
                sendFileButton.setVisible(false);
                cancelSelectedButton.setVisible(false);
            }
        });


        /*创建接收面板*/
        JPanel rightReceivePanel = new JPanel();
        rightReceivePanel.add(new JLabel("接收功能面板"),BorderLayout.CENTER);
        rightReceivePanel.setLayout(new BoxLayout(rightReceivePanel, BoxLayout.Y_AXIS));
        rightReceivePanel.setPreferredSize(new Dimension(640, 0)); // 设置右侧栏的宽度
        rightReceivePanel.add(Box.createVerticalStrut(10)); //设置垂直间距
        rightPanel.add(rightReceivePanel, "receive");

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
