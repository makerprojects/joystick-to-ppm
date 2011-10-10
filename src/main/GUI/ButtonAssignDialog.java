package main.GUI;

import main.GUI.controller.ComponentConfig;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

/**
 * @author Alexandr Vorobiev
 */
public class ButtonAssignDialog extends JDialog implements ActionListener, AdjustmentListener{
    private final JComboBox channels;
    private final JButton okBtn = new JButton("Ok");
    private final ComponentConfig component;
    private final JScrollBar value = new JScrollBar(JScrollBar.HORIZONTAL, 50, 0, 0, 100);
    private final JTextField field = new JTextField();
    public ButtonAssignDialog(ComponentConfig component) {
        super(MainFrame.getInstance(), true);
        field.setEditable(false);
        field.setMinimumSize(new Dimension(50, 20));
        field.setText(component.getSentValue() + "%");
        channels = new JComboBox();
        value.setValue(component.getSentValue());
        value.addAdjustmentListener(this);
        okBtn.addActionListener(this);
        this.component = component;
        for (int i = 1; i <= DeviceConfigPanel.SERVO_COUNT; i++) {
                channels.addItem(i);
        }
        createLayout();
    }

    private void createLayout() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5,0,5,5);
        gbc.weighty =1;
        gbc.weightx =0;
        add(new JLabel("Choose channel: "),gbc);
        gbc.gridx = 1;
        gbc.weightx =1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        add(channels,gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        add(new JLabel("Choose value: "),gbc);

        gbc.gridx = 1;
        add(field, gbc);

        gbc.gridx = 2;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        add(value, gbc);

        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.gridx = 1;
        add(okBtn,gbc);

        setPreferredSize(new Dimension(260, 150));
        setSize(getPreferredSize());
    }

    public void actionPerformed(ActionEvent e) {
        component.setSentValue(value.getValue());
        DeviceConfigPanel.addMapping(component, (Integer)channels.getSelectedItem() );
        setVisible(false);
    }

    public void adjustmentValueChanged(AdjustmentEvent e) {
        field.setText(e.getValue() + "%");
    }
}
