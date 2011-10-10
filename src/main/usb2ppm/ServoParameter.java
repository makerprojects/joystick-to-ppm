package main.usb2ppm;

/**
 * @author Alexandr Vorobiev
 */
public class ServoParameter {
    private boolean reverse;
    private int trim;
    private int epa;
    public ServoParameter(boolean reverse, int trim, int epa) {
        this.reverse = reverse;
        this.trim = trim;
        this.epa = epa;
    }

    public boolean isReverse() {
        return reverse;
    }

    public int getTrim() {
        return trim;
    }

    public int getEpa() {
        return epa;
    }
}
