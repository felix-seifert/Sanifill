# Sensor Simulation

This project simulates a sensor which measures the filling of a container. The sensor simulation is built with Quarkus 
and published information about the measured filling to a Kafka topic.

## Run

To run a sensor in Quarkus development mode without the need to separate build and run, you can use the Quarkus plugin 
via the Maven wrapper.

```shell script
./mvnw quarkus:dev -Dquarkus.http.port=<port> -Dsensor.id=<sensor-id>
```

## Kafka Topic and Sent Information

The measured information is regularly published to the Kafka topic `sensors`. Each message which a sensor published 
includes a `sensorId` of the sensor which published the message, a `dateTime` of when the message was produced and a 
`filling` which is a percentage of how much liquid is left in the measured container.

### Published Messages

To see the published messages, you can locate the file `kafka-console-consumer.sh`, go to its folder and execute 
`./kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic sensors --from-beginning --max-messages 10`. In 
our example of running Kafka via the provided [`docker-compose.yml`](../infrastructure/docker-compose.yml), you have to 
`exec` into the started Kafa container with `docker-compose exec kafka bach`. After starting a sensor simulation, you 
can execute the following command:

```shell script
./bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic sensors --from-beginning --max-messages 10
```

## Run Multiple Sensors Simultaneously

To run several sensors simultaneously, each sensor needs an **own ID** and each sensor has to run on a **unique port**.

### Custom Port

Usually, each sensor application start on port `8080`. However, several applications cannot run on the same port. To 
easily modify the port of each sensor, the port can be set via the config property `quarkus.http.port=<port>`. This 
system property can also be set during startup with Maven's `-D` flag. The application can therefore be started in the 
development mode with:

```shell script
./mvnw quarkus:dev -Dquarkus.http.port=<port>
```

### Custom Sensor ID

Generally, each sensor has a random UUID as its ID. However, it is also possible to set a custom ID via the config 
property `sensor.id=<sensor-id>`. This is also possible during startup with Maven's `-D` flag. The application can 
therefore be started in the development mode with:

```shell script
./mvnw quarkus:dev -Dsensor.id=<sensor-id>
```

[comment]: <> (## Running the application in dev mode)

[comment]: <> (You can run your application in dev mode that enables live coding using:)

[comment]: <> (```shell script)

[comment]: <> (./mvnw compile quarkus:dev)

[comment]: <> (```)

[comment]: <> (> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.)

[comment]: <> (## Packaging and running the application)

[comment]: <> (The application can be packaged using:)

[comment]: <> (```shell script)

[comment]: <> (./mvnw package)

[comment]: <> (```)

[comment]: <> (It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory. Be aware that it’s not an _über-jar_ as)

[comment]: <> (the dependencies are copied into the `target/quarkus-app/lib/` directory.)

[comment]: <> (If you want to build an _über-jar_, execute the following command:)

[comment]: <> (```shell script)

[comment]: <> (./mvnw package -Dquarkus.package.type=uber-jar)

[comment]: <> (```)

[comment]: <> (The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.)

[comment]: <> (## Creating a native executable)

[comment]: <> (You can create a native executable using:)

[comment]: <> (```shell script)

[comment]: <> (./mvnw package -Pnative)

[comment]: <> (```)

[comment]: <> (Or, if you don't have GraalVM installed, you can run the native executable build in a container using:)

[comment]: <> (```shell script)

[comment]: <> (./mvnw package -Pnative -Dquarkus.native.container-build=true)

[comment]: <> (```)

[comment]: <> (You can then execute your native executable with: `./target/sensor-1.0-SNAPSHOT-runner`)

[comment]: <> (If you want to learn more about building native executables, please consult https://quarkus.io/guides/maven-tooling.html)

[comment]: <> (.)
