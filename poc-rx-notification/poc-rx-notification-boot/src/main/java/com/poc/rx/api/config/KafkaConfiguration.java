package com.poc.rx.api.config;

import com.poc.rx.api.repository.CassandraStateRepository;
import com.poc.rx.data.repository.OffsetStorageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Maksym Khudiakov
 */
@Configuration
public class KafkaConfiguration {

    @Value("${kafka.groupId}")
    private String kafkaGroupId;

    @Autowired
    private OffsetStorageRepository offsetStorageRepository;

    @Bean(name = "offsetStore", initMethod = "start", destroyMethod = "stop")
    public CassandraStateRepository cassandraStore() {
        return new CassandraStateRepository(offsetStorageRepository, kafkaGroupId);
    }
}