package main.usb2ppm.event;

/**
 * @author Alexandr Vorobiev
 */
public interface DataSentEvent {
    void dataSent(final int channel, final int data);
}
