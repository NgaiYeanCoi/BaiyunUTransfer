package xyz.nyc1;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author NgaiYeanCoi
 * */

public class ImageReset {

    static Image resizeImage(Image originalImage, int targetWidth, int targetHeight) {
        /**
         * 缩放图片到指定大小
         * @param originalImage 原始图片
         * @param targetWidth 目标宽度
         * @param targetHeight 目标高度
         * @return 缩放后的图片
         */
        BufferedImage resizedImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resizedImage.createGraphics();

        // 设置渲染提示以获得更好的图像质量
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 绘制缩放后的图像
        g2d.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
        g2d.dispose();

        return resizedImage;
    }
    static ImageIcon createRolloverIcon(ImageIcon originalIcon, float alpha) {
        /**
         * 创建悬停图标
         * @param originalIcon 原始图标
         * @param alpha 透明度值
         * @return 悬停时的图标
         */
        Image image = originalIcon.getImage();// 获取原始图片
        BufferedImage bufferedImage = new BufferedImage(
                originalIcon.getIconWidth(),
                originalIcon.getIconHeight(),
                BufferedImage.TYPE_INT_ARGB
        );

        // 创建 Graphics2D 对象
        Graphics2D g2d = bufferedImage.createGraphics();

        // 设置透明度
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

        // 绘制原始图片
        g2d.drawImage(image, 0, 0, null);
        g2d.dispose();
        // 创建并返回新的 ImageIcon
        return new ImageIcon(bufferedImage);
    }
}
