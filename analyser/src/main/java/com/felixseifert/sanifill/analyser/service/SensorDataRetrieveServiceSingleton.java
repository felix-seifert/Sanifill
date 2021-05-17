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

import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.rest.client.RestClientBuilder;

import javax.ws.rs.core.UriBuilder;
import java.util.Objects;
import java.util.Optional;

public class SensorDataRetrieveServiceSingleton {

    private static SensorDataRetrieveService serviceSingleton;

    private SensorDataRetrieveServiceSingleton() {}

    public static SensorDataRetrieveService getInstance() {
        if(Objects.isNull(serviceSingleton)) {
            synchronized(SensorDataRetrieveServiceSingleton.class) {
                Optional<Integer> storagePort = getStoragePort();
                if(Objects.isNull(serviceSingleton) && storagePort.isPresent()) {
                    serviceSingleton = createSensorDataRetrieveService(storagePort.get());
                }
            }
        }
        // Can be null if no port is given!
        return serviceSingleton;
    }

    private static SensorDataRetrieveService createSensorDataRetrieveService(Integer port) {
        return RestClientBuilder.newBuilder()
                .baseUri(UriBuilder
                        .fromPath("/api/v1/sensors")
                        .scheme("http")     // HTTP only because local
                        .host("localhost")
                        .port(port).build())
                .build(SensorDataRetrieveService.class);
    }

    private static Optional<Integer> getStoragePort() {
        return ConfigProvider.getConfig().getOptionalValue("sanifill.storage-service.port", Integer.class);
    }
}
