package com.poc.rx.api.repository;

import com.poc.rx.data.entity.OffsetStorage;
import com.poc.rx.data.repository.OffsetStorageRepository;
import org.apache.camel.spi.StateRepository;
import org.apache.camel.support.ServiceSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CassandraStateRepository extends ServiceSupport implements StateRepository<String, String> {

    private static final Logger LOG = LoggerFactory.getLogger(CassandraStateRepository.class);

    private final OffsetStorageRepository offsetStorageRepository;
    private final String groupId;
    private final String clientId;

    public CassandraStateRepository(OffsetStorageRepository offsetStorageRepository, String groupId, String clientId) {
        this.offsetStorageRepository = offsetStorageRepository;
        this.groupId = groupId;
        this.clientId = clientId;
    }

    @Override
    public void setState(String key, String value) {
        OffsetStorage offset = new OffsetStorage(clientId, groupId, key, value);
        offsetStorageRepository.save(offset);
    }

    @Override
    public String getState(String key) {
        return offsetStorageRepository.findOffset(groupId, clientId)
                .map(OffsetStorage::getOffset).orElse(null);
    }

    @Override
    protected void doStart() throws Exception {
        LOG.info("Cassandra State Repository started");
    }

    @Override
    protected void doStop() throws Exception {
        LOG.info("Cassandra State Repository stopped");
    }
}
