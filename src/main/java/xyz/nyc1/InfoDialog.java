package xyz.nyc1;

import javax.swing.*;
import java.awt.*;

public class InfoDialog {
    /**
     * 显示错误对话框
     *
     * @param parentComponent 父组件，用于确定弹窗的位置
     * @param infoMessage    提示信息
     */
    public InfoDialog(Component parentComponent, String infoMessage) {
        JOptionPane.showMessageDialog(parentComponent,infoMessage, "提示", JOptionPane.INFORMATION_MESSAGE);
    }
}
