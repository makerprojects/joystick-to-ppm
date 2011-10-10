package main.GUI.controller;

import main.GUI.AssignDialog;
import main.GUI.DeviceConfigPanel;
import main.GlobalProperties;
import net.java.games.input.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * @author Alexandr Vorobiev
 */
public class AnalogAxisPanel extends JPanel implements ActionListener{
    private final net.java.games.input.Component component;
    private final JProgressBar progressBar;
    private final JTextField minField = new JTextField("-1.0");
    private final JTextField maxField = new JTextField("1.0");
    private final JTextField valueField = new JTextField("0.0");
    private final JButton assignBtn= new JButton("+");
    private float data;
    private final ComponentConfig componentConfig;
    public AnalogAxisPanel(Controller controller,net.java.games.input.Component c) {
        this.component = c;
        progressBar = new JProgressBar(-100, 100);
        createLayout();
        assignBtn.addActionListener(this);
        assignBtn.setMinimumSize(new Dimension(60,20));
        componentConfig = new ComponentConfig(controller,c);
        Integer value = GlobalProperties.checkChannelForComponent(componentConfig);
        if (value != null ) {
            DeviceConfigPanel.addMapping(componentConfig, value, false);
            assignBtn.setText("-");
        }
    }

    private void createLayout() {
        setLayout(new GridBagLayout());
        minField.setEditable(false);
        minField.setMinimumSize(new Dimension(50,20));
        maxField.setEditable(false);
        maxField.setMinimumSize(new Dimension(50,20));
        valueField.setEditable(false);
        valueField.setMinimumSize(new Dimension(50,20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5,0,5,5);
        gbc.weighty =1;
        gbc.weightx =0;
        add(assignBtn, gbc);
        gbc.gridx = 1;
        add(new JLabel(component.getName()), gbc);

        gbc.gridx = 2;
        gbc.weightx =1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        add(progressBar, gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridwidth = 1;
        gbc.gridx = 2;
        add(minField, gbc);
        gbc.gridx = 3;
        add(maxField, gbc);
        gbc.gridx = 4;
        add(valueField, gbc);


        setPreferredSize( new Dimension(200,60));
        setMinimumSize(new Dimension(200,60));
        setMaximumSize(new Dimension(200,60));

    }
    protected void renderData(){
        if (component.getDeadZone() >= Math.abs(data)) {
            progressBar.setValue(0);
            valueField.setText("0.0");
        }
        else {
            componentConfig.updateMinMax(data);
            try {
                float f1 = Float.parseFloat(maxField.getText());
                float f2 = Float.parseFloat(minField.getText());
                if (data > f1)
                    maxField.setText(String.format("%.2f",data));
                if (data < f2)
                        minField.setText(String.format("%.2f",data));

            } catch (Exception ignored) {}
            if ((int)(data * 100.0f) > progressBar.getMaximum()) {

                progressBar.setMaximum((int)((data) * 100.0f));
            }
            if ((int)(data * 100.0f) < progressBar.getMinimum())
                progressBar.setMinimum((int)((data) * 100.0f));

            progressBar.setValue((int)(data*100.0f));
            valueField.setText(String.format("%.2f",data));

        }
    }

    public void poll() {
        data = component.getPollData();
        renderData();
    }


    public void actionPerformed(ActionEvent e) {
        if (DeviceConfigPanel.getAssignMap().containsKey(componentConfig)) {
            DeviceConfigPanel.removeMapping(componentConfig);
            assignBtn.setText("+");
        } else {
            (new AssignDialog(componentConfig)).setVisible(true);
            if (DeviceConfigPanel.getAssignMap().containsKey(componentConfig))
                assignBtn.setText("-");

        }
    }
}
