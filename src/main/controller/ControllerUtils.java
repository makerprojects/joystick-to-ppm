package main.controller;

import net.java.games.input.Controller;

/**
 * @author Alexandr Vorobiev
 */
public class ControllerUtils {
    public static boolean isValidController(Controller c) {
        return c.getType() == Controller.Type.WHEEL || c.getType() == Controller.Type.RUDDER || c.getType() == Controller.Type.STICK ||
                c.getType() == Controller.Type.GAMEPAD /*|| c.getType() == Controller.Type.MOUSE*/;
    }
}
