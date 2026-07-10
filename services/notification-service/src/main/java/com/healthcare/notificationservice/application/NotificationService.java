package com.healthcare.notificationservice.application;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.healthcare.notificationservice.api.NotificationRequest;
import com.healthcare.notificationservice.api.NotificationResponse;
import com.healthcare.notificationservice.domain.Notification;
import com.healthcare.notificationservice.domain.NotificationRepository;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Transactional
    public NotificationResponse create(NotificationRequest request) {
        return NotificationMapper.toResponse(notificationRepository.save(NotificationMapper.newEntity(request)));
    }

    @Transactional(readOnly = true)
    public NotificationResponse getById(String notificationId) {
        return notificationRepository.findById(notificationId).map(NotificationMapper::toResponse).orElseThrow(() -> new NotificationNotFoundException(notificationId));
    }

    @Transactional(readOnly = true)
    public List<NotificationResponse> search(String recipient) {
        if (recipient == null || recipient.isBlank()) {
            return notificationRepository.findAll().stream().map(NotificationMapper::toResponse).toList();
        }
        return notificationRepository.findByRecipientContainingIgnoreCase(recipient).stream().map(NotificationMapper::toResponse).toList();
    }

    @Transactional
    public void createFromDomainEvent(String eventType, String referenceId, String recipient) {
        Notification notification = new Notification();
        notification.setChannel("EMAIL");
        notification.setRecipient(recipient == null || recipient.isBlank() ? "ops@healthcare.local" : recipient);
        notification.setSubject(eventType + " processed");
        notification.setBody("Event " + eventType + " was received for reference " + referenceId);
        notification.setStatus("QUEUED");
        notification.setSentAt(LocalDateTime.now());
        notificationRepository.save(notification);
    }
}