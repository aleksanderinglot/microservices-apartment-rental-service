package com.example.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final JavaMailSender mailSender;
    private final NotificationRepository notificationRepository;

    @Async
    public CompletableFuture<Boolean> sendNotification(Notification notification) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(notification.getRecipient());
            message.setSubject(notification.getTitle());
            message.setText(notification.getContent());

            //set email address to send notifications!!!
            //and configure host, port, username and password in application.properties file!!!
            message.setFrom("");
            mailSender.send(message);
            future.complete(true);
        } catch (Exception e) {
            future.completeExceptionally(e);
        }

        return future;
    }

    public void processNotification(Notification notification) {
        notificationRepository.save(notification);
        System.out.println("Processing notification and saving to the database: " + notification);
    }

    @Async
    public CompletableFuture<Boolean> scheduleNotification(Notification notification, LocalDateTime scheduledTime) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        try {
            LocalDateTime currentTime = LocalDateTime.now();
            long delayInMillis = currentTime.until(scheduledTime, ChronoUnit.MILLIS);
            if (delayInMillis < 0) {
                future.completeExceptionally(new IllegalArgumentException("Scheduled time cannot be in the past."));
            } else {
                ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
                scheduler.schedule(() -> {
                    processNotification(notification);
                    future.complete(true);
                }, delayInMillis, TimeUnit.MILLISECONDS);
            }
        } catch (Exception e) {
            future.completeExceptionally(e);
        }

        return future;
    }
}
