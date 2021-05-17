# Infrastructure

The for this project required infrastructure can be started via Docker Compose. The different Docker images startup the 
following containers:
* Kafka as message broker for asynchronous messaging
* Zookeeper as distributed coordination server for Kafka
* PostgreSQL as relational database to persist data

## Run

To start containers of the above mentioned images, run the following command.

```shell script
docker-compose up
```