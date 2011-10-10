package main.usb2ppm.event;

/**
 * @author Alexandr Vorobiev
 */
public interface DataSentEvent {
    public void dataSent(final int channel, final int data);
}
