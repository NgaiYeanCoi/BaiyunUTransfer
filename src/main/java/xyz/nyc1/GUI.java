package xyz.nyc1;

import xyz.nyc1.backend.Callback;
import xyz.nyc1.backend.Client;
import xyz.nyc1.backend.Request;
import xyz.nyc1.backend.TransferPoint;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * @author NgaiYeanCoi,canyie,Aasling
 * */



public class GUI extends JFrame {
    public static void creatMainGUI(){
    // ����������
        JFrame mainFrame = new JFrame("BaiyunUTransfer");
        //mainFrame.setDefaultLookAndFeelDecorated(true);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // ���ô��ڹرղ���
        mainFrame.setSize(400, 300);
        mainFrame.setResizable(false); // ��ֹ���ڵ�����С
        mainFrame.getContentPane().setBackground(new Color(255, 255, 255)); // ���ô��ڵı�����ɫ��ɫɫ����
        mainFrame.addComponentListener(new ComponentAdapter() { // ʹ����ʼ�ձ��־���
            @Override
            public void componentResized(ComponentEvent e) {
                mainFrame.setLocationRelativeTo(null);
            }
        });

        //-- ����mainFrame����
        var mainFrameContentPane = mainFrame.getContentPane();
            mainFrameContentPane.setLayout(null);   // ʹ�þ��Բ���


        mainFrame.setBackground(Color.white);

        // ������ǩ
        JLabel label1 = new JLabel("��ѡ�����ģʽ");
        label1.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 20));
        mainFrame.getContentPane().add(label1);
        label1.setBounds(125, 30, 150, 40);




        //---- serverBtn ----
        JButton serverBtn = new JButton("���ǵ�һ̨�豸");
        mainFrameContentPane.add(serverBtn);
        serverBtn.setBounds(new Rectangle(new Point(135, 100), serverBtn.getPreferredSize()));
        //serverBtn����¼�
        serverBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //

                System.out.println("server");
            }
        });


        //---- clientBtn ----
        JButton clientBtn = new JButton("���ǵڶ�̨�豸");
        mainFrameContentPane.add(clientBtn);
        clientBtn.setBounds(new Rectangle(new Point(135, 155), clientBtn.getPreferredSize()));
        //clientBtn����¼�
        clientBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //
                mainFrame.dispose();
                new ClientUI();
                System.out.println("client");
            }
        });




        // ���ò��ֹ�����Ϊnull���Ա�ʹ�þ��Զ�λ
        mainFrame.setLayout(null);

        // ��ʾ����
        mainFrame.setVisible(true); // ʹ���ڿɼ�
        mainFrame.setLocationRelativeTo(null);


}

}
