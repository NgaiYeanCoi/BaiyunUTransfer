package xyz.nyc1;
/**
 * @author NgaiYeanCoi,canyie,Aasling
 * */

public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                GUI.creatGUI();
            }
        });
    }

}