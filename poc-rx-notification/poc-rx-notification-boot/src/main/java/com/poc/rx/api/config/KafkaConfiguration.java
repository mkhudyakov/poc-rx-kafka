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

    @Value("${kafka.clientId}")
    private String kafkaClientId;

    @Autowired
    private OffsetStorageRepository offsetStorageRepository;

    @Bean(name = "offsetStore", initMethod = "start", destroyMethod = "stop")
    public CassandraStateRepository cassandraStore() {
        return new CassandraStateRepository(offsetStorageRepository, kafkaGroupId, kafkaClientId);
    }

/*    @Bean(name = "offsetStore", initMethod = "start", destroyMethod = "stop")
    public FileStateRepository fileStore() {
        FileStateRepository fileStateRepository =
                FileStateRepository.fileStateRepository(new File("kafka.consumer.cfg"));
        fileStateRepository.setMaxFileStoreSize(10485760); // 10MB max
        return fileStateRepository;
    } */
}