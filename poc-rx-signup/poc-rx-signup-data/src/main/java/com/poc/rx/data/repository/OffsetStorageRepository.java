package com.poc.rx.data.repository;

import com.datastax.oss.driver.api.core.DefaultConsistencyLevel;
import com.poc.rx.data.entity.OffsetStorage;
import org.springframework.data.cassandra.repository.Consistency;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface OffsetStorageRepository extends Repository<OffsetStorage, String> {

    @Consistency(DefaultConsistencyLevel.LOCAL_QUORUM)
    @Query("SELECT * from offset_storage WHERE group_id = ?0 and topic_partition = ?1 limit 1")
    Optional<OffsetStorage> findOffset(String groupId, String topicPartition);

    void save(OffsetStorage sentence);
}
