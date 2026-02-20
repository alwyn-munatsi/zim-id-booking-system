package org.zimid.notificationservice.service;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.zimid.notificationservice.model.BookingEvent;

import java.time.format.DateTimeFormatter;

@Service
@Slf4j
public class SmsService {

    @Value("${sms.africastalking.username:sandbox}")
    private String username;

    @Value("${sms.africastalking.api-key:}")
    private String apiKey;

    @Value("${sms.africastalking.enabled:false}")
    private boolean smsEnabled;

    private static final String SMS_URL = "https://api.africastalking.com/version1/messaging";

    private final OkHttpClient httpClient = new OkHttpClient();

    public void sendBookingConfirmation(BookingEvent event) {
        String message = buildConfirmationMessage(event);
        sendSms(event.getPhoneNumber(), message);
    }

    public void sendBookingUpdate(BookingEvent event) {
        String message = buildUpdateMessage(event);
        sendSms(event.getPhoneNumber(), message);
    }

    public void sendBookingCancellation(BookingEvent event) {
        String message = buildCancellationMessage(event);
        sendSms(event.getPhoneNumber(), message);
    }

    private void sendSms(String phoneNumber, String message) {
        if (!smsEnabled) {
            log.info("📱 SMS DISABLED - Would send to {}: {}", phoneNumber, message);
            return;
        }

        if (apiKey == null || apiKey.isEmpty()) {
            log.warn("📱 SMS API key not configured - Would send to {}: {}", phoneNumber, message);
            return;
        }

        try {
            log.info("📱 Sending SMS to: {}", phoneNumber);

            RequestBody formBody = new FormBody.Builder()
                    .add("username", username)
                    .add("to", phoneNumber)
                    .add("message", message)
                    .build();

            Request request = new Request.Builder()
                    .url(SMS_URL)
                    .addHeader("apiKey", apiKey)
                    .addHeader("Content-Type", "application/x-www-form-urlencoded")
                    .addHeader("Accept", "application/json")
                    .post(formBody)
                    .build();

            Response response = httpClient.newCall(request).execute();

            if (response.isSuccessful()) {
                log.info("SMS sent successfully to: {}", phoneNumber);
                log.debug("Response: {}", response.body().string());
            } else {
                log.error("SMS failed to {}: HTTP {}", phoneNumber, response.code());
            }

            response.close();
        } catch (Exception e) {
            log.error("Failed to send SMS to: {}", phoneNumber, e);
        }
    }

    private String buildConfirmationMessage(BookingEvent event) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");

        return String.format(
                "ZimID BOOKING CONFIRMED\n" +
                        "Ref: %s\n" +
                        "Office: %s\n" +
                        "Date: %s at %s\n" +
                        "Service: %s\n" +
                        "Bring all required documents.\n" +
                        "Registrar General Zimbabwe",
                event.getBookingReference(),
                event.getProvinceName(),
                event.getAppointmentDate().format(dateFormatter),
                event.getAppointmentTime().format(timeFormatter),
                event.getServiceName()
        );
    }

    private String buildUpdateMessage(BookingEvent event) {
        return String.format(
                "ZimID BOOKING UPDATED\n" +
                        "Ref: %s\n" +
                        "Status: %s\n" +
                        "Office: %s\n" +
                        "Check your email for details.\n" +
                        "Registrar General Zimbabwe",
                event.getBookingReference(),
                event.getStatus(),
                event.getProvinceName()
        );
    }

    private String buildCancellationMessage(BookingEvent event) {
        return String.format(
                "ZimID BOOKING CANCELLED\n" +
                        "Ref: %s\n" +
                        "Your appointment has been cancelled.\n" +
                        "You can book a new appointment anytime.\n" +
                        "Registrar General Zimbabwe",
                event.getBookingReference()
        );
    }
}