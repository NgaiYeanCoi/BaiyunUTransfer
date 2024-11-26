/*
 * Created by JFormDesigner on Tue Nov 26 11:32:38 CST 2024
 */

package xyz.nyc1;

import java.awt.*;
import javax.swing.*;

/**
 * @author 22320
 */
public class ClientInterface extends JFrame {
    public ClientInterface() {
        initComponents();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        panel1 = new JPanel();
        panel2 = new JPanel();
        panel6 = new JPanel();
        panel5 = new JPanel();
        center = new JPanel();
        label2 = new JLabel();
        connectInfo = new JPanel();
        connectPortLabel = new JLabel();
        TextFieldPort = new JTextField();
        connectIPaddressLabel = new JLabel();
        textField1 = new JTextField();
        button2 = new JButton();
        button1 = new JButton();

        //======== this ========
        setTitle("\u4e91\u79fb\u5ba2\u6237\u7aef");
        setMinimumSize(new Dimension(400, 300));
        var contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //======== panel1 ========
        {
            panel1.setLayout(new BorderLayout());
        }
        contentPane.add(panel1, BorderLayout.NORTH);

        //======== panel2 ========
        {
            panel2.setLayout(new BorderLayout());
        }
        contentPane.add(panel2, BorderLayout.WEST);

        //======== panel6 ========
        {
            panel6.setLayout(new BorderLayout());
        }
        contentPane.add(panel6, BorderLayout.EAST);

        //======== panel5 ========
        {
            panel5.setLayout(new BorderLayout());
        }
        contentPane.add(panel5, BorderLayout.SOUTH);

        //======== center ========
        {
            center.setLayout(new BorderLayout(5, 5));

            //---- label2 ----
            label2.setText("\u8fde\u63a5\u4fe1\u606f");
            label2.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
            label2.setAlignmentY(1.5F);
            label2.setAlignmentX(1.0F);
            label2.setIconTextGap(5);
            center.add(label2, BorderLayout.NORTH);

            //======== connectInfo ========
            {
                connectInfo.setMinimumSize(new Dimension(800, 44));
                connectInfo.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 15));
                ((FlowLayout)connectInfo.getLayout()).setAlignOnBaseline(true);

                //---- connectPortLabel ----
                connectPortLabel.setText("\u7aef\u53e3\uff1a");
                connectInfo.add(connectPortLabel);

                //---- TextFieldPort ----
                TextFieldPort.setMinimumSize(new Dimension(200, 34));
                connectInfo.add(TextFieldPort);

                //---- connectIPaddressLabel ----
                connectIPaddressLabel.setText("IP\u5730\u5740:");
                connectInfo.add(connectIPaddressLabel);
                connectInfo.add(textField1);

                //---- button2 ----
                button2.setText("text");
                button2.setMaximumSize(new Dimension(60, 34));
                connectInfo.add(button2);

                //---- button1 ----
                button1.setText("text");
                connectInfo.add(button1);
            }
            center.add(connectInfo, BorderLayout.CENTER);
        }
        contentPane.add(center, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JPanel panel1;
    private JPanel panel2;
    private JPanel panel6;
    private JPanel panel5;
    private JPanel center;
    private JLabel label2;
    private JPanel connectInfo;
    private JLabel connectPortLabel;
    private JTextField TextFieldPort;
    private JLabel connectIPaddressLabel;
    private JTextField textField1;
    private JButton button2;
    private JButton button1;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
