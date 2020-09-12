package com.poc.rx.data.repository;

import com.poc.rx.data.entity.OffsetStorage;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface OffsetStorageRepository extends Repository<OffsetStorage, String> {

    @Query("SELECT * from offset_storage WHERE group_id = ?0 and client_id = ?1 limit 1")
    Optional<OffsetStorage> findOffset(String groupId, String clientId);

    OffsetStorage save(OffsetStorage sentence);
}
