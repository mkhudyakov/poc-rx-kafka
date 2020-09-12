package com.poc.rx.api.controller;

import com.poc.rx.api.model.register.UserRegistrationRequest;
import com.poc.rx.core.service.user.UserRegistrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import rx.Completable;

import javax.validation.Valid;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author Maksym Khudiakov
 */
@RestController
public class UserRegistrationController {

    private static final Logger LOG = LoggerFactory.getLogger(UserRegistrationController.class);

    private final UserRegistrationService userRegistrationService;

    @Autowired
    public UserRegistrationController(UserRegistrationService userRegistrationService) {
        this.userRegistrationService = userRegistrationService;
    }

    @PostMapping(value = "/register", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public Completable register(@Valid @RequestBody UserRegistrationRequest request) {
        final UUID requestId = UUID.randomUUID();
        LOG.debug(String.format("Request for user registration received [Email: %s, RequestId: %s]",
                request.getEmail(), requestId.toString()));

        return userRegistrationService.register(requestId, request.getEmail(), request.getPassword(), request.getFullName());
    }
}
