package com.poc.rx.core.route;

import com.poc.rx.notification.model.EmailSendRequest;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.kafka.KafkaConstants;
import org.apache.camel.component.kafka.KafkaManualCommit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class NotificationHandlerRoute extends RouteBuilder {

    @Value("${spring.broker.kafka.address}")
    private String kafkaAddress;

    @Value("${kafka.clientId}")
    private String clientId;

    @Override
    public void configure() throws Exception {
        final String kafkaBaseUrl = String.format("kafka:%s?brokers=%s", kafkaAddress, kafkaAddress);

        from(String.format("%s&topic=req-notification&groupId=defaultGroup&clientId=%s" +
                "&autoCommitEnable=false&allowManualCommit=true&offsetRepository=#offsetStore", kafkaBaseUrl, clientId))
            .autoStartup(true)
            /* Email Send Processor */
            .process(exchange -> {
                EmailSendRequest request = EmailSendRequest.parseFrom(exchange.getIn().getBody(String.class)
                        .getBytes());

                exchange.getIn().setBody(request);
            })
            .to("log:EmailSentLog")
            .process(exchange -> {
                KafkaManualCommit manual = exchange.getIn().getHeader(KafkaConstants.MANUAL_COMMIT, KafkaManualCommit.class);
                if (manual != null) {
                    manual.commitSync();
                }
            });
    }
}
