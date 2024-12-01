package xyz.nyc1;

import javax.swing.*;
import java.awt.*;

public class SetBtnImage {
    public static void setBtnImage(JButton Btn, Image BtnImage, int targetWidth, int targetHeight) {
        //图片按钮样式
        ImageIcon Icon = new ImageIcon(ImageReset.resizeImage(BtnImage, targetWidth, targetHeight));
        ImageIcon receiveBtnRolloverIcon = ImageReset.createRolloverIcon(Icon,0.7f);
        Btn.setIcon(Icon);
        Btn.setBorderPainted(false);
        Btn.setContentAreaFilled(false);
        Btn.setFocusPainted(false);
        Btn.setRolloverIcon(receiveBtnRolloverIcon);
    }
}
