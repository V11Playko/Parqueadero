package com.playko.messagingservice.service.impl;

import com.playko.messagingservice.entities.SendNotification;
import com.playko.messagingservice.service.INotificationService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class NotificationService implements INotificationService {
    /**
     * Notifica al cliente sobre la entrada de un vehículo al parqueadero.
     *
     * @param sendNotification Objeto que contiene la información de notificación, incluyendo el email del cliente,
     *                         la placa del vehículo, el mensaje personalizado y el id del parqueadero.
     */
    @Override
    public void sendNotification(SendNotification sendNotification) {
        System.out.println("Email: " + sendNotification.getEmail());
        System.out.println("Placa: " + sendNotification.getPlate());
        System.out.println("Mensaje: " + sendNotification.getMessage());
        System.out.println("Id del parqueadero: " + sendNotification.getParkingId());
    }
}

