# analyser

The Quarkus service `analyser` consumes messages from the channel `sensors`, performs some analytics on them and 
publishes the results on another channel. Currently, the service calculates the gradient of the content which is 
measured by each sensor. Then, the service forms a simple moving average (SMA) of these gradients.

## Run

To run the `analyser` in Quarkus development mode without the need to separate build and run, you can use the Quarkus 
plugin via the Maven wrapper.

```shell script
./mvnw quarkus:dev -Dquarkus.http.port=<port>
```

## Simple Moving Average (SMA)

The service calculates a SMA of the gradients. The gradient assumes a linear function between two data points. These 
gradients are then stored to calculate a moving average, i.e. an average of the last few gradients (ignoring the older 
ones). Generally, the SMA calculation uses the last two gradients. If you want to change this, you can set the property 
`analyser.sma-values`. You can either set this property in the ['applications.properties'](src/main/resources/application.properties) 
or use Maven's `-D` flag. To start the service in development mode, use the following Maven command.

```shell script
./mvnw quarkus:dev -Danalyser.sma-values=<number-of-values-for-sma>
```
