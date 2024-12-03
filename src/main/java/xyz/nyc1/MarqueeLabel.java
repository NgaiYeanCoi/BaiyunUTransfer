package xyz.nyc1;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author NgaiYeanCoi
 */

public class MarqueeLabel extends JLabel {
    private static final int RATE = 150; // 滚动速度（毫秒）
    private static final int THRESHOLD = 50; // 阈值，超过这个长度才开始滚动
    private final String fixedPrefix = "当前路径为："; // 固定文本
    private String marqueeText;
    private int position;
    private Timer timer;

    public MarqueeLabel(String text) {
        super(text);
        this.marqueeText = text;
        this.position = 0;
        setHorizontalAlignment(SwingConstants.LEFT);
        startMarquee();
    }

    private void startMarquee() {
        if (marqueeText.length() > THRESHOLD) {
            if (timer != null && timer.isRunning()) {
                timer.stop();
            }
            timer = new Timer(RATE, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    position++;
                    if (position > marqueeText.length()) {
                        position = 0;
                    }
                    updateText();
                }
            });
            timer.start();
        } else {
            // 如果文本长度不超过阈值，直接显示文本，不启动滚动
            setText(fixedPrefix + marqueeText);
        }
    }

    private void updateText() {
        setText(fixedPrefix +marqueeText.substring(position) +"   "+marqueeText.substring(0, position));
    }

    public void setMarqueeText(String text) {
        this.marqueeText = text;
        this.position = 0;
        startMarquee(); // 重新检查是否需要启动或停止滚动
    }
}
