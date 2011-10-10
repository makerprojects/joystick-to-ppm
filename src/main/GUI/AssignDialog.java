package main.GUI;

import main.GUI.controller.ComponentConfig;
import net.java.games.input.Component;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Alexandr Vorobiev
 */
public class AssignDialog extends JDialog implements ActionListener{
    private final JComboBox channels;
    private final JButton okBtn = new JButton("Ok");
    private final ComponentConfig component;
    public AssignDialog(ComponentConfig component) {
        super(MainFrame.getInstance(), true);
        channels = new JComboBox();
        okBtn.addActionListener(this);
        this.component = component;
        for (int i = 1; i <= DeviceConfigPanel.SERVO_COUNT; i++) {
            if (!DeviceConfigPanel.getAssignMap().containsValue(i))
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
        gbc.gridx = 1;
        add(okBtn,gbc);

        setPreferredSize(new Dimension(250, 110));
        setSize(getPreferredSize());
    }

    public void actionPerformed(ActionEvent e) {
        DeviceConfigPanel.addMapping(component, (Integer)channels.getSelectedItem() );
        setVisible(false);
    }
}
