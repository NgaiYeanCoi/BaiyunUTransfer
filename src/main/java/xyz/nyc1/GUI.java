package xyz.nyc1;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

/**
 * @author NgaiYeanCoi,canyie,Aasling
 * */



public class GUI extends JFrame {
    public static void creatGUI(){
    //创建主窗口
    JFrame mainFrame = new JFrame("云移");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //设置窗口关闭操作
        mainFrame.setSize(1150, 720);
        mainFrame.addComponentListener(new ComponentAdapter() { //使窗口始终保持居中
            @Override
            public void componentResized(ComponentEvent e) {
                mainFrame.setLocationRelativeTo(null);
            }
        });
        mainFrame.setBackground(Color.white);

        // 创建按钮
        JButton serverBtn = new JButton("服务端");
        JButton clientBtn = new JButton("客户端");

        // 设置按钮位置和大小
        serverBtn.setBounds(10, 10, 100, 50);
        clientBtn.setBounds(170, 10, 100, 50);

        // 设置按钮背景和前景颜色
        serverBtn.setBackground(new Color(0, 123, 255));
        serverBtn.setForeground(Color.WHITE);
        clientBtn.setBackground(new Color(40, 167, 69));
        clientBtn.setForeground(Color.WHITE);

        //设置按钮字体
        Font mainButton = new Font("Microsoft Yahei", Font.BOLD, 20);
        serverBtn.setFont(mainButton);
        clientBtn.setFont(mainButton);

        // 设置按钮边框
        serverBtn.setBorder(BorderFactory.createEmptyBorder());
        clientBtn.setBorder(BorderFactory.createEmptyBorder());




        //添加按钮到窗口
        mainFrame.getContentPane().add(serverBtn);
        mainFrame.getContentPane().add(clientBtn);


        // 设置布局管理器为null，以便使用绝对定位
        mainFrame.setLayout(null);


    //显示窗口
        mainFrame.setVisible(true); //使窗口可见
        mainFrame.setLocationRelativeTo(null);
}

}
