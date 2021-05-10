# database-storage

The Quarkus service `database-storage` consumes messages from the Kafka channel `sensors` and persists them in a 
PostgreSQL database. Idea is that this services is then available to provide historical sensor data for further 
analytics.

## Interact With PostgreSQL Database

The credentials for the database user can be found in [init-databases.sql](../infrastructure/postgres/init-databases.sql). 
Use these credentials to `exec` in the `postgres` container and interact with the database directly.

```shell script
docker exec -it postgres psql -d sensor_data_storage -U sensors -W
```
