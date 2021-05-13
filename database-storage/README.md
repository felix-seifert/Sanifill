# database-storage

The Quarkus service `database-storage` consumes messages from the Kafka channel `sensors` and persists them in a 
PostgreSQL database. Idea is that this services is then available to provide historical sensor data for further 
analytics.

## Run

To run a the service `database-storage` in Quarkus development mode without the need to separate build and run, you can 
use the Quarkus plugin via the Maven wrapper.

```shell script
./mvnw quarkus:dev -Dquarkus.http.port=<port>
```

Keep in mind that not only the Kafka topic `sensors` but also the PostgreSQL database should be available.

## Interact With PostgreSQL Database

The credentials for the database user can be found in [init-databases.sql](../infrastructure/postgres/init-databases.sql). 
Use these credentials to `exec` in the `postgres` container and interact with the database directly.

```shell script
docker exec -it postgres psql -d sensor_data_storage -U sensors -W
```
