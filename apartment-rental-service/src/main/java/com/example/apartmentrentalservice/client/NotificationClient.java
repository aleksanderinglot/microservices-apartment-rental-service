package com.example.apartmentrentalservice.client;

import com.example.apartmentrentalservice.dto.NotificationDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.concurrent.CompletableFuture;

@FeignClient(name = "notification")
public interface NotificationClient {

    @PostMapping("/api/notification/send")
    CompletableFuture<Void> sendNotification(@RequestBody NotificationDTO notificationDTO);

    @PostMapping("/api/notification/process")
    ResponseEntity<String> processNotification(@RequestBody NotificationDTO notificationDTO);

    @PostMapping("/api/notification/schedule")
    CompletableFuture<ResponseEntity<String>> scheduleNotification(@RequestBody NotificationDTO notificationDTO, @RequestParam("scheduledTime") String scheduledTime);
}
