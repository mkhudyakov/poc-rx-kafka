package com.poc.rx.data.entity;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Table(value = "offset_storage")
public class OffsetStorage {

    @PrimaryKeyColumn(name = "client_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private final String clientId;

    @PrimaryKeyColumn(name = "group_id", ordinal = 1, type = PrimaryKeyType.PARTITIONED)
    private final String groupId;

    @Column(value = "topic_partition")
    private final String topicPartition;

    private final String offset;

    public OffsetStorage(String clientId, String groupId, String topicPartition, String offset) {
        this.clientId = clientId;
        this.groupId = groupId;
        this.topicPartition = topicPartition;
        this.offset = offset;
    }

    public String getClientId() {
        return clientId;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getTopicPartition() {
        return topicPartition;
    }

    public String getOffset() {
        return offset;
    }
}
