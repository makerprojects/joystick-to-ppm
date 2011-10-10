package main.GUI.controller;

import net.java.games.input.Component;
import net.java.games.input.Controller;

/**
 * @author Alexandr Vorobiev
 */
public class ComponentConfig {
    private final Controller controller;
    private final Component component;
    private float maxValue = 1.0f;
    private float minValue = -1.0f;
    private int sentValue = 50;
    private int prevValue = 512;

    public int getPrevValue() {
        return prevValue;
    }

    public void setPrevValue(int prevValue) {
        this.prevValue = prevValue;
    }

    private boolean x = true;

    public boolean isX() {
        return x;
    }

    public ComponentConfig(Controller controller, Component component) {
        this.controller = controller;
        this.component = component;
    }

    public int getPercentage(float val) {
        float max = maxValue - minValue;
        float value = val - minValue;
        return (int)((value / max) * 100.0f);
    }

    public int getSentValue() {
        return sentValue;
    }

    public void setSentValue(int sentValue) {
        this.sentValue = sentValue;
    }

    public ComponentConfig(Controller controller, Component component, boolean X) {
        this(controller,component);
        this.x = X;
    }
    public Controller getController() {
        return controller;
    }

    public Component getComponent() {
        return component;
    }

    public float getMinValue() {
        return minValue;
    }

    public float getMaxValue() {
        return maxValue;
    }

    public void updateMinMax(float v) {
        if (v > maxValue)
            maxValue = v;
        if (v < minValue)
            minValue = v;

    }

    @Override
    public String toString() {
        return controller.getName() +";" + component.getName() + ";" + x;
    }
}
