package com.playko.messagingservice.service;

import com.playko.messagingservice.entities.SendNotification;

public interface INotificationService {
    void sendNotification(SendNotification sendNotification);
}
