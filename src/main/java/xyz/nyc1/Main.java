package xyz.nyc1;

import javax.swing.*;

/**
 * @author NgaiYeanCoi,canyie,Aasling
 * */

public class Main {
    public static void main(String[] args) {
         //System.setProperty("file.encoding", "UTF-8");
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    new MainUI();
                } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException |
                         IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
                //ReceiveRequestUI.DemoTest();

            }
        });
    }

}