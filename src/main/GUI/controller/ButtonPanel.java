package main.GUI.controller;


import main.GUI.AssignDialog;
import main.GUI.ButtonAssignDialog;
import main.GUI.DeviceConfigPanel;
import main.GlobalProperties;
import net.java.games.input.Component;
import net.java.games.input.Controller;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * @author Alexandr Vorobiev
 */
public class ButtonPanel extends JPanel implements ActionListener{
    private final Component component;
    private final JButton assignBtn= new JButton("+");
    private final ComponentConfig componentConfig;

    public ButtonPanel(Controller controller,Component component) {
        this.component = component;
        componentConfig = new ComponentConfig(controller,component);
        assignBtn.addActionListener(this);
        assignBtn.setMinimumSize(new Dimension(60,20));
        setPreferredSize(new Dimension(50,50));
        setMaximumSize(getPreferredSize());
        setMinimumSize(getPreferredSize());
        setBorder(LineBorder.createGrayLineBorder());
        setBackground(Color.gray);
        add(new JLabel(component.getName()));
        add(assignBtn);

        Integer value = GlobalProperties.checkChannelForComponent(componentConfig);
        if (value != null ) {

            DeviceConfigPanel.addMapping(componentConfig, value, false);
            value = GlobalProperties.checkChannelSVForComponent(componentConfig);
            if (value != null)
                componentConfig.setSentValue(value);

            assignBtn.setText("-");
        }
        //assignBtn.addActionListener(this);

    }

    public void poll() {
        float data = component.getPollData();
        if (data == 0.0f){
            setBackground(Color.GRAY);
        } else if ( data == 1.0f) {
            setBackground(Color.GREEN);
        }else { // should never happen
            setBackground(Color.red);
        }
    }


    public void actionPerformed(ActionEvent e) {
        if (DeviceConfigPanel.getAssignMap().containsKey(componentConfig)) {
            DeviceConfigPanel.removeMapping(componentConfig);
            assignBtn.setText("+");
        } else {
            (new ButtonAssignDialog(componentConfig)).setVisible(true);
            if (DeviceConfigPanel.getAssignMap().containsKey(componentConfig))
                assignBtn.setText("-");

        }
    }
}
