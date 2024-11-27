package xyz.nyc1;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class Demo {
    static JFrame mainFrame = new JFrame("test");


    public static void DemoTest() {

        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // ���ô��ڹرղ���
        mainFrame.setSize(400, 300);
        mainFrame.setResizable(false); // ��ֹ���ڵ�����С
        mainFrame.setLocationRelativeTo(null);
        mainFrame.getContentPane().setBackground(new Color(255, 255, 255)); // ���ô��ڵı�����ɫΪ��ɫ




        // ����ģ̬�Ի���
        JDialog receiveFileRequestDialog = new JDialog(mainFrame, "�����ļ�", true); // ����Ϊģ̬
        receiveFileRequestDialog.setSize(600, 400);
        receiveFileRequestDialog.setLocationRelativeTo(null); // ������ʾ
        receiveFileRequestDialog.setResizable(false); // ��ֹ������С
        receiveFileRequestDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE); // ���ùرղ���
        receiveFileRequestDialog.setLayout(new BorderLayout()); // ���ò���
        receiveFileRequestDialog.setBackground(new Color(255, 255, 255));

        //������Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(new Color(255, 255, 255));
        mainPanel.setLayout(new BorderLayout());


        //center���
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(new Color(255, 255, 255));
        JLabel receiveInfoLabel = new JLabel();
        centerPanel.add(receiveInfoLabel);
        centerPanel.add(receiveInfoLabel);
        receiveInfoLabel.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 30));
        String ipPort ;
        ipPort="127.0.0.1:10086";
        receiveInfoLabel.setText(ipPort+"��Ҫ���͸���һ���ļ�");



        //��ť�ؼ�
            JButton receiveAcceptBtn = new JButton("����");
            JButton receiveDeclineBtn = new JButton("�ܾ�");
        //�ײ����
            JPanel bottomPanel = new JPanel();
            bottomPanel.setLayout(new FlowLayout());
            bottomPanel.add(receiveAcceptBtn);
            bottomPanel.add(receiveDeclineBtn);
        //��ӽ������
            mainPanel.add(centerPanel, BorderLayout.CENTER);
            mainPanel.add(bottomPanel, BorderLayout.SOUTH);
            receiveFileRequestDialog.add(mainPanel);

        // ��ʾ�����
        mainFrame.setVisible(true);

        // ��ʾ�Ի���
        receiveFileRequestDialog.setVisible(true);


    }
}
