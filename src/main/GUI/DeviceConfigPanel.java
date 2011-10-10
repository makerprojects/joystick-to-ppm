package main.GUI;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import main.GUI.controller.ComponentConfig;
import main.GUI.event.ControllerAddListener;

import main.GlobalProperties;
import main.usb2ppm.ServoParameter;
import main.usb2ppm.Usb2PPMWorker;
import main.usb2ppm.event.DataSentEvent;
import net.java.games.input.*;
import net.java.games.input.Component;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.*;
import java.util.List;

/**
 * User: Vorobyev Alexandr
 * Date: 24.08.11
 * Time: 22:15
 */
public class DeviceConfigPanel extends JPanel implements ActionListener, DataSentEvent, AdjustmentListener{
    public final static int SERVO_COUNT = 10;
    private static final String CONNECT = "Connect";
    private static final String DISCONNECT = "Disconnect";
    private final JComboBox comBox;
    private final JPanel deviceConfigPanel = new JPanel(new GridBagLayout());
    private final JPanel portSettingPanel = new JPanel(new GridBagLayout());
    private final JPanel servoOutputPanel = new JPanel(new GridBagLayout());
    private final JComboBox channelsBox = new JComboBox(new Integer[]{1,2,3,4,5,6,7,8,9,10,11,12,13,14});
    private final JProgressBar[] servoBars = new JProgressBar[SERVO_COUNT];
    private final JRadioButton[] servoInv = new JRadioButton[SERVO_COUNT];
    private final JScrollBar[] servoTrim = new JScrollBar[SERVO_COUNT];
    private final JScrollBar[] servoEPA = new JScrollBar[SERVO_COUNT];
    private final JRadioButton v2 = new JRadioButton("Compufly v2");
    private final JComboBox modeBox = new JComboBox(new String[]{"Negative PPM (Futaba,Hitec,Esky,JR)","Positive PPM"});
    private final JButton connect = new JButton(CONNECT);
    //private Map<Integer, net.java.games.input.Component> =


    private final JButton setModeBtn = new JButton("Set Chanel and Mode");

    private Thread workingThread;
    private Usb2PPMWorker worker;
    private static HashMap<ComponentConfig, Integer> assignMap = new HashMap<ComponentConfig, Integer>();
    private static DeviceConfigPanel INSTANCE  = new DeviceConfigPanel();

    protected DeviceConfigPanel() {
        setModeBtn.addActionListener(this);
        v2.setSelected(Boolean.parseBoolean(GlobalProperties.getProperties().getProperty("V2",Boolean.toString(false))));
        //v2.setVisible(false);
        v2.addActionListener(this);
        comBox = new JComboBox();
        HashSet<CommPortIdentifier> set = getAvailableSerialPorts();
        for (CommPortIdentifier port: set)
            comBox.addItem(port.getName());
        if (comBox.getItemCount() > 0) {
            String selCom = (String)comBox.getSelectedItem();
            selCom = GlobalProperties.getProperties().getProperty("COM",selCom);
            if (!selCom.equals(comBox.getSelectedItem()))
                for (int i = 0; i < comBox.getItemCount(); i++ ) {
                    if (comBox.getItemAt(i).equals(selCom)) {
                        comBox.setSelectedIndex(i);
                        break;
                    }
                }
            GlobalProperties.getProperties().setProperty("COM",(String)comBox.getSelectedItem());
        }
        Integer value = Integer.valueOf(GlobalProperties.getProperties().getProperty("CHANNELS", "1"));
        channelsBox.setSelectedItem(value);
        GlobalProperties.getProperties().setProperty("CHANNELS", value.toString());

        value = Integer.valueOf(GlobalProperties.getProperties().getProperty("PPMTYPE", "0"));
        modeBox.setSelectedIndex(value);
        GlobalProperties.getProperties().setProperty("PPMTYPE", value.toString());


        createLayout();
    }

    public static DeviceConfigPanel getINSTANCE() {
        return INSTANCE;
    }
    public static Map<ComponentConfig, Integer> getAssignMap() {
        return Collections.unmodifiableMap(assignMap);
    }
    public static void addMapping(ComponentConfig component, int channel, boolean updateParams) {
        assignMap.put(component, channel);
        if (updateParams) {
            updateParams();
        }
        INSTANCE.updateMapping();
    }

    private static void updateParams() {
        GlobalProperties.getProperties().setProperty("DC", Integer.toString(assignMap.size()));
        int i = 1;
        for (Map.Entry<ComponentConfig, Integer> entry: assignMap.entrySet()) {
            GlobalProperties.getProperties().setProperty("DEVICE" + i, entry.getKey().toString());
            GlobalProperties.getProperties().setProperty("DCHANNEL" + i, entry.getValue().toString());
            GlobalProperties.getProperties().setProperty("CHANNEL_SV" + i, entry.getKey().getSentValue() + "");
            i++;
        }
    }

    public static void addMapping(ComponentConfig component, int channel){
        addMapping(component,channel, true);
    }
    public static void removeMapping(ComponentConfig component) {
        if (assignMap.containsKey(component))
            assignMap.remove(component);
        updateParams();
        INSTANCE.updateMapping();
    }

    public void updateMapping() {
        if (worker!= null)
            worker.setMapping(getAssignMap());
    }
    public void updateParametersMap() {
        if (worker!= null)
            worker.setParameterMap(servoParameterMap());
    }


    private void createLayout() {
        deviceConfigPanel.setBorder(new TitledBorder("Device Configuration"));
        GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.WEST;

		gbc.weightx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets.set(5, 5, 5, 5);
        gbc.gridwidth = 2;
        deviceConfigPanel.add(channelsBox, gbc);
        deviceConfigPanel.add(setModeBtn, gbc);
        gbc.gridx = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        deviceConfigPanel.add(modeBox, gbc);

        portSettingPanel.setBorder(new TitledBorder("Port Setting"));

        gbc.gridx = 0;
        gbc.gridwidth = 2;
        portSettingPanel.add(comBox, gbc);
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        portSettingPanel.add(v2, gbc);
        gbc.gridx = 1;
        gbc.gridwidth = 1;
        portSettingPanel.add(connect, gbc);
        connect.addActionListener(this);

        servoOutputPanel.setBorder(new TitledBorder("Servo Outputs"));
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridwidth = 3;
        servoOutputPanel.add(new JPanel(), gbc);
        gbc.gridwidth = 1;
        gbc.gridx = 3;
        servoOutputPanel.add(new JLabel("Trim"), gbc);
        gbc.gridx = 4;
        servoOutputPanel.add(new JLabel("EPA"), gbc);

        for (int j = 0; j < SERVO_COUNT; j++) {
            servoBars[j] = new JProgressBar(0,1024);
            servoInv[j] = new JRadioButton("inv");
            servoInv[j].addActionListener(this);
            servoInv[j].setSelected(false);
            Boolean value = Boolean.valueOf(GlobalProperties.getProperties().getProperty("INV"+j, Boolean.toString(servoInv[j].isSelected()) ));
            servoInv[j].setSelected(value);
            GlobalProperties.getProperties().setProperty("INV"+j, Boolean.toString(servoInv[j].isSelected()));
            servoTrim[j] = new JScrollBar(JScrollBar.HORIZONTAL, 0, 0, -100, 100);
            servoTrim[j].addAdjustmentListener(this);
            Integer ivalue = Integer.valueOf(GlobalProperties.getProperties().getProperty("TRIM"+j, Integer.toString(servoTrim[j].getValue()) ));
            servoTrim[j].setValue(ivalue);
            GlobalProperties.getProperties().setProperty("TRIM"+j, Integer.toString(servoTrim[j].getValue()) );
            servoEPA[j] = new JScrollBar(JScrollBar.HORIZONTAL, 100, 0, 50, 150);
            servoEPA[j].addAdjustmentListener(this);
            ivalue = Integer.valueOf(GlobalProperties.getProperties().getProperty("EPA"+j, Integer.toString(servoEPA[j].getValue()) ));
            servoEPA[j].setValue(ivalue);
            GlobalProperties.getProperties().setProperty("EPA"+j, Integer.toString(servoEPA[j].getValue()) );
            gbc.gridx = 0;
            servoOutputPanel.add(new JLabel(""+(j+1)),gbc);
            gbc.gridx = 1;
            servoOutputPanel.add(servoBars[j], gbc);
            gbc.gridx = 2;
            servoOutputPanel.add(servoInv[j], gbc);
            gbc.gridx = 3;
            servoOutputPanel.add(servoTrim[j], gbc);
            gbc.gridx = 4;
            servoOutputPanel.add(servoEPA[j], gbc);
        }
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridheight = GridBagConstraints.REMAINDER;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weighty = 100;
        servoOutputPanel.add(new JPanel(), gbc);
        setLayout(new BorderLayout());
        JPanel leftPanel =  new JPanel();
        leftPanel.setLayout(new GridBagLayout());
        add(leftPanel, BorderLayout.WEST);
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(0,0,0,0);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx=1;
        gbc.weighty = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        //gbc.gridheight = GridBagConstraints.RELATIVE;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.weightx = 1;

        leftPanel.add(deviceConfigPanel,gbc);
        leftPanel.add(portSettingPanel,gbc);
        gbc.weighty = 100;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridheight = GridBagConstraints.REMAINDER;
        leftPanel.add(new JPanel(),gbc);
        add(servoOutputPanel, BorderLayout.CENTER);
    }



    /**
     * @return    A HashSet containing the CommPortIdentifier for all serial ports that are not currently being used.
     */
    public static HashSet<CommPortIdentifier> getAvailableSerialPorts() {
        HashSet<CommPortIdentifier> h = new HashSet<CommPortIdentifier>();
        Enumeration thePorts = CommPortIdentifier.getPortIdentifiers();
        while (thePorts.hasMoreElements()) {
            CommPortIdentifier com = (CommPortIdentifier) thePorts.nextElement();
            switch (com.getPortType()) {
            case CommPortIdentifier.PORT_SERIAL:
               // try {
                   // CommPort thePort = com.open("CommUtil", 50);
                    //thePort.close();
                    h.add(com);
                //} catch (PortInUseException e) {
                //    System.out.println("Port, "  + com.getName() + ", is in use.");
               // } catch (Exception e) {
                //    System.err.println("Failed to open port " +  com.getName());
               //     e.printStackTrace();
               // }
            }
        }
        return h;
    }

    public void actionPerformed(ActionEvent e) {
         if (e.getSource() == connect) {
            if (connect.getText().equals(CONNECT)) {
                //connect
                try {
                    connect(comBox.getSelectedItem().toString());
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(this,
                    e1.getMessage(),
                    "Error!",
                    JOptionPane.WARNING_MESSAGE);
                    return;
                }
                connect.setText(DISCONNECT);
            } else {
                workingThread.interrupt();
                try {
                    workingThread.join();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                workingThread = null;
                worker =null;
                //disconnect
                connect.setText(CONNECT);
            }
        } else if (e.getSource() == setModeBtn) {
            if (connect.getText().equals(CONNECT)) {
                JOptionPane.showMessageDialog(this,
                        "You must open serial port first.",
                        "Warning!",
                        JOptionPane.WARNING_MESSAGE);
            } else {
                worker.sendNewParameterAndTerminate(channelsBox.getSelectedIndex() + 1, modeBox.getSelectedIndex());
                try {
                    workingThread.join();
                } catch (InterruptedException e1) {
                    e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                worker = null;
                workingThread = null;
                connect.setText(CONNECT);
                JOptionPane.showMessageDialog(this,
                        "PPM Stream Type Changed. \n" +
                                "Please restart your transmitter from Power On/Off\n" +
                                "After that you can connect do transmitter again.",
                        "Warning", JOptionPane.WARNING_MESSAGE);
            }
        } else if (e.getSource() == v2) {
             GlobalProperties.getProperties().setProperty("V2", Boolean.toString(v2.isSelected()));
             if (worker != null)
                 worker.setV2(v2.isSelected());
         } else {
             updateParametersMap();
             updateProperties();
         }
    }

    private void connect ( String portName ) throws Exception
    {
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        if ( portIdentifier.isCurrentlyOwned() )
            throw new Exception("Port is currently in use");
        else {
            for (JProgressBar progressBar: servoBars)
                progressBar.setValue(0);
            CommPort commPort = portIdentifier.open(this.getClass().getName(),2000);
            SerialPort serialPort = (SerialPort) commPort;
            serialPort.setSerialPortParams(38400,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
            worker = new Usb2PPMWorker(serialPort, serialPort.getOutputStream(), v2.isSelected());
            worker.addListener(this);
            updateMapping();
            updateParametersMap();
            workingThread = new Thread(worker);
            workingThread.start();


        }
    }

    private Map<Integer, ServoParameter> servoParameterMap() {
        Map<Integer, ServoParameter> result = new TreeMap<Integer, ServoParameter>();
        for (int i = 0; i < SERVO_COUNT; i++) {
            ServoParameter parameter = new ServoParameter(servoInv[i].isSelected(), servoTrim[i].getValue(), servoEPA[i].getValue());
            result.put(i+1, parameter);
        }
        return result;
    }

    private void updateProperties() {
        for (int i = 0; i < SERVO_COUNT; i++) {
            if (servoInv[i] == null || servoTrim[i] == null || servoEPA[i] == null)
                break;
            GlobalProperties.getProperties().setProperty("INV"+i, Boolean.toString(servoInv[i].isSelected()));
            GlobalProperties.getProperties().setProperty("TRIM"+i, Integer.toString(servoTrim[i].getValue()));
            GlobalProperties.getProperties().setProperty("EPA"+i, Integer.toString(servoEPA[i].getValue()));
        }
    }

    public void dataSent(final int channel, final int data) {
        if (channel <= SERVO_COUNT) {
            SwingUtilities.invokeLater( new Runnable() {
                public void run() {
                    servoBars[channel - 1].setValue(data);
                }
            });

        }
    }

    public void adjustmentValueChanged(AdjustmentEvent e) {
        updateParametersMap();
        updateProperties();

    }
}
