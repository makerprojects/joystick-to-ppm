package main.GUI;

import main.GUI.event.ControllerAddListener;
import main.GUI.controller.ControllerPanel;
import main.GUI.tabs.ButtonTabComponent;
import main.controller.ControllerUtils;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.ControllerEvent;
import net.java.games.input.ControllerListener;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: Vorobyev ALexandr
 * Date: 24.08.11
 * Time: 20:40
 */
public class MainFrame extends JFrame implements ControllerListener, ControllerAddListener{
    private final JTabbedPane tabs = new JTabbedPane();
    private final DeviceConfigPanel deviceConfigPanel = new DeviceConfigPanel();
    private final ControllerEnvironment ce = ControllerEnvironment.getDefaultEnvironment();
    private final List<Controller> controllerList;
    private static final MainFrame INSTANCE = new MainFrame();
    public static MainFrame getInstance() {
        return INSTANCE;
    }
    protected MainFrame() {
        controllerList = new ArrayList<Controller>(Arrays.asList(ce.getControllers()));
        createLayout();
        ce.addControllerListener(this);
    }


    private void createLayout() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(700, 600));
        setSize(getPreferredSize());
        tabs.setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);
        tabs.addTab("Device config", deviceConfigPanel);
        for (Controller controller: controllerList)
            controllerChoosed(controller);
        add(tabs, BorderLayout.CENTER);

    }

    private void initTabComponent(int i) {
        //tabs.setTabComponentAt(i,
        //         new ButtonTabComponent(tabs));
        //tabs.setSelectedIndex(i);
    }
    public void controllerRemoved(ControllerEvent ev) {
        if (ControllerUtils.isValidController(ev.getController())) {
            controllerList.remove(ev.getController()) ;
        }
    }

    public void controllerAdded(ControllerEvent ev) {
        if (ControllerUtils.isValidController(ev.getController())) {
            controllerList.add(ev.getController());
            controllerChoosed(ev.getController());
        }
    }

    public void controllerChoosed(Controller c) {
        if (!ControllerUtils.isValidController(c))
            return;
        for(int i =1; i < tabs.getTabCount(); i++) {
            if (((ControllerPanel)tabs.getComponentAt(i)).getController() == c) {
                return;
            }
        }
        tabs.addTab(c.getName(), new ControllerPanel(c));
        initTabComponent(tabs.getTabCount() - 1);
    }
}
