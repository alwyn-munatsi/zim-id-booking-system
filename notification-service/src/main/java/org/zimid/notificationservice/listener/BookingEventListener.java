package org.zimid.notificationservice.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.zimid.notificationservice.model.BookingEvent;
import org.zimid.notificationservice.service.EmailService;
import org.zimid.notificationservice.service.SmsService;

@Component
@Slf4j
@RequiredArgsConstructor
public class BookingEventListener {

    private final EmailService emailService;
    private final SmsService smsService;

    @RabbitListener(queues = "notification.queue")
    public void handleBookingEvent(BookingEvent event) {
        log.info("Received booking event: {} - {}", event.getEventType(), event.getBookingReference());

        try {
            switch (event.getEventType()) {
                case "CREATED":
                    handleBookingCreated(event);
                    break;
                case "UPDATED":
                    handleBookingUpdated(event);
                    break;
                case "CANCELLED":
                    handleBookingCancelled(event);
                    break;
                default:
                    log.warn("Unknown event type: {}", event.getEventType());
            }
        } catch (Exception e) {
            log.error("Error processing booking event: {}", event.getBookingReference(), e);
        }
    }

    private void handleBookingCreated(BookingEvent event) {
        log.info("Processing booking created event: {}", event.getBookingReference());

        // Send SMS
        smsService.sendBookingConfirmation(event);

        // Send Email
        emailService.sendBookingConfirmation(event);
    }

    private void handleBookingUpdated(BookingEvent event) {
        log.info("Processing booking updated event: {}", event.getBookingReference());

        // Send SMS
        smsService.sendBookingUpdate(event);

        // Send Email
        emailService.sendBookingUpdate(event);
    }

    private void handleBookingCancelled(BookingEvent event) {
        log.info("Processing booking cancelled event: {}", event.getBookingReference());

        // Send SMS
        smsService.sendBookingCancellation(event);

        // Send Email
        emailService.sendBookingCancellation(event);
    }
}