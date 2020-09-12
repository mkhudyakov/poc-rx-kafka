package com.poc.rx.core.service.user.impl;

import com.poc.rx.core.model.UserRegistrationRequest;
import com.poc.rx.core.service.user.UserRegistrationService;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.utils.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import rx.Completable;

import java.util.UUID;

/**
 * {@link UserRegistrationService} implementation
 * @author Maksym Khudiakov
 */
@Service
public class UserRegistrationServiceImpl implements UserRegistrationService {

    private static final Logger LOG = LoggerFactory.getLogger(UserRegistrationServiceImpl.class);

    @Value("${kafka.request.topic}")
    private String requestTopic;

    @Autowired
    private KafkaTemplate<Bytes, Bytes> kafkaTemplate;

    @Override
    public Completable register(UUID requestId, String email, String password, String fullName) {

        /* Build message */
        UserRegistrationRequest request = UserRegistrationRequest.newBuilder()
                .setRequestId(requestId.toString()).setEmail(email).setPassword(password).setFullName(fullName)
                .build();

        final ProducerRecord<Bytes, Bytes> record = new ProducerRecord<>(requestTopic, null,
                Bytes.wrap(requestId.toString().getBytes()), Bytes.wrap(request.toByteArray()));

        return Completable.create(onSubscribe -> {
            try {
                kafkaTemplate.send(record).get();
                onSubscribe.onCompleted();
            } catch (Exception ex) {
                onSubscribe.onError(ex);
            }
        });
    }
}
