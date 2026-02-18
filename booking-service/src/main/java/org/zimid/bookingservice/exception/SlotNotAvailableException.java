package org.zimid.bookingservice.exception;

public class SlotNotAvailableException extends RuntimeException {
    public SlotNotAvailableException(String message) {
        super(message);
    }
}
