/*
 * Copyright (C) 2021 Felix Seifert <mail@felix-seifert.com> (https://felix-seifert.com)
 *
 * This programme is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or any
 * later version.
 *
 * This programme is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.felixseifert.sanifill.sensor.sensor;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Singleton
public class SensorImpl implements Sensor {

    private static final Logger LOGGER = Logger.getLogger(SensorImpl.class);

    @ConfigProperty(name = "quarkus.http.host")
    String sensorAddress;

    @ConfigProperty(name = "quarkus.http.port")
    int sensorPort;

    @ConfigProperty(name = "sensor.id")
    Optional<String> cmdSensorId;

    private double currentFilling;

    private String sensorId;

    @PostConstruct
    public void postConstruct() {
        currentFilling = ThreadLocalRandom.current().nextDouble(0.0, 1.0);
        sensorId = createSensorId();
        LOGGER.infov("Sensor created with ID {0} and filling {1}.", sensorId, currentFilling);
    }

    private String createSensorId() {
        return cmdSensorId.orElse(UUID.randomUUID().toString());
    }

    @Override
    public String getSensorId() {
        return sensorId;
    }

    @Override
    public SensorData getCurrentData() {
        return new SensorData(sensorId, sensorAddress, sensorPort, LocalDateTime.now(), getCurrentFilling());
    }

    private double getCurrentFilling() {
        if(currentFilling > 0) {
            reduceCurrentFilling();
        }
        return Math.max(0.0, currentFilling);
    }

    private void reduceCurrentFilling() {
        currentFilling -= (1.0 / (ThreadLocalRandom.current().nextInt(100, 160)));
    }

    @Override
    public SensorData resetData() {
        currentFilling = 1;
        return getCurrentData();
    }
}
