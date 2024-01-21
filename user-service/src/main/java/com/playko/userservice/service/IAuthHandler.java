package com.playko.userservice.service;

import com.playko.userservice.configuration.security.dto.JwtTokenResponseDto;
import com.playko.userservice.configuration.security.dto.LoginRequestDto;

public interface IAuthHandler {
    JwtTokenResponseDto loginUser(LoginRequestDto loginRequestDto);
}
