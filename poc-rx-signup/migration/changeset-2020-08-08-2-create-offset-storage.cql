USE eis;

CREATE TABLE offset_storage (
    client_id text,
    group_id text,
    topic_partition text,
    offset text,
    PRIMARY KEY ((client_id, group_id))
);