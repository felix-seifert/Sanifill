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

package com.felixseifert.sanifill.analyser.service;

import com.felixseifert.sanifill.analyser.model.SensorData;
import com.felixseifert.sanifill.analyser.model.SensorDataSma;
import io.vertx.mutiny.core.eventbus.EventBus;
import io.vertx.mutiny.core.eventbus.Message;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Objects;

@ApplicationScoped
public class KafkaListener {

    private static final Logger LOGGER = Logger.getLogger(KafkaListener.class);

    @Inject
    EventBus eventBus;

    @Inject
    @Channel("sensors-sma")
    Emitter<SensorDataSma> smaEmitter;

    @Incoming("sensors")
    public void analyseIncomingData(SensorData sensorData) {
        LOGGER.infov("Received SensorData: {0}", sensorData);
        eventBus.<SensorDataSma>request("calculateSmaOfFillingGradient", sensorData)
                .onItem()
                .transform(Message::body)
                .subscribe().with(this::publishMovingAverage);
    }

    private void publishMovingAverage(SensorDataSma sensorDataSma) {
        if(Objects.isNull(sensorDataSma.getMovingAverage())) {
            return;
        }
        LOGGER.infov("Send moving average of {0} at {1}: {2}",
                sensorDataSma.getSensorId(),
                sensorDataSma.getTimeOfAverage(),
                sensorDataSma.getMovingAverage().toString());
        smaEmitter.send(sensorDataSma);
    }
}
