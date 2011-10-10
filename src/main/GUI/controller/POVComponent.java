package main.GUI.controller;

import net.java.games.input.Component;

import javax.swing.*;
import java.awt.*;

/**
 * @author Alexandr Vorobiev
 */
public class POVComponent extends JComponent{
    private final static int R = 10;
    private final Component component;
    private float data = Component.POV.OFF;
    public POVComponent(Component component) {
        this.component = component;
        setPreferredSize(new Dimension(200,200));
        setMinimumSize(getPreferredSize());
        setMaximumSize(getPreferredSize());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;
        int w = getWidth();
        int h = getHeight();
        int wh = Math.min(w,h);
        int ulx = (w - wh) /2;
        int uly = (h - wh) /2;
        // Set pen parameters
        g2d.setPaint(Color.white);
        g2d.fillOval(ulx, uly, wh, wh);
        g2d.setPaint(Color.red);
        if (data == Component.POV.CENTER) {
            g2d.fillOval(ulx - R  + wh/2 , uly - R + wh/2, R*2, R*2);
        } else if ( data == Component.POV.UP) {
            g2d.fillOval(ulx - R  + wh/2 , uly + R, R*2, R*2);
        } else if ( data == Component.POV.UP_RIGHT) {
            g2d.fillOval(ulx + 3*wh/4 , uly + wh/4 , R*2, R*2);
        } else if ( data == Component.POV.RIGHT) {
            g2d.fillOval(ulx - R*2  + wh , uly + wh/2 - R, R*2, R*2);
        } else if ( data == Component.POV.DOWN_RIGHT) {
            g2d.fillOval(ulx + 3*wh/4 , uly + 3*wh/4  - R, R*2, R*2);
        } else if ( data == Component.POV.DOWN) {
            g2d.fillOval(ulx - R  + wh/2 , uly + wh - R*2, R*2, R*2);
        } else if ( data == Component.POV.DOWN_LEFT) {
            g2d.fillOval(ulx + wh/4  , uly+ 3*wh/4 - R , R*2, R*2);
        } else if ( data == Component.POV.LEFT) {
            g2d.fillOval(ulx, uly - R + wh/2, R*2, R*2);
        } else if ( data == Component.POV.UP_LEFT) {
            g2d.fillOval(ulx + wh/4 -R, uly + wh/4 , R*2, R*2);
        }
    }

		public void poll(){
            data = component.getPollData();
            repaint();
		}
        public String getStringValue() {
            if (data == Component.POV.CENTER) {
                return "Center";
            } else if ( data == Component.POV.UP) {
                return "Up";
            } else if ( data == Component.POV.UP_RIGHT) {
                return "Up_right";
            } else if ( data == Component.POV.RIGHT) {
                return "Right";
            } else if ( data == Component.POV.DOWN_RIGHT) {
                return "Down_right";
            } else if ( data == Component.POV.DOWN) {
                return "Down";
            } else if ( data == Component.POV.DOWN_LEFT) {
                return "Down_left";
            } else if ( data == Component.POV.LEFT) {
                return "Left";
            } else if ( data == Component.POV.UP_LEFT) {
                return "Up_left";
            } else if ( data == Component.POV.OFF) {
                return "Off";
            } else
                return String.format("%.2f",data);

        }
}
