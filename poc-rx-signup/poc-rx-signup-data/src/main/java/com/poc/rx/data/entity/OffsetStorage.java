package com.poc.rx.data.entity;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Table(value = "offset_storage")
public class OffsetStorage {

    @PrimaryKeyColumn(name = "group_id", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private final String groupId;

    @PrimaryKeyColumn(name = "topic_partition", ordinal = 1, type = PrimaryKeyType.CLUSTERED)
    private final String topicPartition;

    private final String offset;

    public OffsetStorage(String groupId, String topicPartition, String offset) {
        this.groupId = groupId;
        this.topicPartition = topicPartition;
        this.offset = offset;
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
