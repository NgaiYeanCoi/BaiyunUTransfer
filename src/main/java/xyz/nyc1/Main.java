package xyz.nyc1;
/**
 * @author NgaiYeanCoi,canyie,Aasling
 * */

public class Main {
    public static void main(String[] args) {
         //System.setProperty("file.encoding", "UTF-8");

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                mainGui main = new mainGui();
                main.setVisible(true);
            }
        });
    }

}