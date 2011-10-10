package main.GUI;

import javax.swing.*;

/**
 * @author Alexandr Vorobiev
 */
public class SignalConfigPanel extends JPanel{
    private final JLabel signalNumLabel;
    public SignalConfigPanel(int signalNum) {
        signalNumLabel = new JLabel("" + signalNum);

    }
}
