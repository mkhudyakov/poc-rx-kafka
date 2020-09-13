package com.poc.rx.core.route;

import com.poc.rx.notification.model.EmailSendRequest;
import com.poc.rx.signup.model.UserRegistrationRequest;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.kafka.KafkaConstants;
import org.apache.camel.component.kafka.KafkaManualCommit;
import org.apache.kafka.common.utils.Bytes;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UserRegistrationHandlerRoute extends RouteBuilder {

    @Value("${spring.broker.kafka.address}")
    private String kafkaAddress;

    @Override
    public void configure() throws Exception {
        final String kafkaBaseUrl = String.format("kafka:%s?brokers=%s", kafkaAddress, kafkaAddress);

        from(String.format("%s&topic=req-user-reg&groupId=defaultGroup&clientId=signupClient" +
                "&autoCommitEnable=false&allowManualCommit=true&offsetRepository=#offsetStore", kafkaBaseUrl))
            .autoStartup(true)
            /* Deserialize a request */
            .process(exchange -> {
                UserRegistrationRequest request = UserRegistrationRequest.parseFrom(exchange.getIn().getBody(String.class)
                        .getBytes());
                exchange.getIn().setBody(request);
            })
            .wireTap("direct:email_confirmation")
            .process(exchange -> {
                KafkaManualCommit manual = exchange.getIn().getHeader(KafkaConstants.MANUAL_COMMIT, KafkaManualCommit.class);
                if (manual != null) {
                    manual.commitSync();
                }
            })
            .to("log:done");

        /* Email Delivery Route */
        from("direct:email_confirmation")
            .process(exchange -> {
                UserRegistrationRequest request = exchange.getIn().getBody(UserRegistrationRequest.class);
                EmailSendRequest emailRequest = EmailSendRequest.newBuilder()
                        .setRequestId(request.getRequestId())
                        .setTo(request.getEmail())
                        .setTemplateId("template_email_confirmation")
                        .putParameters("link", "https://www.google.com")
                        .build();

                exchange.getIn().setBody(Bytes.wrap(emailRequest.toByteArray()));
            })
            .to(String.format("%s&topic=req-notification&serializerClass=org.apache.kafka.common.serialization.BytesSerializer",
                    kafkaBaseUrl));
    }
}
