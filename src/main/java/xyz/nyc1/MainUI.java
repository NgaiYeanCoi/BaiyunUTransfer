package xyz.nyc1;

import xyz.nyc1.backend.Callback;
import xyz.nyc1.backend.Client;
import xyz.nyc1.backend.Request;
import xyz.nyc1.backend.Server;
import xyz.nyc1.backend.TransferPoint;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.awt.Desktop;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Objects;

import static xyz.nyc1.NetworkIpInterface.getHostIPs;

/**
 * @author NgaiYeanCoi
 * */

public class MainUI extends WindowAdapter implements Callback {
    private TransferPoint transferPoint;
    private ReceiveRequestUI receiveRequestUI;
    private String selectedDownloadDir;

    /**
     * 主要的UI界面
     */
    private JFrame mainFrame;
    private JPanel mainPanel;
    private JPanel leftPanel;
    private String globalFilePath;
    private CardLayout cardLayout;
    private JPanel centerPanel;
    private String currentCard = "default";
    private String mainColor = "#e6eef2";
    private String viceColor = "#f0f7fa"; //默认
    /*发送面板*/
    private JTextField ipSegment1;
    private JTextField ipSegment2;
    private JTextField ipSegment3;
    private JTextField ipSegment4;
    private JTextField sendPortTextField;
    private JTextArea sendLogTextArea;
    private JButton connectBtn;
    private JButton disconnectBtn;
    private JButton selectFileBtn;
    private JButton sendFileBtn;
    private JButton cancelSelectedBtn;
    /*发送面板*/
    private JPanel rightSendPanel;
    private JPanel sendTopPanel;
    private JPanel bottomPanel;
    /*接收面板*/
    private JTextArea receiveLogTextArea;
    private JPanel rightReceivePanel;
    private JPanel receiveTopPanel;
    /*设置面板*/
    private GridBagConstraints settingGbc;
    private JPanel settingCenterPanel;
    private JPanel settingPanel;
    private JPanel settingTopPanel;
    /*默认面板*/
    private JPanel defaultPanel;
    private JButton leftReceiveBtn;

    public MainUI() throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        createUI();
    }

    private void createUI() throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        // 创建主窗口
        mainFrame = new JFrame("BaiyunUTransfer");
        mainFrame.setSize(900, 600);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setResizable(false);
        mainFrame.addWindowListener(this);
        ImageIcon icon32 = new ImageIcon(Objects.requireNonNull(MainUI.class.getResource("/images/icon32x32.png")));
        mainFrame.setIconImage(icon32.getImage());
        // 创建主面板
        mainPanel = new JPanel(new BorderLayout());

        // 定义按钮的样式
        String lookAndFeel = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
        UIManager.setLookAndFeel(lookAndFeel);
        UIManager.put("Button.font", new Font("Microsoft YaHei UI", Font.PLAIN, 14));
        //UIManager.put("Button.background", Color.LIGHT_GRAY); // 设置背景颜色
        //UIManager.put("Button.foreground", Color.BLACK); // 设置前景颜色（文字颜色）
        //UIManager.put("Button.border", BorderFactory.createLineBorder(Color.BLACK, 1)); // 设置边框

        // 创建左侧栏面板
        leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setPreferredSize(new Dimension(260, 0));
        leftPanel.add(Box.createVerticalStrut(50));
        leftPanel.setBackground(Color.decode(mainColor));

        // 创建logoLabel控件
        JLabel leftLogoLabel = new JLabel("BaiyunUTransfer");
        leftLogoLabel.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 25));
        leftLogoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(leftLogoLabel);
        leftPanel.add(Box.createVerticalStrut(40));

        // 创建功能按钮
        JButton leftSendBtn = new JButton("发送");
        leftSendBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(leftSendBtn);
        leftPanel.add(Box.createVerticalStrut(45));

        leftReceiveBtn = new JButton("接收");
        leftReceiveBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(leftReceiveBtn);
        leftPanel.add(Box.createVerticalStrut(45));

        JButton leftSettingBtn = new JButton("设置");
        leftSettingBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(leftSettingBtn);
        leftPanel.add(Box.createVerticalStrut(45));

        ImageIcon settingLogo = new ImageIcon(Objects.requireNonNull(MainUI.class.getResource("/images/icon200x200.png")));
        ImageIcon settingLogoIcon = ImageReset.createRolloverIcon(settingLogo,0.2f); //半透明效果
        JLabel settingLogoLabel = new JLabel(settingLogoIcon);
        settingLogoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(settingLogoLabel);

        mainPanel.add(leftPanel, BorderLayout.WEST);

        // 创建中心面板，使用CardLayout管理不同的功能面板
        centerPanel = new JPanel(new CardLayout());
        cardLayout = (CardLayout) centerPanel.getLayout();

        // 创建右侧边栏默认面板
        defaultPanel = new JPanel();
        defaultPanel.setBackground(Color.decode(viceColor));
        defaultPanel.add(Box.createVerticalStrut(500)); //设置垂直间距
        // 创建默认面板信息部件Label
        JLabel defaultInfoLabel = new JLabel("请选择模式");
        defaultInfoLabel.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 25));
        defaultInfoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        defaultInfoLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
        //添加默认面板到centerPanel
        defaultPanel.add(defaultInfoLabel);
        centerPanel.add(defaultPanel, "default");

        // 创建右侧边栏发送面板
        rightSendPanel = new JPanel();
        rightSendPanel.setLayout(new BoxLayout(rightSendPanel, BoxLayout.Y_AXIS));
        rightSendPanel.setPreferredSize(new Dimension(640, 0)); // 设置右侧栏的宽度
        rightSendPanel.add(Box.createVerticalStrut(10)); //设置垂直间距
        rightSendPanel.setBackground(Color.decode(viceColor));
        // 创建顶部面板
        sendTopPanel = new JPanel();
        sendTopPanel.setLayout(new FlowLayout());
        sendTopPanel.setBackground(Color.decode(viceColor));
        // 创建IPV4组件
        JLabel ipLabel = new JLabel("IP地址:");
        ipLabel.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 15));
        ipLabel.setBorder(BorderFactory.createEmptyBorder(0,5,0,0));
        ipSegment1 = new JTextField(3);
        ipSegment2 = new JTextField(3);
        ipSegment3 = new JTextField(3);
        ipSegment4 = new JTextField(3);
        ipSegment1.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 12));
        ipSegment2.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 12));
        ipSegment3.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 12));
        ipSegment4.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 12));
        // 传入applyIPSegmentPattern检查输入
        applyIPSegmentPattern(ipSegment1);
        applyIPSegmentPattern(ipSegment2);
        applyIPSegmentPattern(ipSegment3);
        applyIPSegmentPattern(ipSegment4);
        // 创建端口组件
        JLabel sendPortLabel = new JLabel("端口:");
        sendPortLabel.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 15));
        sendPortTextField = new JTextField(5);
        sendPortTextField.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 12));
        applyPortPattern(sendPortTextField);
        sendPortLabel.setBorder(BorderFactory.createEmptyBorder(0,10,0,0));//设置portLabel的边框
        // 创建按钮
        connectBtn = new JButton("连接");
        disconnectBtn = new JButton("断开连接");
        selectFileBtn = new JButton("选择发送文件");
        sendFileBtn = new JButton("确认发送");
        cancelSelectedBtn = new JButton("取消选择");
        /*添加组件到顶部面板*/
            // IPv4输入面板
            sendTopPanel.add(ipLabel);
            sendTopPanel.add(ipSegment1);
            sendTopPanel.add(new JLabel("."));
            sendTopPanel.add(ipSegment2);
            sendTopPanel.add(new JLabel("."));
            sendTopPanel.add(ipSegment3);
            sendTopPanel.add(new JLabel("."));
            sendTopPanel.add(ipSegment4);
            // 端口输入面板
            sendTopPanel.add(sendPortLabel);
            sendTopPanel.add(sendPortTextField);
            // 按钮面板
            sendTopPanel.add(connectBtn);
            sendTopPanel.add(disconnectBtn);
        //设置topPanel的边框
        sendTopPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        // 添加面板到右侧面板
        rightSendPanel.add(sendTopPanel,BorderLayout.NORTH);
        rightSendPanel.add(Box.createVerticalStrut(10)); //设置垂直间距
        // 创建日志文本区域
        sendLogTextArea = new JTextArea();
        sendLogTextArea.setEditable(false);
        sendLogTextArea.setBackground(Color.decode(viceColor));
        JScrollPane sendCenterScrollPane = new JScrollPane(sendLogTextArea);
        sendCenterScrollPane.setBorder(null);
        sendCenterScrollPane.setPreferredSize(new Dimension(0, 500)); // 设置滚动面板的首选大小
        // 添加面板到右侧面板
        rightSendPanel.add(sendCenterScrollPane,BorderLayout.CENTER);
        rightSendPanel.add(Box.createVerticalStrut(5));
        // 创建底部面板
        bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout());
        bottomPanel.setBackground(Color.decode(viceColor));
        bottomPanel.add(selectFileBtn);
        bottomPanel.add(sendFileBtn);
        bottomPanel.add(cancelSelectedBtn);
        // 初始按钮为不可见
        sendFileBtn.setVisible(false);
        selectFileBtn.setVisible(false);
        disconnectBtn.setVisible(false);
        cancelSelectedBtn.setVisible(false);
        // 设置bottomPanel的边框
        //bottomPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // 添加连接按钮事件监听器
        connectBtn.addActionListener(e -> {
            String ip = ipSegment1.getText() + "." + ipSegment2.getText() + "." + ipSegment3.getText() + "." + ipSegment4.getText();
            String port = sendPortTextField.getText();
            // 判断ip以及port的合法性
            if((ipSegment1.getText().isEmpty()) || ipSegment2.getText().isEmpty()||ipSegment3.getText().isEmpty()||ipSegment4.getText().isEmpty()) {
                new ErrorDialog(mainFrame, "IP地址不能为空！");
            } else if(port.isEmpty()){
                new ErrorDialog(mainFrame, "端口不能为空");
            } else if( Integer.parseInt(port) < 1024) {
                new ErrorDialog(mainFrame, "端口不得小于1024");
            } else if(Integer.parseInt(port) > 65535) {
                new ErrorDialog(mainFrame, "端口不得大于65535");
            } else {
                sendLogTextArea.append("正在连接到 " + ip + ":" + port + "中...\n");
                transferPoint = new Client(ip, Integer.parseInt(port), selectedDownloadDir, this);
                transferPoint.start();
                leftReceiveBtn.setEnabled(false);
                connectBtn.setEnabled(false);
            }
        });

        disconnectBtn.addActionListener(e -> {
            String ip = ipSegment1.getText() + "." + ipSegment2.getText() + "." + ipSegment3.getText();
            String port = sendPortTextField.getText();
            if (transferPoint != null) {
                sendLogTextArea.append("正在断开 " + ip + ":" + port + "\n");
                transferPoint.close();
                transferPoint = null;
            }
            disconnectBtn.setVisible(false);
            selectFileBtn.setVisible(false);
            sendFileBtn.setVisible(false);
            connectBtn.setEnabled(true);
            connectBtn.setVisible(true);
            cancelSelectedBtn.setVisible(false);
            leftReceiveBtn.setEnabled(true);
            globalFilePath = null;
        });

        selectFileBtn.addActionListener(new ActionListener() {
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


                // 设置拖放目标
                new DropTarget(fileDropPanel, DnDConstants.ACTION_COPY_OR_MOVE, new DropTargetAdapter() {
                    public void drop(DropTargetDropEvent evt) {
                        try {
                            evt.acceptDrop(DnDConstants.ACTION_COPY);
                            Transferable transferable = evt.getTransferable();
                            if (transferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                                java.util.List<File> files = (List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);
                                if(files.size()==1){
                                    File file = files.get(0);
                                    globalFilePath = file.getAbsolutePath();
                                    receiveLogTextArea.append("已选择文件 " + globalFilePath + "\n");
                                    sendLogTextArea.append("已选择文件 " + globalFilePath + "\n");
                                    selectFileDialog.dispose();
                                    selectFileBtn.setVisible(false);
                                    cancelSelectedBtn.setVisible(true);
                                    sendFileBtn.setVisible(true);

                                } else {
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
                innerSelectBtn.addActionListener(e1 -> {
                    JFileChooser fileChooser = new JFileChooser();
                    int result = fileChooser.showOpenDialog(selectFileDialog);
                    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        globalFilePath = fileChooser.getSelectedFile().getAbsolutePath();
                        sendLogTextArea.append("已选择文件: " + globalFilePath + "\n");
                        receiveLogTextArea.append("已选择文件 " + globalFilePath + "\n");
                        if (!globalFilePath.isEmpty()) {
                            selectFileDialog.dispose();
                            selectFileBtn.setVisible(false);
                            cancelSelectedBtn.setVisible(true);
                            sendFileBtn.setVisible(true);
                        }
                    }
                });
                // 显示窗口
                selectFileDialog.setVisible(true);
            }
        });

        sendFileBtn.addActionListener(new ActionListener() {
            /**
             *  发送文件按钮监听事件
             * */
            @Override
            public void actionPerformed(ActionEvent e) {
                transferPoint.sendFile(globalFilePath);
                sendLogTextArea.append("发送文件中...\n");
                receiveLogTextArea.append("发送文件中...\n");
                selectFileBtn.setVisible(true);
                sendFileBtn.setVisible(false);
                cancelSelectedBtn.setVisible(false);
            }
        });

        cancelSelectedBtn.addActionListener(new ActionListener() {
            /**
             *  "取消选择"按钮监听事件
             * */
            @Override
            public void actionPerformed(ActionEvent e) {
                sendLogTextArea.append("已取消选取文件" + globalFilePath + "\n");
                receiveLogTextArea.append("已取消选取文件" + globalFilePath + "\n");
                globalFilePath = null;
                selectFileBtn.setVisible(true);
                sendFileBtn.setVisible(false);
                cancelSelectedBtn.setVisible(false);
            }
        });

        centerPanel.add(rightSendPanel, "send");// 将功能面板添加到卡片面板中

        // 创建接收面板
        rightReceivePanel = new JPanel();
        rightReceivePanel.setLayout(new BoxLayout(rightReceivePanel, BoxLayout.Y_AXIS));
        rightReceivePanel.setPreferredSize(new Dimension(640, 0)); // 设置右侧栏的宽度
        rightReceivePanel.add(Box.createVerticalStrut(10)); // 设置垂直间距
        rightReceivePanel.setBackground(Color.decode(viceColor));
        // 创建顶部面板
        receiveTopPanel = new JPanel();
        receiveTopPanel.setLayout(new FlowLayout());
        receiveTopPanel.setBackground(Color.decode(viceColor));
        // 创建IP地址标签
        JLabel receiveIpLabel = new JLabel("IP地址:");
        receiveIpLabel.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 15));
        receiveIpLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
        // 创建IP地址显示区域
        JTextArea ipTextArea = new JTextArea(5, 20);
        ipTextArea.setEditable(false);
        ipTextArea.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 12));
        ipTextArea.setText(getHostIPs()); // 获取并显示主机的IP地址
        ipTextArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        // 创建端口Label组件
        JLabel receivePortLabel = new JLabel("端口:");
        receivePortLabel.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 15));
        receivePortLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        // 创建端口输入框
        JTextField receivePortTextField = new JTextField(5);
        receivePortTextField.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 12));
        applyPortPattern(receivePortTextField); // 应用端口号输入正则表达式
        // 创建监听按钮
        JButton receiveListenBtn = new JButton("监听端口");
        //创建停止监听按钮
        JButton receivePauseListenBtn = new JButton("停止监听");
        // 添加组件到顶部面板
        receiveTopPanel.add(receiveIpLabel);
        receiveTopPanel.add(new JScrollPane(ipTextArea)); // 使用滚动面板支持多行文本
        receiveTopPanel.add(receivePortLabel);
        receiveTopPanel.add(receivePortTextField);
        receiveTopPanel.add(receiveListenBtn);
        receiveTopPanel.add(receivePauseListenBtn);
        //初始化按钮可见
        receiveListenBtn.setVisible(true);
        receivePauseListenBtn.setVisible(false);
        // 添加顶部面板到接收面板
        rightReceivePanel.add(receiveTopPanel, BorderLayout.NORTH);
        rightReceivePanel.add(Box.createVerticalStrut(10)); // 设置垂直间距
        // 创建日志文本区域
        receiveLogTextArea = new JTextArea();
        receiveLogTextArea.setEditable(false);
        receiveLogTextArea.setBackground((Color.decode(viceColor)));
        JScrollPane receiveCenterScrollPane = new JScrollPane(receiveLogTextArea);
        receiveCenterScrollPane.setPreferredSize(new Dimension(0, 500)); // 设置滚动面板的首选大小
        receiveCenterScrollPane.setBorder(null);
        rightReceivePanel.add(receiveCenterScrollPane,BorderLayout.CENTER);

        // 添加监听按钮事件监听器
        receiveListenBtn.addActionListener(e -> {
            String port = receivePortTextField.getText();
            // 判断port的合法性
            if(port.isEmpty()) {
                new ErrorDialog(mainFrame, "端口不能为空");
            } else if(Integer.parseInt(port) < 1024) {
                new ErrorDialog(mainFrame, "端口不得小于1024");
            } else if(Integer.parseInt(port) > 65535) {
                new ErrorDialog(mainFrame, "端口不得大于65535");
            } else {
                receiveLogTextArea.append("正在监听本机"+receivePortTextField.getText()+"端口中...\n");
                try {
                    transferPoint = new Server(Integer.parseInt(port), selectedDownloadDir, this);
                    transferPoint.start();
                    selectFileBtn.setVisible(true);
                } catch (IOException ex) {
                    receiveLogTextArea.append("启动服务器失败，此端口可能已被占用，请选择其他端口号！" + ex + "\n");
                    return;
                }
                receiveListenBtn.setVisible(false);
                receivePauseListenBtn.setVisible(true);
                leftSendBtn.setEnabled(false);
            }
        });

        // 添加停止监听按钮事件监听器
        receivePauseListenBtn.addActionListener(e -> {
            //TODO:需连接后端API断开
            if (transferPoint != null) {
                transferPoint.close();
                transferPoint = null;
            }
            receiveLogTextArea.append("已停止监听\n");
            receivePauseListenBtn.setVisible(false);
            receiveListenBtn.setVisible(true);
            leftSendBtn.setEnabled(true);
        });


        centerPanel.add(rightReceivePanel, "receive");// 将功能面板添加到卡片面板中



        // 创建设置面板
        settingPanel = new JPanel();
        settingPanel.setLayout(new BoxLayout(settingPanel, BoxLayout.Y_AXIS));
        settingPanel.setBackground(Color.decode(viceColor));
        settingPanel.add(Box.createVerticalStrut(50));

        // 创建设置顶部Panel
        settingTopPanel = new JPanel();
        settingTopPanel.setLayout(new BoxLayout(settingTopPanel, BoxLayout.Y_AXIS));
        settingTopPanel.setBackground(Color.decode(viceColor));

        // 创建设置顶部信息Label
        JLabel settingInfoLabel = new JLabel("设置");
        settingInfoLabel.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 25));
        settingInfoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        settingPanel.add(settingInfoLabel);//将settingInfoLabel添加进settingTopPanel里
        settingPanel.add(settingTopPanel, BorderLayout.NORTH);



        // 创建中间设置选项面板
        settingCenterPanel = new JPanel();
        settingCenterPanel.setLayout(new GridBagLayout());
        settingCenterPanel.setBackground(Color.decode(viceColor));
        settingCenterPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));


        settingGbc = new GridBagConstraints();
        settingGbc.fill = GridBagConstraints.HORIZONTAL;
        settingGbc.insets = new Insets(20, 50,20 , 50);
        settingGbc.gridx = 0;
        settingGbc.gridy = 0;

        // 添加设置选项和按钮
        //清空日志板块
        addSettingLabel("清空所有日志");
        JButton settingClearLogBtn = new JButton("清空");
        addSettingBtn(settingClearLogBtn);
        settingClearLogBtn.addActionListener(e -> {
            receiveLogTextArea.setText("");
            sendLogTextArea.setText("");
            new InfoDialog(mainFrame,"清空成功！");
        });

        //设置保存路径板块
        addSettingLabel("默认保存路径");
        JButton settingDefaultPathBtn = new JButton("设置");
        addSettingBtn(settingDefaultPathBtn);
        JLabel settingDefaultPathLabel = new JLabel("当前的路径为：");
        settingDefaultPathLabel.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
        settingGbc.gridx = 0;
        settingGbc.weightx = 1.0; // 让标签占据更多空间
        settingCenterPanel.add(settingDefaultPathLabel, settingGbc);
        settingGbc.gridy++;  // 移动到下一行
        settingDefaultPathBtn.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int result = fileChooser.showOpenDialog(mainFrame);
            if (result == JFileChooser.APPROVE_OPTION) {
                selectedDownloadDir = fileChooser.getSelectedFile().getAbsolutePath();
                if (transferPoint != null)
                    transferPoint.setDownloadDir(new File(selectedDownloadDir));
                settingDefaultPathLabel.setText("当前路径为："+selectedDownloadDir);
                new InfoDialog(mainFrame,"设置默认路径成功！");
            }
        });


        // 修改主题样式板块
        addSettingLabel("修改主题颜色");
        JComboBox<String>selectStyleBox=new JComboBox<>();
        selectStyleBox.addItem("默认");
        selectStyleBox.addItem("亮白");//TODO：可添加其他主题颜色
        selectStyleBox.addItem("樱花粉");
        selectStyleBox.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 14));
        // 设置渲染器以居中显示文本
        selectStyleBox.setRenderer(new CenteredComboBoxRenderer());
        settingGbc.gridx = 1;
        settingGbc.weightx = 0.0; // 按钮不需要额外空间
        settingCenterPanel.add(selectStyleBox, settingGbc);
        settingGbc.gridy++;  // 移动到下一行
        selectStyleBox.addActionListener(e -> {
            String  selectedItem=(String)selectStyleBox.getSelectedItem();
            if("默认".equals(selectedItem)) {
                mainColor = "#e6eef2";
                viceColor = "#f0f7fa";
                changeStyle(mainColor,viceColor);
            } else if("亮白".equals(selectedItem)) {
                mainColor = "#ffffff";
                viceColor = "#f8f9fa";
                changeStyle(mainColor,viceColor);
            } else if ("樱花粉".equals(selectedItem)) {
                mainColor = "#f2e1ed";
                viceColor = "#faf2f7";
                changeStyle(mainColor,viceColor);
            }
        });

        //Github
        addSettingLabel("Source Code (Github)");
        JButton settingGithubBtn = new JButton("打开");
        addSettingBtn(settingGithubBtn);
        settingGithubBtn.addActionListener(e -> {
            try {
                // 打开 GitHub 主页
                Desktop.getDesktop().browse(new URI("https://github.com/NgaiYeanCoi/BaiyunUTransfer"));
            } catch (Exception ex) {
                ex.printStackTrace();
                new ErrorDialog(mainFrame,ex.getMessage());
            }
        });


        settingPanel.add(settingCenterPanel,BorderLayout.CENTER);
        // 将功能面板添加到卡片面板中
        centerPanel.add(settingPanel, "setting");
        //centerPanel添加到主面板中
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        //主面板添加到主视窗中
        mainFrame.add(mainPanel);

        // 添加按钮监听事件
        leftSendBtn.addActionListener(e -> {
            cardLayout.show(centerPanel, "send");
            currentCard = "send";
        });

        leftReceiveBtn.addActionListener(e -> {
            cardLayout.show(centerPanel, "receive");
            currentCard = "receive";
        });

        leftSettingBtn.addActionListener(e -> {
            cardLayout.show(centerPanel, "setting");
            currentCard = "setting";
        });

        mainFrame.setVisible(true);
    }

    private void addSettingLabel(String labelName){
        JLabel label = new JLabel(labelName);
        label.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
        settingGbc.gridx = 0;
        settingGbc.weightx = 1.0; // 让标签占据更多空间
        settingCenterPanel.add(label, settingGbc);
    }

    private  void addSettingBtn(JButton button){
        settingGbc.gridx = 1;
        settingGbc.weightx = 0.0; // 按钮不需要额外空间
        settingCenterPanel.add(button, settingGbc);
        settingGbc.gridy++;  // 移动到下一行
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

    private void changeStyle(String mainColor,String viceColor){ // 切换主题
        leftPanel.setBackground(Color.decode(mainColor));
        defaultPanel.setBackground(Color.decode(viceColor));
        rightSendPanel.setBackground(Color.decode(viceColor));
        sendTopPanel.setBackground(Color.decode(viceColor));
        bottomPanel.setBackground(Color.decode(viceColor));
        rightReceivePanel.setBackground(Color.decode(viceColor));
        receiveTopPanel.setBackground(Color.decode(viceColor));
        settingPanel.setBackground(Color.decode(viceColor));
        settingTopPanel.setBackground(Color.decode(viceColor));
        settingCenterPanel.setBackground(Color.decode(viceColor));
        sendLogTextArea.setBackground(Color.decode(viceColor));
        receiveLogTextArea.setBackground((Color.decode(viceColor)));
    }

    static class CenteredComboBoxRenderer extends DefaultListCellRenderer {
        /*
        * 使Label居中显示
        * */
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            // 调用父类方法获取组件
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            // 设置文本居中
            label.setHorizontalAlignment(SwingConstants.CENTER);

            return label;
        }
    }

    @Override
    public void onNewConnection(TransferPoint transferPoint, String address, Request request) {
        if (request != null) request.accept();
        String msg = "已成功连接到 " + address + "\n";
        selectFileBtn.setVisible(true);
        if (transferPoint instanceof Server) {
            receiveLogTextArea.append(msg);
            rightReceivePanel.add(bottomPanel,BorderLayout.SOUTH);//添加底部面板到右侧面板
        } else {
            sendLogTextArea.append(msg);
            rightSendPanel.add(bottomPanel,BorderLayout.SOUTH);//添加底部面板到右侧面板
            connectBtn.setVisible(false);
            disconnectBtn.setVisible(true);
        }
//        bottomPanel.invalidate();
//        bottomPanel.revalidate();
//        bottomPanel.validate();
//        bottomPanel.repaint();
        SwingUtilities.updateComponentTreeUI(bottomPanel);
    }

    @Override
    public void onConnectionFailed(TransferPoint transferPoint, String address, Exception e) {
        if (e instanceof UnknownHostException) {
            sendLogTextArea.append("欲连接主机不存在或您未连接到目标网络！\n");
        } else {
            sendLogTextArea.append("连接失败，请检查网络、目标主机地址及端口号！\n" + e + "\n");
        }
        if (transferPoint instanceof Server) {
            rightReceivePanel.remove(bottomPanel);
        } else {
            rightSendPanel.remove(bottomPanel);
        }
        leftReceiveBtn.setEnabled(true);
        connectBtn.setEnabled(true);
        SwingUtilities.updateComponentTreeUI(bottomPanel);
    }

    @Override
    public void onLostConnection(TransferPoint transferPoint, String address) {
        String msg = address + " 已断开连接\n";
        disconnectBtn.setVisible(false);
        selectFileBtn.setVisible(false);
        sendFileBtn.setVisible(false);
        cancelSelectedBtn.setVisible(false);
        if (transferPoint instanceof Server) {
            receiveLogTextArea.append(msg);
            rightReceivePanel.remove(bottomPanel);
        } else {
            sendLogTextArea.append(msg);
            rightSendPanel.remove(bottomPanel);
            connectBtn.setEnabled(true);
            connectBtn.setVisible(true);
            leftReceiveBtn.setEnabled(true);
            globalFilePath = null;
        }
        SwingUtilities.updateComponentTreeUI(bottomPanel);
    }

    @Override
    public void onReceiveFile(TransferPoint transferPoint, String filename, String address, Request request) {
        receiveRequestUI = new ReceiveRequestUI();
        try {
            receiveRequestUI.show(mainFrame,filename, address, request);
        } catch (UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onTransferSuccess(TransferPoint transferPoint, File outputFile) {
        String msg = outputFile == null ? "发送文件成功\n" : "接收文件成功，放置在 " + outputFile + "\n";
        if (transferPoint instanceof Server) {
            receiveLogTextArea.append(msg);
        } else {
            sendLogTextArea.append(msg);
        }
        if (receiveRequestUI != null) {
            receiveRequestUI.onTransferSuccess(outputFile);
            receiveRequestUI = null;
        }
    }

    @Override
    public void onTransferFailed(TransferPoint transferPoint, String filename) {
        String msg = "传输文件 " + filename + " 失败！\n";
        if (transferPoint instanceof Server) {
            receiveLogTextArea.append(msg);
        } else {
            sendLogTextArea.append(msg);
        }
        if (receiveRequestUI != null) {
            receiveRequestUI.onTransferFailed(mainFrame);
            receiveRequestUI = null;
        }
    }

    @Override public void windowClosing(WindowEvent e) {
        if (transferPoint != null) {
            transferPoint.close();
            transferPoint = null;
        }
    }
}
