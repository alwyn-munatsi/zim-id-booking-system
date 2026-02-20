package org.zimid.notificationservice.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.zimid.notificationservice.model.BookingEvent;

import java.time.format.DateTimeFormatter;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendBookingConfirmation(BookingEvent event) {
        try {
            log.info("Sending confirmation email to: {}", event.getEmail());

            Context context = new Context();
            context.setVariable("fullName", event.getFullName());
            context.setVariable("bookingReference", event.getBookingReference());
            context.setVariable("provinceName", event.getProvinceName());
            context.setVariable("serviceName", event.getServiceName());
            context.setVariable("appointmentDate", event.getAppointmentDate().format(DateTimeFormatter.ofPattern("dd MMMM yyyy")));
            context.setVariable("appointmentTime", event.getAppointmentTime().format(DateTimeFormatter.ofPattern("hh:mm a")));

            String htmlContent = templateEngine.process("booking-confirmation", context);

            sendHtmlEmail(
                    event.getEmail(),
                    "ZimID Booking Confirmation - " + event.getBookingReference(),
                    htmlContent
            );

            log.info("Confirmation email sent successfully to: {}", event.getEmail());
        } catch (Exception e) {
            log.error("Failed to send confirmation email to: {}", event.getEmail(), e);
        }
    }

    public void sendBookingUpdate(BookingEvent event) {
        try {
            log.info("Sending update email to: {}", event.getEmail());

            Context context = new Context();
            context.setVariable("fullName", event.getFullName());
            context.setVariable("bookingReference", event.getBookingReference());
            context.setVariable("status", event.getStatus());
            context.setVariable("provinceName", event.getProvinceName());
            context.setVariable("appointmentDate", event.getAppointmentDate().format(DateTimeFormatter.ofPattern("dd MMMM yyyy")));
            context.setVariable("appointmentTime", event.getAppointmentTime().format(DateTimeFormatter.ofPattern("hh:mm a")));

            String htmlContent = templateEngine.process("booking-update", context);

            sendHtmlEmail(
                    event.getEmail(),
                    "ZimID Booking Update - " + event.getBookingReference(),
                    htmlContent
            );

            log.info("Update email sent successfully to: {}", event.getEmail());
        } catch (Exception e) {
            log.error("Failed to send update email to: {}", event.getEmail(), e);
        }
    }

    public void sendBookingCancellation(BookingEvent event) {
        try {
            log.info("Sending cancellation email to: {}", event.getEmail());

            Context context = new Context();
            context.setVariable("fullName", event.getFullName());
            context.setVariable("bookingReference", event.getBookingReference());
            context.setVariable("provinceName", event.getProvinceName());
            context.setVariable("appointmentDate", event.getAppointmentDate().format(DateTimeFormatter.ofPattern("dd MMMM yyyy")));

            String htmlContent = templateEngine.process("booking-cancellation", context);

            sendHtmlEmail(
                    event.getEmail(),
                    "ZimID Booking Cancelled - " + event.getBookingReference(),
                    htmlContent
            );

            log.info("Cancellation email sent successfully to: {}", event.getEmail());
        } catch (Exception e) {
            log.error("Failed to send cancellation email to: {}", event.getEmail(), e);
        }
    }

    private void sendHtmlEmail(String to, String subject, String htmlContent) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom(fromEmail);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);

        mailSender.send(message);
    }
}
