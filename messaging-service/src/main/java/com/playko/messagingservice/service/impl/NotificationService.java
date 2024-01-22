package com.playko.messagingservice.service.impl;

import com.playko.messagingservice.entities.SendNotification;
import com.playko.messagingservice.service.INotificationService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class NotificationService implements INotificationService {
    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    /**
     * Notifica al cliente sobre la entrada de un vehículo al parqueadero.
     *
     * @param sendNotification Objeto que contiene la información de notificación, incluyendo el email del cliente,
     *                         la placa del vehículo, el mensaje personalizado y el id del parqueadero.
     */
    @Override
    public void sendNotification(SendNotification sendNotification) {
        logger.info("Email: {}", sendNotification.getEmail());
        logger.info("Placa: {}", sendNotification.getPlate());
        logger.info("Mensaje: {}", sendNotification.getMessage());
        logger.info("Id del parqueadero: {}", sendNotification.getParkingId());
    }
}

