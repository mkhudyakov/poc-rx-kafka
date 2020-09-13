USE eis;

CREATE TABLE offset_storage (
    group_id text,
    topic_partition text,
    offset text,
    PRIMARY KEY (group_id, topic_partition)
);