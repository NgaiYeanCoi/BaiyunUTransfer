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
 * @author NgaiYeanCoi
 * */

public class ClientUI implements Callback {
    /**
     * �ͻ���UI����
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
    private boolean globalSendFunctionDetection;
    private boolean globalSettingFunctionDetection = false;

    public ClientUI() {
        createUI();
    }

    private void sendFunction() {

    }

    private void createUI() {
        globalSendFunctionDetection = true;//����Ĭ��ֵ
        // ����������
        mainFrame = new JFrame("BaiyunUTransferClient");
        mainFrame.setSize(900, 600);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLocationRelativeTo(null);
        //mainFrame.setResizable(false);
        mainFrame.getContentPane().setBackground(new Color(255, 255, 255)); // ���ô��ڵı�����ɫ��ɫ����

        // ���������
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        /*������������*/
            JPanel leftPanel = new JPanel();
            leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS)); // ����Ϊ��ֱ����
            leftPanel.setPreferredSize(new Dimension(257, 0)); // ����������Ŀ��
            leftPanel.add(Box.createVerticalStrut(10));
            //����logoLabel�ؼ�
            JLabel rightLogo = new JLabel("BaiyunUTransfer");
            rightLogo.setFont(new Font("Microsoft YaHei UI", Font.BOLD, 25));
            rightLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
            leftPanel.add(rightLogo);
            leftPanel.add(Box.createVerticalStrut(40)); //���ô�ֱ���
        //�������Ͱ�ť�ؼ�
        JButton rightSendBtn = new JButton("����");
        rightSendBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(rightSendBtn);
        leftPanel.add(Box.createVerticalStrut(45));
        //���ͼ����¼�
        rightSendBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO:������UI����
                System.out.println("����" + globalSendFunctionDetection);
                if (!globalSendFunctionDetection) {
                    sendFunction();
                } else {
                    //TODO:������UI����
                }
            }
        });
            //�������հ�ť���
            JButton rightReceiveBtn = new JButton("����");
            rightReceiveBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
            leftPanel.add(rightReceiveBtn);
            leftPanel.add(Box.createVerticalStrut(45)); //���ô�ֱ���
            //���ռ����¼�
            rightReceiveBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //TODO:������UI����
                    System.out.println("����");
                }
            });

        //�������ð�ť
        JButton rightSettingBtn = new JButton("����");
        rightSettingBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(rightSettingBtn);
        //���ü����¼�
        rightSettingBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO:������UI����
                System.out.println("����");
                if (globalSettingFunctionDetection) {

                }

            }
        });

        // ���������ӵ����������
        mainPanel.add(leftPanel, BorderLayout.WEST);


        /*�����������*/
            JPanel topPanel = new JPanel();
            topPanel.setLayout(new FlowLayout());

            // ����IPV4�ؼ�
            JLabel ipLabel = new JLabel("IP��ַ:");
            ipLabel.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 15));
            ipLabel.setBorder(BorderFactory.createEmptyBorder(0,5,0,0));
            ipSegment1 = new JTextField(3);
            ipSegment2 = new JTextField(3);
            ipSegment3 = new JTextField(3);
            ipSegment4 = new JTextField(3);

            // ����applyIPSegmentPattern�������
            applyIPSegmentPattern(ipSegment1);
            applyIPSegmentPattern(ipSegment2);
            applyIPSegmentPattern(ipSegment3);
            applyIPSegmentPattern(ipSegment4);

            // �����˿�Label���
            JLabel portLabel = new JLabel("�˿�:");
            portLabel.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 15));
            portTextField = new JTextField(5);
            applyPortPattern(portTextField);
            portLabel.setBorder(BorderFactory.createEmptyBorder(0,10,0,0));//����portLabel�ı߿�

            // ������ť
            connectButton = new JButton("����");
            disconnectButton = new JButton("�Ͽ�����");
            selectFileButton = new JButton("ѡ�����ļ�");
            sendFileButton = new JButton("ȷ�Ϸ���");
            cancelSelectedButton = new JButton("ȡ��ѡ��");

            /*���������������*/
                // IPv4�������
                topPanel.add(ipLabel);
                topPanel.add(ipSegment1);
                topPanel.add(new JLabel("."));
                topPanel.add(ipSegment2);
                topPanel.add(new JLabel("."));
                topPanel.add(ipSegment3);
                topPanel.add(new JLabel("."));
                topPanel.add(ipSegment4);
                // �˿��������
                topPanel.add(portLabel);
                topPanel.add(portTextField);
                // ��ť���
                topPanel.add(connectButton);
                topPanel.add(disconnectButton);

                    //����topPanel�ı߿�
                    topPanel.setBorder(BorderFactory.createEmptyBorder(0, 50, 0, 0));
                    // �����嵽�����
                    mainPanel.add(topPanel, BorderLayout.NORTH);

        // ������־�ı�����
        logTextArea = new JTextArea();
        logTextArea.setEditable(false);
        JScrollPane centerScrollPane = new JScrollPane(logTextArea);
            //�����嵽�����
            mainPanel.add(centerScrollPane, BorderLayout.CENTER);

        /*�����ײ����*/
            JPanel bottomPanel = new JPanel();
            bottomPanel.setLayout(new FlowLayout());
            bottomPanel.add(selectFileButton);
            bottomPanel.add(sendFileButton);
            bottomPanel.add(cancelSelectedButton);

            // ��ʼΪ���ɼ�
            sendFileButton.setVisible(false);
            selectFileButton.setVisible(false);
            disconnectButton.setVisible(false);
            cancelSelectedButton.setVisible(false);
                //����bottomPanel�ı߿�
                bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 220, 0, 0));
                //�����嵽�����
                mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // �������嵽����
        mainFrame.add(mainPanel);


        // ������Ӱ�ť�¼�������
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String ip = ipSegment1.getText() + "." + ipSegment2.getText() + "." + ipSegment3.getText() + "." + ipSegment4.getText();
                String port = portTextField.getText();
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
                else {
                    // TODO:������ӷ������Ĵ��룬��Ҫ���Ӻ��API


                    logTextArea.append("�������ӵ� " + ip + ":" + port + "��...\n");
                    logTextArea.append("�ѳɹ����ӣ�\n");
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
                // TODO:�Ͽ����ӣ���Ҫ���api
                logTextArea.append(ip + ":" + port + " �ѶϿ�����\n");
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
            JFileChooser fileChooser = new JFileChooser();


                // �����Ϸ�Ŀ��
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
                                    logTextArea.append("��ѡ���ļ� " + globalFilePath + "\n");
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
                    int result = fileChooser.showOpenDialog(selectFileDialog);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        globalFilePath = fileChooser.getSelectedFile().getAbsolutePath();
                        logTextArea.append("��ѡ���ļ�: " + globalFilePath + "\n");
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
                logTextArea.append("�����ļ���...\n");
                logTextArea.append("���ͳɹ���\n");
            }
        });


        cancelSelectedButton.addActionListener(new ActionListener() {
            /**
             *  "ȡ��ѡ��"��ť�����¼�
             * */
            @Override
            public void actionPerformed(ActionEvent e) {
                logTextArea.append("��ȡ��ѡȡ�ļ�" + globalFilePath + "\n");
                globalFilePath = null;
                selectFileButton.setVisible(true);
                sendFileButton.setVisible(false);
                cancelSelectedButton.setVisible(false);
            }
        });

        // ��ʾ����
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


}
