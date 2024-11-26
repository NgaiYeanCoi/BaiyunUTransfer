package xyz.nyc1;

import javax.swing.*;
import java.awt.*;
/**
 * @author NgaiYeanCoi,canyie,Aasling
 * */

public class ErrorDialog {
    /**
     * 显示错误对话框
     *
     * @param parentComponent 父组件，用于确定弹窗的位置
     * @param errorMessage    提示错误信息
     */
    public ErrorDialog(Component parentComponent,String errorMessage) {
        JOptionPane.showMessageDialog(parentComponent,errorMessage, "错误！", JOptionPane.ERROR_MESSAGE);
    }
}
