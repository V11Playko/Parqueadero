package com.playko.userservice.controller;

import com.playko.userservice.controller.mapper.IUserRequestMapper;
import com.playko.userservice.service.IUserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users/v1/partner")
public class PartnerRestController {

    private final IUserService userService;
    private final IUserRequestMapper userRequestMapper;

    public PartnerRestController(IUserService userService, IUserRequestMapper userRequestMapper) {
        this.userService = userService;
        this.userRequestMapper = userRequestMapper;
    }

}
