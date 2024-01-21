package com.playko.userservice.service;

public interface IAuthPasswordEncoderPort {
    String encodePassword(String decodedPassword);

    String decodePassword(String encodedPassword);
}
