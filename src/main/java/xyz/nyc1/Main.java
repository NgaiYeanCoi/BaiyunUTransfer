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
                //GUI.creatMainGUI(); 暂时弃用，但保留
                new ClientUI();
                //receiveRequestUI.DemoTest();
            }
        });
    }

}