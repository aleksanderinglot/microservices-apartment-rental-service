package com.example.notification;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @PostMapping("/send")
    public CompletableFuture<ResponseEntity<Notification>> sendNotification(@RequestBody Notification notification) {
        return CompletableFuture.supplyAsync(() -> {
            notificationService.sendNotification(notification);
            return ResponseEntity.ok(notification);
        });
    }

    @PostMapping("/process")
    public ResponseEntity<String> processNotification(@RequestBody Notification notification) {
        notificationService.processNotification(notification);
        return ResponseEntity.ok("Notification processed and saved successfully");
    }

    @PostMapping("/schedule")
    public CompletableFuture<ResponseEntity<String>> scheduleNotification(@RequestBody Notification notification, @RequestParam("scheduledTime") String scheduledTime) {
        LocalDateTime dateTime = LocalDateTime.parse(scheduledTime, DateTimeFormatter.ISO_DATE_TIME);
        return notificationService.scheduleNotification(notification, dateTime)
                .thenApplyAsync(result -> ResponseEntity.ok("Notification scheduled successfully"));
    }
}
