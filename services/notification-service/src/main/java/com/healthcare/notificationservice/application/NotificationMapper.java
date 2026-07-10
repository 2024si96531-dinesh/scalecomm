package com.healthcare.notificationservice.application;

import com.healthcare.notificationservice.api.NotificationRequest;
import com.healthcare.notificationservice.api.NotificationResponse;
import com.healthcare.notificationservice.domain.Notification;

public final class NotificationMapper {

    private NotificationMapper() {
    }

    public static Notification newEntity(NotificationRequest request) {
        Notification notification = new Notification();
        apply(request, notification);
        return notification;
    }

    public static void apply(NotificationRequest request, Notification notification) {
        notification.setChannel(request.channel());
        notification.setRecipient(request.recipient());
        notification.setSubject(request.subject());
        notification.setBody(request.body());
        notification.setStatus(request.status());
    }

    public static NotificationResponse toResponse(Notification notification) {
        return new NotificationResponse(notification.getNotificationId(), notification.getChannel(), notification.getRecipient(), notification.getSubject(), notification.getBody(), notification.getStatus(), notification.getCreatedAt(), notification.getSentAt());
    }
}