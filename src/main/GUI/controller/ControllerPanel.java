package main.GUI.controller;

import net.java.games.input.*;
import net.java.games.input.Component;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Alexandr Vorobiev
 */
public class ControllerPanel extends JPanel{
    private final Controller controller;
    private final JLabel deviceName;
    //private final JComboBox deviceComponents;
    private final JPanel axisPanel = new JPanel();
    private final JPanel POVPanel = new JPanel();
    private final JPanel leftPanel = new JPanel(new GridLayout(0,1));
    private final JPanel centralPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
    private final Thread thread;
    public ControllerPanel(final Controller controller) {
        this.controller = controller;
        deviceName = new JLabel(controller.getName());
        //deviceComponents = new JComboBox(controller.getComponents());
        createLayout();
        thread = new Thread( new Runnable() {
            public void run() {
                while (!Thread.interrupted()) {
                    if (isShowing()) {

                        try {
                            SwingUtilities.invokeAndWait(new Runnable() {
                                public void run() {
                                    synchronized (controller) {
                                        poll();
                                    }
                                }
                            });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        thread.start();

    }

    private void createLayout() {
        setLayout(new BorderLayout());
        add(deviceName, BorderLayout.NORTH);
        add(leftPanel, BorderLayout.WEST);
        add(centralPanel, BorderLayout.CENTER);
        axisPanel.setLayout(new GridBagLayout());
        //add(deviceComponents);
		makeController(controller);
    }

	private void makeController(Controller ca) {
        Component[] components = ca.getComponents();
        if (components.length>0) {
          //  int width = (int)Math.ceil(Math.sqrt(components.length));
         //   JPanel p = new JPanel();
          //  p.setLayout(new GridLayout(width,0));
            for (Component component : components) {
                addAxis(component);
            }
            if (axisPanel.getComponentCount() > 0) {
                GridBagConstraints gbc =new GridBagConstraints();
                gbc.fill = GridBagConstraints.BOTH;
                gbc.gridheight = GridBagConstraints.REMAINDER;
                gbc.weightx = 1;
                gbc.weighty = 160;

                axisPanel.add(new JPanel(), gbc);
                leftPanel.add(axisPanel);
            }
            if (POVPanel.getComponentCount() > 0)
                leftPanel.add(POVPanel);
            //c.add(new JScrollPane(p),BorderLayout.CENTER);
        }
	}
    private void addAxis(Component ax){
        if (ax.isAnalog()) {
            addToAxisPanel(ax);
        } else {
            if (ax.getIdentifier() == Component.Identifier.Axis.POV) {
                addToPOVPanel(ax);
            } else {
                addToButtonPanel(ax);
            }
        }
    }

    private void addToButtonPanel(Component ax) {
        centralPanel.add(new ButtonPanel(controller,ax));
    }
    private void addToPOVPanel(Component ax) {
        POVPanel.add(new POVPanel(controller,ax));
    }
    private void addToAxisPanel(Component ax) {
        GridBagConstraints gbc =new GridBagConstraints();
        gbc.insets = new Insets(0,0,5,5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx=0;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.NONE;

		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx = 1;
        axisPanel.add(new AnalogAxisPanel(controller,ax), gbc);
    }
    public Controller getController() {
        return controller;
    }
	public void poll(){
			if (controller.poll()) {
			//System.out.println("Polled "+ca.getName());
			for(int i = 0; i < axisPanel.getComponentCount() - 1; i++){
				try {
					((AnalogAxisPanel)(axisPanel.getComponent(i))).poll();
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
            for(int i = 0; i < POVPanel.getComponentCount(); i++){
                try {
                    ((POVPanel)(POVPanel.getComponent(i))).poll();
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
            for(int i = 0; i < centralPanel.getComponentCount(); i++){
                try {
                    ((ButtonPanel)(centralPanel.getComponent(i))).poll();
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
		}
	}
}
