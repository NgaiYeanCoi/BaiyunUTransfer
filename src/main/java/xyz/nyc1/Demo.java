package xyz.nyc1;


import javax.swing.*;
import java.awt.*;

public class Demo {
    static JFrame mainFrame = new JFrame("test");
    private  JButton receiveAcceptBtn = new JButton("����");
    private JButton receiveDeclineBtn = new JButton("�ܾ�");

    public static void DemoTest() {

        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // ���ô��ڹرղ���
        mainFrame.setSize(400, 300);
        mainFrame.setResizable(false); // ��ֹ���ڵ�����С
        mainFrame.setLocationRelativeTo(null);
        mainFrame.getContentPane().setBackground(new Color(255, 255, 255)); // ���ô��ڵı�����ɫΪ��ɫ



        //

        // ����ģ̬�Ի���
        JDialog receiveFileRequestDialog = new JDialog(mainFrame, "�����ļ�", true); // ����Ϊģ̬
        receiveFileRequestDialog.setSize(600, 400);
        receiveFileRequestDialog.setLocationRelativeTo(null); // ������ʾ
        receiveFileRequestDialog.setResizable(false); // ��ֹ������С
        receiveFileRequestDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE); // ���ùرղ���
        receiveFileRequestDialog.setLayout(new BorderLayout()); // ���ò���
        receiveFileRequestDialog.setBackground(new Color(255, 255, 255));

        String ipPort="127.0.0.1:10086";
        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(new Color(255, 255, 255));
        centerPanel.add(receiveFileRequestDialog);
        centerPanel.setLayout(new BorderLayout());
        JLabel centerInfo = new JLabel();
        centerInfo.setText(ipPort+"\n��Ҫ������㷢��һ���ļ�");
        centerPanel.add(centerInfo, BorderLayout.CENTER);


        //��ť�ؼ�
            JButton receiveAcceptBtn = new JButton("����");
            JButton receiveDeclineBtn = new JButton("�ܾ�");
            JPanel bottomPanel = new JPanel();
            bottomPanel.setLayout(new FlowLayout());
            bottomPanel.add(receiveAcceptBtn);
            bottomPanel.add(receiveDeclineBtn);
            receiveFileRequestDialog.getContentPane().add(bottomPanel, BorderLayout.SOUTH);


        // ��ʾ�����
        mainFrame.setVisible(true);

        // ��ʾ�Ի���
        receiveFileRequestDialog.setVisible(true);


    }
}
