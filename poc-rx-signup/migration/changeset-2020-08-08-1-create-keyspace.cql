CREATE KEYSPACE IF NOT EXISTS eis
WITH replication = {
    'class': 'SimpleStrategy',
    'replication_factor': 1
};