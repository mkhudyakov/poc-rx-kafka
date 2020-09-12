package com.poc.rx.core.service.user;

import rx.Completable;
import java.util.UUID;

/**
 * @author Maksym Khudiakov
 */
public interface UserRegistrationService {

    Completable register(UUID requestId, String email, String password, String fullName);
}
