package main;

import main.GUI.MainFrame;

import javax.swing.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileInputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

/**
 * User: Vorobyev Alexandr
 * Date: 24.08.11
 * Time: 20:21
 */
public class Main {
    public static final String VERSION = "1.12";
    public static void main(String[] args) throws InvocationTargetException, InterruptedException {

        GlobalProperties.loadProperties();
        final MainFrame frame = MainFrame.getInstance();
        frame.setTitle("JoystickToPPM v"+VERSION);
        frame.addWindowListener( new WindowListener() {
            public void windowOpened(WindowEvent e) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public void windowClosing(WindowEvent e) {
                GlobalProperties.saveProperties();
            }

            public void windowClosed(WindowEvent e) {

            }

            public void windowIconified(WindowEvent e) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public void windowDeiconified(WindowEvent e) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public void windowActivated(WindowEvent e) {
                //To change body of implemented methods use File | Settings | File Templates.
            }

            public void windowDeactivated(WindowEvent e) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        });
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                frame.setVisible(true);
            }
        });

    }


}
