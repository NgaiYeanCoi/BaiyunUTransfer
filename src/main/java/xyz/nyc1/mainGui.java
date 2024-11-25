/*
 * Created by JFormDesigner on Mon Nov 25 23:22:17 CST 2024
 */

package xyz.nyc1;

import java.awt.*;
import javax.swing.*;

/**
 * @author NgaiYeanCoi
 */
public class mainGui extends JFrame {
    public mainGui() {
        initComponents();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        label1 = new JLabel();

        //======== this ========
        setMinimumSize(new Dimension(480, 380));
        var contentPane = getContentPane();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

        //---- label1 ----
        label1.setText("text");
        contentPane.add(label1);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JLabel label1;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
