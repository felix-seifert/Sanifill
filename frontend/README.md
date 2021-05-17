# Sanifill Frontend

The frontend of Sanifill shows the live status of the deployed sensors and gives the ability to inform the sensors that 
their monitored containers got refilled.

## Run

To run the Sanifill frontend in production mode without separating build and run, you can use the Spring Boot plugin to 
start the application and see it in your browser via the address `http://localhost:8080/`.

```shell script
mvn spring-boot:run -Pproduction
```

The frontend attempts on startup to retrieve the latest sensor data from the storage service. It therefore needs the 
storage service's port via the property `sanifill.storage-service`. You can just pass it to the application with the 
`-D` flag.

```shell script
mvn spring-boot:run -Pproduction \
    -Dsanifill.storage-service.port=<storage-port>
```

## Project structure

* `MainView.java` in `src/main/java` contains the navigation setup. It uses [App Layout](https://vaadin.com/components/vaadin-app-layout).
* `views` package in `src/main/java` contains the server-side Java views of the application.
* `views` folder in `frontend/` contains the client-side JavaScript views of the application.

[comment]: <> (## Deploying using Docker)

[comment]: <> (To build the Dockerized version of the project, run)

[comment]: <> (```)

[comment]: <> (docker build . -t sanifill-frontend:latest)

[comment]: <> (```)

[comment]: <> (Once the Docker image is correctly built, you can test it locally using)

[comment]: <> (```)

[comment]: <> (docker run -p 8080:8080 sanifill-frontend:latest)

[comment]: <> (```)
