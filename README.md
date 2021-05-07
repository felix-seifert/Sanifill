# Sanifill [![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)

Sanifill is a system which allows analytics of the filling and usage of liquid in sanitary liquid containers like soap 
dispensers or hand sanitiser containers. Sanifill does so by reading the filling of liquid containers through sensors 
which are then published to a Kafka topic. From this topic, several consumers can read the sensor data and perform their 
analytics.

Sanifill uses sensors which integrated in the existing environment and therefore, adapt to the user. The system's user, 
a cleaner or someone else responsible for refilling the liquid containers, benefits from the information the system 
provides while the end-user of the sanitary facilities does not directly realise the existence of Sanifill or the 
system's sensors. As these sensors sense their environment, the filling of their monitored liquidity container, the 
system is context-aware and, as opposed to sampling methods, listens to events.

## Run

For the system to function, one have to start a message broker and other infrastructure which the system needs (amongst 
others Kafka and database). This can be achieved by spinning up the Docker containers in 
[`docker-compose.yml`](infrastructure/docker-compose.yml) with a simple `docker-compose up`.

To simulate some physical sensor which publish their data to Kafka, start sensors in the folder [sensor](sensor) with 
`./mvnw quarkus:dev -Dquarkus.http.port=<port> -Dsensor.id=<sensor-id>`.

You can then use the Vaadin frontend to consume the sensor data from Kafka. Just start the [Sanifill frontend](frontend) 
with `mvn spring-boot:run -Pproduction` and open `http://localhost:8080/` to see the frontend programme.

## Used Technology

* Quarkus to build sensor simulations
* Docker to run required infrastructure and simplify running of built applications
* Kafka (with Zookeeper) as message broker between sensors and other components
* Spring Boot and Vaadin to build frontend which consumes other services via Kafka and REST API

## Copyright and License

Copyright © 2021, [Felix Seifert](https://www.felix-seifert.com/)

This programme is free software. It is licensed under the GNU GPL version 3 or later. That means you are free to use 
this programme for any purpose; free to study and modify this programme to suit your needs; and free to share this 
programme or your modifications with anyone. If you share this programme or your modifications, you must grant the 
recipients the same freedoms. To be more specific: You must share the source code under the same license.
For details see [https://www.gnu.org/licenses/gpl-3.0.html](https://www.gnu.org/licenses/gpl-3.0.html)