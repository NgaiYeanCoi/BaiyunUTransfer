package xyz.nyc1;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * @author NgaiYeanCoi
 */


public class MarqueeLabel extends JLabel {
    private static final int RATE = 120; // 滚动速度（毫秒）
    private String fixedPrefix = "当前路径为：";
    private String marqueeText;
    private int position;

    public MarqueeLabel(String text) {
        super(text);
        this.marqueeText = text;
        this.position = 0;
        setHorizontalAlignment(SwingConstants.LEFT);
        startMarquee();
    }

    private void startMarquee() {
        Timer timer = new Timer(RATE, new ActionListener() {
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
    }

    private void updateText() {
        setText(fixedPrefix + marqueeText.substring(position) + marqueeText.substring(0, position));
    }

    public void setMarqueeText(String text) {
        this.marqueeText = text;
        this.position = 0;
        updateText();
    }
}