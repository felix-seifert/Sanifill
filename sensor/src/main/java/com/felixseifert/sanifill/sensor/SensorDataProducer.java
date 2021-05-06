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

package com.felixseifert.sanifill.sensor;

import com.felixseifert.sanifill.sensor.sensor.Sensor;
import com.felixseifert.sanifill.sensor.sensor.SensorData;
import io.smallrye.mutiny.Multi;
import io.smallrye.reactive.messaging.annotations.Broadcast;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.Duration;

@ApplicationScoped
public class SensorDataProducer {

    private static final Logger LOGGER = Logger.getLogger(SensorDataProducer.class);

    @Inject
    Sensor sensor;

    @Outgoing("sensors")
    @Broadcast
    public Multi<SensorData> produceSensorData() {
        return Multi.createFrom()
                .ticks().every(Duration.ofSeconds(20))
                .onOverflow().drop()
                .map(this::publishSensorData);
    }

    private SensorData publishSensorData(Long tick) {
        SensorData sensorData = sensor.getCurrentData();
        LOGGER.infov("Publish sensor data: {0}", sensorData);
        return sensorData;
    }
}
