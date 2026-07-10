package com.healthcare.notificationservice.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, String> {

    List<Notification> findByRecipientContainingIgnoreCase(String recipient);
}