package com.playko.parkingservice.client;

import com.playko.parkingservice.client.dto.SendNotification;
import com.playko.parkingservice.client.interceptor.FeignClientInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "Messaging-Service",
        url = "http://localhost:8093/messaging/v1",
        configuration = FeignClientInterceptor.class)
public interface IMessagingClient {

    @PostMapping(value = "/send-notification")
    void sendNotification(@RequestBody SendNotification sendNotification);
}
