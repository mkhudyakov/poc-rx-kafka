package com.poc.rx.core.route;

import com.poc.rx.core.model.UserRegistrationRequest;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.kafka.KafkaConstants;
import org.apache.camel.component.kafka.KafkaManualCommit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UserRegistrationHandlerRoute extends RouteBuilder {

    @Value("${spring.broker.kafka.address}")
    private String kafkaAddress;

    @Override
    public void configure() throws Exception {
        final String kafkaBaseUrl = String.format("kafka:%s?brokers=%s", kafkaAddress, kafkaAddress);

        from(String.format("%s&topic=req-user-reg&groupId=default-group&clientId=defaultClient" +
                "&autoCommitEnable=false&allowManualCommit=true&offsetRepository=#offsetStore", kafkaBaseUrl))
            .autoStartup(true)
            .process(exchange -> {
                UserRegistrationRequest request = UserRegistrationRequest.parseFrom(exchange.getIn().getBody(String.class)
                        .getBytes());
                exchange.getIn().setBody(request);

            })
            .process(exchange -> {
                KafkaManualCommit manual = exchange.getIn().getHeader(KafkaConstants.MANUAL_COMMIT, KafkaManualCommit.class);
                if (manual != null) {
                    manual.commitSync();
                }
            })
            .to("log:done");
    }
}
