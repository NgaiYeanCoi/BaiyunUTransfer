package xyz.nyc1;

import xyz.nyc1.backend.Callback;
import xyz.nyc1.backend.Request;
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
import java.io.File;
import java.net.URI;
import java.awt.Desktop;
import java.util.List;
import static xyz.nyc1.NetworkIpInterface.getHostIPs;

/**
 * @author NgaiYeanCoi
 * */

public class mainUI implements Callback {
    /**
     * ��Ҫ��UI����
     */
    private JFrame mainFrame;
    private JPanel mainPanel;
    private JPanel leftPanel;
    private String globalFilePath;
    private CardLayout cardLayout;
    private JPanel centerPanel;
    private String currentCard = "default";
    private String mainColor = "#e6eef2";
    private String viceColor = "#f0f7fa";
    /*�������*/
    private JTextField ipSegment1;
    private JTextField ipSegment2;
    private JTextField ipSegment3;
    private JTextField ipSegment4;
    private JTextField sendPortTextField;
    private JTextArea sendLogTextArea;
    private JButton connectButton;
    private JButton disconnectButton;
    private JButton selectFileButton;
    private JButton sendFileButton;
    private JButton cancelSelectedButton;
    /*�������*/
    private JPanel rightSendPanel;
    private JPanel sendTopPanel;
    private JPanel bottomPanel;
    /*�������*/
    private JTextArea receiveLogTextArea;
    private JPanel rightReceivePanel;
    private JPanel receiveTopPanel;
    /*�������*/
    private GridBagConstraints settingGbc;
    private JPanel settingCenterPanel;
    private JPanel settingPanel;
    private JPanel settingTopPanel;
    /*Ĭ�����*/
    private JPanel defaultPanel;


    public mainUI() throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        createUI();
    }

    private void createUI() throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        // ����������
        mainFrame = new JFrame("BaiyunUTransfer");
        mainFrame.setSize(900, 600);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setResizable(false);
        //ImageIcon icon32 = new ImageIcon(Objects.requireNonNull(mainUI.class.getResource("/images/icon32x32.png")));
        //mainFrame.setIconImage(icon32.getImage());

        // ���������
        mainPanel = new JPanel(new BorderLayout());

        // ���尴ť����ʽ
        String lookAndFeel = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
        UIManager.setLookAndFeel(lookAndFeel);
        UIManager.put("Button.font", new Font("Microsoft YaHei UI", Font.PLAIN, 14));
        //UIManager.put("Button.background", Color.LIGHT_GRAY); // ���ñ�����ɫ
        //UIManager.put("Button.foreground", Color.BLACK); // ����ǰ����ɫ��������ɫ��
        //UIManager.put("Button.border", BorderFactory.createLineBorder(Color.BLACK, 1)); // ���ñ߿�

        // ������������
        leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setPreferredSize(new Dimension(260, 0));
        leftPanel.add(Box.createVerticalStrut(50));
        leftPanel.setBackground(Color.decode(mainColor));

        // ����logoLabel�ؼ�
        JLabel leftLogoLabel = new JLabel("BaiyunUTransfer");
        leftLogoLabel.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 25));
        leftLogoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(leftLogoLabel);
        leftPanel.add(Box.createVerticalStrut(40));

        /*�������ܰ�ť*/
        JButton leftSendBtn = new JButton("����");
        leftSendBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(leftSendBtn);
        leftPanel.add(Box.createVerticalStrut(45));

        JButton leftReceiveBtn = new JButton("����");
        leftReceiveBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(leftReceiveBtn);
        leftPanel.add(Box.createVerticalStrut(45));

        JButton leftSettingBtn = new JButton("����");
        leftSettingBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(leftSettingBtn);

        mainPanel.add(leftPanel, BorderLayout.WEST);

        // ����������壬ʹ��CardLayout����ͬ�Ĺ������
        centerPanel = new JPanel(new CardLayout());
        cardLayout = (CardLayout) centerPanel.getLayout();

        // �����Ҳ����Ĭ�����
        defaultPanel = new JPanel();
        defaultPanel.setBackground(Color.decode(viceColor));
        defaultPanel.add(Box.createVerticalStrut(500)); //���ô�ֱ���
        // ����Ĭ�������Ϣ����Label
        JLabel defaultInfoLabel = new JLabel("��ѡ��ģʽ");
        defaultInfoLabel.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 25));
        defaultInfoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        defaultInfoLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
        //���Ĭ����嵽centerPanel
        defaultPanel.add(defaultInfoLabel);
        centerPanel.add(defaultPanel, "default");

        // �����Ҳ�����������
        rightSendPanel = new JPanel();
        rightSendPanel.setLayout(new BoxLayout(rightSendPanel, BoxLayout.Y_AXIS));
        rightSendPanel.setPreferredSize(new Dimension(640, 0)); // �����Ҳ����Ŀ��
        rightSendPanel.add(Box.createVerticalStrut(10)); //���ô�ֱ���
        rightSendPanel.setBackground(Color.decode(viceColor));
        // �����������
        sendTopPanel = new JPanel();
        sendTopPanel.setLayout(new FlowLayout());
        sendTopPanel.setBackground(Color.decode(viceColor));
        // ����IPV4�ؼ�
        JLabel ipLabel = new JLabel("IP��ַ:");
        ipLabel.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 15));
        ipLabel.setBorder(BorderFactory.createEmptyBorder(0,5,0,0));
        ipSegment1 = new JTextField(5);
        ipSegment2 = new JTextField(5);
        ipSegment3 = new JTextField(5);
        ipSegment4 = new JTextField(5);
        // ����applyIPSegmentPattern�������
        applyIPSegmentPattern(ipSegment1);
        applyIPSegmentPattern(ipSegment2);
        applyIPSegmentPattern(ipSegment3);
        applyIPSegmentPattern(ipSegment4);
        // �����˿�Label���
        JLabel sendPortLabel = new JLabel("�˿�:");
        sendPortLabel.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 15));
        sendPortTextField = new JTextField(8);
        applyPortPattern(sendPortTextField);
        sendPortLabel.setBorder(BorderFactory.createEmptyBorder(0,10,0,0));//����portLabel�ı߿�
        // ������ť
        connectButton = new JButton("����");
        disconnectButton = new JButton("�Ͽ�����");
        selectFileButton = new JButton("ѡ�����ļ�");
        sendFileButton = new JButton("ȷ�Ϸ���");
        cancelSelectedButton = new JButton("ȡ��ѡ��");
        /*���������������*/
            // IPv4�������
            sendTopPanel.add(ipLabel);
            sendTopPanel.add(ipSegment1);
            sendTopPanel.add(new JLabel("."));
            sendTopPanel.add(ipSegment2);
            sendTopPanel.add(new JLabel("."));
            sendTopPanel.add(ipSegment3);
            sendTopPanel.add(new JLabel("."));
            sendTopPanel.add(ipSegment4);
            // �˿��������
            sendTopPanel.add(sendPortLabel);
            sendTopPanel.add(sendPortTextField);
            // ��ť���
            sendTopPanel.add(connectButton);
            sendTopPanel.add(disconnectButton);
        //����topPanel�ı߿�
        sendTopPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        // �����嵽�Ҳ����
        rightSendPanel.add(sendTopPanel,BorderLayout.NORTH);
        rightSendPanel.add(Box.createVerticalStrut(10)); //���ô�ֱ���
        // ������־�ı�����
        sendLogTextArea = new JTextArea();
        sendLogTextArea.setEditable(false);
        JScrollPane sendCenterScrollPane = new JScrollPane(sendLogTextArea);
        sendCenterScrollPane.setBorder(null);
        sendCenterScrollPane.setPreferredSize(new Dimension(0, 500)); // ���ù���������ѡ��С
        // �����嵽�Ҳ����
        rightSendPanel.add(sendCenterScrollPane,BorderLayout.CENTER);
        rightSendPanel.add(Box.createVerticalStrut(5));
        // �����ײ����
        bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout());
        bottomPanel.setBackground(Color.decode(viceColor));
        bottomPanel.add(selectFileButton);
        bottomPanel.add(sendFileButton);
        bottomPanel.add(cancelSelectedButton);
        // ��ʼ��ťΪ���ɼ�
        sendFileButton.setVisible(false);
        selectFileButton.setVisible(false);
        disconnectButton.setVisible(false);
        cancelSelectedButton.setVisible(false);
        // ����bottomPanel�ı߿�
        //bottomPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        rightSendPanel.add(bottomPanel,BorderLayout.SOUTH);//��ӵײ���嵽�Ҳ����

        // ������Ӱ�ť�¼�������
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String ip = ipSegment1.getText() + "." + ipSegment2.getText() + "." + ipSegment3.getText() + "." + ipSegment4.getText();
                String port = sendPortTextField.getText();
                // �ж�ip�Լ�port�ĺϷ���
                if((ipSegment1.getText().isEmpty()) || ipSegment2.getText().isEmpty()||ipSegment3.getText().isEmpty()||ipSegment4.getText().isEmpty()) {
                    new ErrorDialog(mainFrame, "IP��ַ����Ϊ�գ�");
                }
                else if(port.isEmpty()){
                    new ErrorDialog(mainFrame, "�˿ڲ���Ϊ��");
                }
                else if( Integer.parseInt(port) < 1024)
                {
                    new ErrorDialog(mainFrame, "�˿ڲ���С��1024");
                }
                else if(Integer.parseInt(port) > 65535)
                {
                    new ErrorDialog(mainFrame, "�˿ڲ��ô���65535");
                }
                else {
                    // TODO:������ӷ������Ĵ��룬��Ҫ���Ӻ��API
                    sendLogTextArea.append("�������ӵ� " + ip + ":" + port + "��...\n");
                    sendLogTextArea.append("�ѳɹ����ӣ�\n");
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
                String port = sendPortTextField.getText();
                // TODO:�Ͽ����ӣ���Ҫ���api
                sendLogTextArea.append(ip + ":" + port + " �ѶϿ�����\n");
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
             * ����ģ̬����ѡ���ļ����͵Ĵ���
             * */
            @Override
            public void actionPerformed(ActionEvent e) {
                // ����ģ̬���ļ�ѡ�񴰿�
                JDialog selectFileDialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(mainFrame), "ѡ���ļ�", true);
                selectFileDialog.setSize(400, 300);
                selectFileDialog.setLocationRelativeTo(null);
                selectFileDialog.setResizable(false);
                selectFileDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                selectFileDialog.setLayout(new BorderLayout());
                // �����ļ��Ϸ����
                JPanel fileDropPanel = new JPanel();
                fileDropPanel.setBorder(BorderFactory.createTitledBorder("�Ϸ��ļ����˴�"));
                fileDropPanel.setPreferredSize(new Dimension(400, 200));
                fileDropPanel.setLayout(new BorderLayout());


                // ���ֽ���
                JPanel panel = new JPanel();
                panel.setLayout(new BorderLayout());
                panel.add(fileDropPanel, BorderLayout.CENTER);
                selectFileDialog.getContentPane().add(fileDropPanel, BorderLayout.CENTER);

                //��ť�ؼ�
                JButton innerSelectBtn = new JButton("ѡ���ļ�");
                JPanel bottomPanel = new JPanel();
                bottomPanel.setLayout(new FlowLayout());
                bottomPanel.add(innerSelectBtn);
                selectFileDialog.getContentPane().add(bottomPanel, BorderLayout.SOUTH);


                // �����Ϸ�Ŀ��
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
                                    sendLogTextArea.append("��ѡ���ļ� " + globalFilePath + "\n");
                                    selectFileDialog.dispose();
                                    selectFileButton.setVisible(false);
                                    cancelSelectedButton.setVisible(true);
                                    sendFileButton.setVisible(true);

                                }
                                else{
                                    new ErrorDialog(selectFileDialog, "һ��ֻ�ܷ���һ���ļ�,�����²�����");
                                    evt.rejectDrop();

                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                // �ڲ��Ӵ��ļ�ѡ��ť�����ļ������
                innerSelectBtn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JFileChooser fileChooser = new JFileChooser();
                        int result = fileChooser.showOpenDialog(selectFileDialog);
                        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                        if (result == JFileChooser.APPROVE_OPTION) {
                            globalFilePath = fileChooser.getSelectedFile().getAbsolutePath();
                            sendLogTextArea.append("��ѡ���ļ�: " + globalFilePath + "\n");
                            if (!globalFilePath.isEmpty()) {
                                selectFileDialog.dispose();
                                selectFileButton.setVisible(false);
                                cancelSelectedButton.setVisible(true);
                                sendFileButton.setVisible(true);
                            }
                        }
                    }
                });
                // ��ʾ����
                selectFileDialog.setVisible(true);
            }
        });

        sendFileButton.addActionListener(new ActionListener() {
            /**
             *  �����ļ���ť�����¼�
             * */
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO:�����ļ�����Ҫ���api
                sendLogTextArea.append("�����ļ���...\n");
                sendLogTextArea.append("���ͳɹ���\n");
            }
        });

        cancelSelectedButton.addActionListener(new ActionListener() {
            /**
             *  "ȡ��ѡ��"��ť�����¼�
             * */
            @Override
            public void actionPerformed(ActionEvent e) {
                sendLogTextArea.append("��ȡ��ѡȡ�ļ�" + globalFilePath + "\n");
                globalFilePath = null;
                selectFileButton.setVisible(true);
                sendFileButton.setVisible(false);
                cancelSelectedButton.setVisible(false);
            }
        });

        centerPanel.add(rightSendPanel, "send");// �����������ӵ���Ƭ�����

        // �����������
        rightReceivePanel = new JPanel();
        rightReceivePanel.setLayout(new BoxLayout(rightReceivePanel, BoxLayout.Y_AXIS));
        rightReceivePanel.setPreferredSize(new Dimension(640, 0)); // �����Ҳ����Ŀ��
        rightReceivePanel.add(Box.createVerticalStrut(10)); // ���ô�ֱ���
        rightReceivePanel.setBackground(Color.decode(viceColor));
        // �����������
        receiveTopPanel = new JPanel();
        receiveTopPanel.setLayout(new FlowLayout());
        receiveTopPanel.setBackground(Color.decode(viceColor));
        // ����IP��ַ��ǩ
        JLabel receiveIpLabel = new JLabel("IP��ַ:");
        receiveIpLabel.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 15));
        receiveIpLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
        // ����IP��ַ��ʾ����
        JTextArea ipTextArea = new JTextArea(5, 20);
        ipTextArea.setEditable(false);
        ipTextArea.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 12));
        ipTextArea.setText(getHostIPs()); // ��ȡ����ʾ������IP��ַ
        ipTextArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        // �����˿�Label���
        JLabel receivePortLabel = new JLabel("�˿�:");
        receivePortLabel.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 15));
        receivePortLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        // �����˿������
        JTextField receivePortTextField = new JTextField(8);
        applyPortPattern(receivePortTextField); // Ӧ�ö˿ں�����������ʽ
        // ����������ť
        JButton receiveListenBtn = new JButton("�����˿�");
        //����ֹͣ������ť
        JButton receivePauseListenBtn = new JButton("ֹͣ����");
        // ���������������
        receiveTopPanel.add(receiveIpLabel);
        receiveTopPanel.add(new JScrollPane(ipTextArea)); // ʹ�ù������֧�ֶ����ı�
        receiveTopPanel.add(receivePortLabel);
        receiveTopPanel.add(receivePortTextField);
        receiveTopPanel.add(receiveListenBtn);
        receiveTopPanel.add(receivePauseListenBtn);
        //��ʼ����ť�ɼ�
        receiveListenBtn.setVisible(true);
        receivePauseListenBtn.setVisible(false);
        // ��Ӷ�����嵽�������
        rightReceivePanel.add(receiveTopPanel, BorderLayout.NORTH);
        rightReceivePanel.add(Box.createVerticalStrut(10)); // ���ô�ֱ���
        // ������־�ı�����
        receiveLogTextArea = new JTextArea();
        receiveLogTextArea.setEditable(false);
        //receiveLogTextArea.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 10));
        JScrollPane receiveCenterScrollPane = new JScrollPane(receiveLogTextArea);
        receiveCenterScrollPane.setPreferredSize(new Dimension(0, 500)); // ���ù���������ѡ��С
        receiveCenterScrollPane.setBorder(null);
        rightReceivePanel.add(receiveCenterScrollPane);

        // ��Ӽ�����ť�¼�������
        receiveListenBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String port = receivePortTextField.getText();
                // �ж�port�ĺϷ���
                if(port.isEmpty()) {
                    new ErrorDialog(mainFrame, "�˿ڲ���Ϊ��");
                }
                else if( Integer.parseInt(port) < 1024)
                {
                    new ErrorDialog(mainFrame, "�˿ڲ���С��1024");
                }
                else if(Integer.parseInt(port) > 65535)
                {
                    new ErrorDialog(mainFrame, "�˿ڲ��ô���65535");
                }
                else {
                    // TODO:������ӷ������Ĵ��룬��Ҫ���Ӻ��API
                    receiveLogTextArea.append("���ڼ�������"+receivePortTextField.getText()+"�˿���...\n");
                    receiveListenBtn.setVisible(false);
                    receivePauseListenBtn.setVisible(true);

                }
            }
        });

        // ���ֹͣ������ť�¼�������
        receivePauseListenBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO:�����Ӻ��API�Ͽ�
                receiveLogTextArea.append("��ֹͣ����\n");
                receivePauseListenBtn.setVisible(false);
                receiveListenBtn.setVisible(true);
            }
        });


        centerPanel.add(rightReceivePanel, "receive");// �����������ӵ���Ƭ�����



        // �����������
        settingPanel = new JPanel();
        settingPanel.setLayout(new BoxLayout(settingPanel, BoxLayout.Y_AXIS));
        settingPanel.setBackground(Color.decode(viceColor));
        settingPanel.add(Box.createVerticalStrut(50));

        // �������ö���Panel
        settingTopPanel = new JPanel();
        settingTopPanel.setLayout(new BoxLayout(settingTopPanel, BoxLayout.Y_AXIS));
        settingTopPanel.setBackground(Color.decode(viceColor));

        // �������ö�����ϢLabel
        JLabel settingInfoLabel = new JLabel("����");
        settingInfoLabel.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 25));
        settingInfoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        settingPanel.add(settingInfoLabel);//��settingInfoLabel��ӽ�settingTopPanel��
        settingPanel.add(settingTopPanel, BorderLayout.NORTH);


        // �����м�����ѡ�����
        settingCenterPanel = new JPanel();
        settingCenterPanel.setLayout(new GridBagLayout());
        settingCenterPanel.setBackground(Color.decode(viceColor));
        settingCenterPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));


        settingGbc = new GridBagConstraints();
        settingGbc.fill = GridBagConstraints.HORIZONTAL;
        settingGbc.insets = new Insets(20, 50,20 , 50);
        settingGbc.gridx = 0;
        settingGbc.gridy = 0;

        // �������ѡ��Ͱ�ť
        //�����־���
        addSettingLabel("���������־");
        JButton settingClearLogBtn = new JButton("���");
        addSettingBtn(settingClearLogBtn);
        settingClearLogBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            receiveLogTextArea.setText("");
            sendLogTextArea.setText("");
            new InfoDialog(mainFrame,"��ճɹ���");
            }
        });

        //���ñ���·�����
        addSettingLabel("Ĭ�ϱ���·��");
        JButton settingDefaultPathBtn = new JButton("����");
        addSettingBtn(settingDefaultPathBtn);
        JLabel settingDefaultPathLabel = new JLabel("��ǰ��·��Ϊ��");
        settingDefaultPathLabel.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
        settingGbc.gridx = 0;
        settingGbc.weightx = 1.0; // �ñ�ǩռ�ݸ���ռ�
        settingCenterPanel.add(settingDefaultPathLabel, settingGbc);
        settingGbc.gridy++;  // �ƶ�����һ��
        settingDefaultPathBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int result = fileChooser.showOpenDialog(mainFrame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    String selectedPath = fileChooser.getSelectedFile().getAbsolutePath();
                    //TODO����Ӻ��API
                    settingDefaultPathLabel.setText("��ǰ·��Ϊ��"+selectedPath);
                    new InfoDialog(mainFrame,"����Ĭ��·���ɹ���");

                }

            }
        });


        // �޸�������ʽ���
        addSettingLabel("�޸�������ɫ");
        JComboBox<String>selectStyleBox=new JComboBox<>();
        selectStyleBox.addItem("Ĭ��");
        selectStyleBox.addItem("����");//TODO�����������������ɫ
        selectStyleBox.addItem("ӣ����");
        selectStyleBox.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 14));
        // ������Ⱦ���Ծ�����ʾ�ı�
        selectStyleBox.setRenderer(new CenteredComboBoxRenderer());
        settingGbc.gridx = 1;
        settingGbc.weightx = 0.0; // ��ť����Ҫ����ռ�
        settingCenterPanel.add(selectStyleBox, settingGbc);
        settingGbc.gridy++;  // �ƶ�����һ��
        selectStyleBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String  selectedItem=(String)selectStyleBox.getSelectedItem();
                if("Ĭ��".equals(selectedItem)) {
                    mainColor = "#e6eef2";
                    viceColor = "#f0f7fa";
                    changeStyle(mainColor,viceColor);
                }
                else if("����".equals(selectedItem)) {
                    mainColor = "#ffffff";
                    viceColor = "#f8f9fa";
                    changeStyle(mainColor,viceColor);
                }
                else if ("ӣ����".equals(selectedItem)) {
                    mainColor = "#f2e1ed";
                    viceColor = "#faf2f7";
                    changeStyle(mainColor,viceColor);
                }
            }
        });

        //Github
        addSettingLabel("Source Code (Github)");
        JButton settingGithubBtn = new JButton("��");
        addSettingBtn(settingGithubBtn);
        settingGithubBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // �� GitHub ��ҳ
                    Desktop.getDesktop().browse(new URI("https://github.com/NgaiYeanCoi/BaiyunUTransfer"));
                } catch (Exception ex) {
                    ex.printStackTrace();
                    new ErrorDialog(mainFrame,ex.getMessage());
                }
            }
        });


        settingPanel.add(settingCenterPanel,BorderLayout.CENTER);
        // �����������ӵ���Ƭ�����
        centerPanel.add(settingPanel, "setting");
        //centerPanel��ӵ��������
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        //�������ӵ����Ӵ���
        mainFrame.add(mainPanel);

        // ��Ӱ�ť�����¼�
        leftSendBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(centerPanel, "send");
                currentCard = "send";
            }
        });

        leftReceiveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(centerPanel, "receive");
                currentCard = "receive";
            }
        });

        leftSettingBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(centerPanel, "setting");
                currentCard = "setting";
            }
        });

        mainFrame.setVisible(true);
    }
    private void addSettingLabel(String labelName){
        JLabel label = new JLabel(labelName);
        label.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
        settingGbc.gridx = 0;
        settingGbc.weightx = 1.0; // �ñ�ǩռ�ݸ���ռ�
        settingCenterPanel.add(label, settingGbc);
    }
    private  void addSettingBtn(JButton button){
        settingGbc.gridx = 1;
        settingGbc.weightx = 0.0; // ��ť����Ҫ����ռ�
        settingCenterPanel.add(button, settingGbc);
        settingGbc.gridy++;  // �ƶ�����һ��

    }
    private void applyPortPattern(JTextField textField) {
        /* *
         * �˿ں�����������ƥ����
         * */
        // ����һ���˿ںŵĲ�������������ʽ
        String portPattern = "^\\d{1,5}$";

        // ����һ��DocumentFilter����������
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

        // ��ȡtextField��Document������DocumentFilter
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(filter);
    }
    private void applyIPSegmentPattern(JTextField textField) {
        /* *
         * IPv4�ε�������ƥ��
         * */
        String ipSegmentPattern = "^(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$";

        // ����һ��DocumentFilter����������
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

        // ��ȡtextField��Document������DocumentFilter
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(filter);
    }
    private void changeStyle(String mainColor,String viceColor){
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

    }

    static class CenteredComboBoxRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            // ���ø��෽����ȡ���
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            // �����ı�����
            label.setHorizontalAlignment(SwingConstants.CENTER);

            return label;
        }
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
}