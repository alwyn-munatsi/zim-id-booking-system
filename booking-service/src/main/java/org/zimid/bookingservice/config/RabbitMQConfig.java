package org.zimid.bookingservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String BOOKING_EXCHANGE = "booking.exchange";
    public static final String NOTIFICATION_QUEUE = "notification.queue";
    public static final String ADMIN_QUEUE = "admin.queue";

    @Bean
    public DirectExchange bookingExchange() {
        return new DirectExchange(BOOKING_EXCHANGE);
    }

    @Bean
    public Queue notificationQueue() {
        return QueueBuilder.durable(NOTIFICATION_QUEUE).build();
    }

    @Bean
    public Queue adminQueue() {
        return QueueBuilder.durable(ADMIN_QUEUE).build();
    }

    @Bean
    public Binding notificationCreatedBinding(Queue notificationQueue, DirectExchange bookingExchange) {
        return BindingBuilder.bind(notificationQueue)
                .to(bookingExchange)
                .with("booking.created");
    }

    @Bean
    public Binding notificationUpdatedBinding(Queue notificationQueue, DirectExchange bookingExchange) {
        return BindingBuilder.bind(notificationQueue)
                .to(bookingExchange)
                .with("booking.updated");
    }

    @Bean
    public Binding notificationCancelledBinding(Queue notificationQueue, DirectExchange bookingExchange) {
        return BindingBuilder.bind(notificationQueue)
                .to(bookingExchange)
                .with("booking.cancelled");
    }

    @Bean
    public Binding adminCreatedBinding(Queue adminQueue, DirectExchange bookingExchange) {
        return BindingBuilder.bind(adminQueue)
                .to(bookingExchange)
                .with("booking.created");
    }

    @Bean
    public Binding adminUpdatedBinding(Queue adminQueue, DirectExchange bookingExchange) {
        return BindingBuilder.bind(adminQueue)
                .to(bookingExchange)
                .with("booking.updated");
    }

    @Bean
    public Binding adminCancelledBinding(Queue adminQueue, DirectExchange bookingExchange) {
        return BindingBuilder.bind(adminQueue)
                .to(bookingExchange)
                .with("booking.cancelled");
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}
