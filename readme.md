### 1. Install build tools
```
brew install protobuf
```

### 1. Create an environment
```
docker-compose up -d
```

#### 1.1 Create Kafka topics
```
docker exec -it eis_kafka-1_1 bash
kafka-topics.sh --create --zookeeper zookeeper:2181 --replication-factor 3 --partitions 1 --topic req-user-reg --config min.insync.replicas=2
kafka-topics.sh --list --zookeeper zookeeper:2181
```

### 1.2. Run migration scripts

Once you have run the command above ensure Cassandra has started successfully by checking the logs `docker logs -f eis_cassandra_1`. Once started run the following commands to create the database.  

```
docker cp ./poc-rx-signup/migration eis_cassandra_1:/migration
docker exec -it eis_cassandra_1 sh -c "cqlsh < /migration/changeset-2020-08-08-1-create-keyspace.cql"
docker exec -it eis_cassandra_1 sh -c "cqlsh < /migration/changeset-2020-08-08-2-create-offset-storage.cql"
```