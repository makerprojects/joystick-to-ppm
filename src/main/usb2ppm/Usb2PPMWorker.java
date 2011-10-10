package main.usb2ppm;

import gnu.io.SerialPort;
import main.GUI.controller.ComponentConfig;
import main.usb2ppm.event.DataSentEvent;
import net.java.games.input.Component;
import net.java.games.input.Controller;


import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

/**
 * @author Alexandr Vorobiev
 */
public class Usb2PPMWorker implements Runnable {
    private static final String SET_V1 = "SET";
    private static final String SET_V2 = "S";
    private final SerialPort serialPort;
    private final OutputStream out;
    private Map<ComponentConfig, Integer> mapping;
    private volatile boolean sendParameters = false;
    private volatile int par1 =0;
    private volatile int par2 =0;
    private boolean v2 = false;
    private Vector<DataSentEvent> listeners = new Vector<DataSentEvent>();
    private Map<Integer, ServoParameter> parameterMap;
    public Usb2PPMWorker(SerialPort serialPort, OutputStream out, boolean v2) {
        this.serialPort = serialPort;
        this.out = out;
        this.v2 = v2;
    }

    private String getSetCommand() {
        return isV2() ? SET_V2 : SET_V1;
    }

    public synchronized boolean isV2() {
        return v2;
    }

    public synchronized void setMapping(Map<ComponentConfig, Integer> mapping) {
        this.mapping = mapping;
    }
    public synchronized void setV2(boolean v) {
        v2 = v;
    }
    public synchronized void setParameterMap(Map<Integer, ServoParameter> parameterMap) {
        this.parameterMap = parameterMap;
    }
    private synchronized Map<ComponentConfig, Integer> getMapping() {
        return mapping;
    }

    public void addListener(DataSentEvent listener) {
        listeners.add(listener);

    }
    public void run() {
        try {
            while (!Thread.interrupted()) {
                if (sendParameters) {
                    sendParameters(par1, par2);
                    break;
                }
                Map<ComponentConfig, Integer> mapping = getMapping();
                Map<Integer, ServoParameter> parameterMap = getParameterMap();
                Map<Integer, ComponentConfig> filteredMap = new TreeMap<Integer, ComponentConfig>();
                if (mapping != null && parameterMap != null) {
                    /*for (Map.Entry<ComponentConfig, Integer> entry: mapping.entrySet()) {
                        int channel = entry.getValue();
                        Controller controller = entry.getKey().getController();
                        Component component = entry.getKey().getComponent();
                        if (!filteredMap.containsKey(channel))
                            filteredMap.put(channel, entry.getKey());
                        else {
                            Controller controller2 = filteredMap.get(channel).getController();
                            Component component2 = filteredMap.get(channel).getComponent();
                            synchronized (controller2) {
                                synchronized (controller) {
                                    float d = component.getPollData();
                                    float d2 = component2.getPollData();
                                    if (component2.isAnalog()) {
                                        // c2 - axis
                                        if (component.isAnalog()) {
                                            if (entry.getKey().getPercentage(d) > filteredMap.get(channel).getPercentage(d2))
                                        }
                                    }
                                }
                            }
                        }
                      }*/
                    for (Map.Entry<ComponentConfig, Integer> entry: mapping.entrySet()) {
                        int channel = entry.getValue();
                        synchronized (entry.getKey().getController()) {
                            if (entry.getKey().getController().poll()) {
                                float fdata = entry.getKey().getComponent().getPollData();
                                if (entry.getKey().getComponent().isAnalog()) {
                                    entry.getKey().updateMinMax(fdata);
                                    //axis
                                    if (entry.getKey().getComponent().getDeadZone() >= Math.abs(fdata))
                                        fdata = 0;
                                    servo(channel, fdata, entry.getKey().getMinValue(), entry.getKey().getMaxValue(), parameterMap.get(entry.getValue()).isReverse(), parameterMap.get(entry.getValue()).getTrim(), parameterMap.get(entry.getValue()).getEpa());
                                    //servo(channel, -0.9f, -1.0f, 1.0f, parameterMap.get(entry.getValue()).isReverse(), parameterMap.get(entry.getValue()).getTrim(), parameterMap.get(entry.getValue()).getEpa());
                                } else if (entry.getKey().getComponent().getIdentifier() == Component.Identifier.Axis.POV) {
                                    int data = 512;
                                    if (fdata == Component.POV.OFF) {
                                        data = 512;
                                    } else if (entry.getKey().isX())  {
                                        if (fdata == Component.POV.LEFT || fdata == Component.POV.DOWN_LEFT || fdata == Component.POV.UP_LEFT) {
                                            data = entry.getKey().getPrevValue() - 50;
                                        } else if (fdata == Component.POV.RIGHT || fdata == Component.POV.DOWN_RIGHT || fdata == Component.POV.UP_RIGHT) {
                                            data = entry.getKey().getPrevValue() + 50;
                                        }
                                    } else {
                                        if (fdata == Component.POV.DOWN || fdata == Component.POV.DOWN_LEFT || fdata == Component.POV.DOWN_RIGHT) {
                                            data = entry.getKey().getPrevValue() - 50;
                                        } else if (fdata == Component.POV.UP || fdata == Component.POV.UP_RIGHT || fdata == Component.POV.UP_LEFT) {
                                            data = entry.getKey().getPrevValue() + 50;
                                        }
                                    }
                                    data = Math.min(data, 1024);
                                    data = Math.max(data,0);
                                    entry.getKey().setPrevValue(data);
                                    servo(channel, data, 0, 1024, parameterMap.get(entry.getValue()).isReverse(), parameterMap.get(entry.getValue()).getTrim(), parameterMap.get(entry.getValue()).getEpa());
                                } else {
                                    if (fdata == 0.0f){
                                        servo(channel, 0, 0, 100, parameterMap.get(entry.getValue()).isReverse(), parameterMap.get(entry.getValue()).getTrim(), parameterMap.get(entry.getValue()).getEpa());
                                    } else if (fdata == 1.0f) {
                                        servo(channel, entry.getKey().getSentValue(), 0, 100, parameterMap.get(entry.getValue()).isReverse(), parameterMap.get(entry.getValue()).getTrim(), parameterMap.get(entry.getValue()).getEpa());
                                    }
                                }

                            }
                        }

                    }
                }
                Thread.sleep(50);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            serialPort.close();
        }
    }

    private synchronized Map<Integer, ServoParameter> getParameterMap() {
        return parameterMap;
    }

    private void sendParameters(int par1, int par2) throws Exception {
        out.write((getSetCommand()  +((char)21) + ""+((char)0) +""+((char)par1)).getBytes());
        out.flush();
        Thread.sleep(500);
        out.write((getSetCommand() + ((char) 22) + "" + ((char) 0) + "" + ((char) par2)).getBytes());
        out.flush();
        for(int i= 0; i <= 20; i++) {
            out.write((char)255);
            out.flush();
            Thread.sleep(10);
        }
    }

    private void servo( int channel, float value, float min, float max, boolean inverse, int trim, int epa) throws IOException {
        System.out.println("send cmd ch="+channel +" min/max/val="+ String.format("%.1f",min)+ String.format("%.1f",max) +"/"+String.format("%.1f",value) + " trim="+trim + " epa="+epa);
        max = max - min;
        value =value - min;
        int data = (int)Math.floor(1024.0f*value / max);
        data = ((data - 512) * epa) / 100 ;
        if (data>512)
            data = 512;
        if (data < -512)
            data = -512;

        data = data + 512;
        data = data + (5*trim);
        if (data<0)
            data = 0;
        if (data>1024)
            data = 1024;
        if (inverse)
            data = 1024 - data;

        if (channel<16)
            sendValues(channel, 1000 + data);


        updateServoBar(channel, data);
    }

    private void sendValues(int channel, int value) throws IOException {
        int arrSize = 6;
        if (isV2()) {
            value*=2;
            arrSize -=2;
        }
        byte[] b = (getSetCommand() +((char)channel)+""+((char)(value/256)) +""+ ((char)(value%256))+"").getBytes();

        b[arrSize-2] =(byte)(value/256);
        b[arrSize-1] =(byte)(value%256);
        out.write(b);
        System.out.println("send cmd v="+(getSetCommand() +((char)channel)+""+((char)(value/256)) +""+ ((char)(value%256))));
        out.flush();
    }
    private void updateServoBar(int channel, int data) {
        for (DataSentEvent listener: listeners)
            listener.dataSent(channel, data);
    }

    public void sendNewParameterAndTerminate(int par1, int par2) {
        this.par1 = par1;
        this.par2 = par2;
        sendParameters = true;
    }
}
