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

package com.felixseifert.sanifill.storage.service;

import com.felixseifert.sanifill.storage.model.SensorData;
import com.felixseifert.sanifill.storage.model.SensorDataIncoming;
import com.felixseifert.sanifill.storage.repository.SensorDataRepository;
import io.quarkus.vertx.ConsumeEvent;
import io.smallrye.common.annotation.Blocking;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

@ApplicationScoped
public class PersistServiceImpl implements PersistService {

    private static final Logger LOGGER = Logger.getLogger(PersistServiceImpl.class);

    @Inject
    SensorDataRepository sensorDataRepository;

    @Override
    @ConsumeEvent("storeIncomingSensorData")
    @Blocking
    @Transactional
    public void persistIncomingSensorData(SensorDataIncoming sensorDataIncoming) {
        SensorData sensorData = sensorDataRepository.addIncoming(sensorDataIncoming);
        LOGGER.infov("Persisted SensorData: {0}", sensorData);
    }
}
