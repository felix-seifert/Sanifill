# Sensor Simulation

This service simulates a sensor which measures the filling of a container. The sensor simulation is built with Quarkus 
and publishes information about the measured filling to a Kafka topic.

## Run

To run a sensor in Quarkus development mode without the need to separate build and run, you can use the Quarkus plugin 
via the Maven wrapper.

```shell script
./mvnw quarkus:dev \
    -Dquarkus.http.port=<port> \
    -Dsensor.id=<sensor-id>
```

## Kafka Topic and Sent Information

The measured information is regularly published to the Kafka topic `sensors`. Each message which a sensor published 
includes a `sensorId` of the sensor which published the message, a `dateTime` of when the message was produced and a 
`filling` which is a percentage of how much liquid is left in the measured container.

### Published Messages

To see the published messages, you can locate the file `kafka-console-consumer.sh`, go to its folder and execute 
`./kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic sensors --from-beginning`. In 
our example of running Kafka via the provided [`docker-compose.yml`](../infrastructure/docker-compose.yml), you have to 
`exec` into the started Kafa container with `docker-compose exec kafka bash`. After starting a sensor simulation, you 
can execute the following command:

```shell script
./bin/kafka-console-consumer.sh \
    --bootstrap-server localhost:9092 \
    --topic sensors \
    --from-beginning \
    --max-messages 10
```

## Run Multiple Sensors Simultaneously

To run several sensors simultaneously, each sensor needs an **own ID** and each sensor has to run on a **unique port**.

### Custom Port

Usually, each sensor application start on port `8080`. However, several applications cannot run on the same port. To 
easily modify the port of each sensor, the port can be set via the config property `quarkus.http.port=<port>`. This 
system property can also be set during startup with Maven's `-D` flag. The application can therefore be started in the 
development mode with:

```shell script
./mvnw quarkus:dev \
    -Dquarkus.http.port=<port>
```

### Custom Sensor ID

Generally, each sensor has a random UUID as its ID. However, it is also possible to set a custom ID via the config 
property `sensor.id=<sensor-id>`. This is also possible during startup with Maven's `-D` flag. The application can 
therefore be started in the development mode with:

```shell script
./mvnw quarkus:dev \
    -Dsensor.id=<sensor-id>
```

## Run Sensor in Production Mode

To create a `.jar` of the sensor application, you have to package the whole application with the Maven Wrapper.

```shell script
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory. It can be run like any other `.jar` and 
equipped with properties via the `-D` flag.

```shell script
java \
    -Dquarkus.http.port=<port> \
    -Dsensor.id=<sensor-id> \
    -jar target/quarkus-app/quarkus-run.jar
```

Be aware that the packaged application is not an _über-jar_ as the dependencies are copied into the 
`target/quarkus-app/lib/` directory. If you want to build an _über-jar_, execute the following command:

```shell script
./mvnw package \
    -Dquarkus.package.type=uber-jar
```

The application is then runnable using `java -jar target/quarkus-app/quarkus-run.jar`. (do not forget port and sensor 
id).

## Run Sensor as Native Executable

To achieve a **blazingly fast startup** and an application with a **smaller footprint**, you can run the application as a 
native executable. You can create a native executable using:

```shell script
./mvnw package -Pnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./mvnw package -Pnative \
    -Dquarkus.native.container-build=true
```

You can then execute your native executable with `./target/sensor-1.0-SNAPSHOT-runner` and also provide it properties 
via the `-D` flag.

```shell script
./target/sensor-1.0-SNAPSHOT-runner \
    -Dquarkus.http.port=<port> \
    -Dsensor.id=<sensor-id>
```

If you want to learn more about building native executables, please consult 
[https://quarkus.io/guides/maven-tooling.html](https://quarkus.io/guides/maven-tooling.html).
