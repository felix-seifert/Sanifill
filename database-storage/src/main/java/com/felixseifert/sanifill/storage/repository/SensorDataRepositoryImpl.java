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

package com.felixseifert.sanifill.storage.repository;

import com.felixseifert.sanifill.storage.model.SensorData;
import com.felixseifert.sanifill.storage.model.SensorDataIncoming;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ApplicationScoped
public class SensorDataRepositoryImpl implements SensorDataRepository {

    @Inject
    EntityManager entityManager;

    @Override
    public SensorData addIncoming(SensorDataIncoming sensorDataIncoming) {
        SensorData sensorData = SensorData.constructFromSensorDataIncoming(sensorDataIncoming);
        entityManager.persist(sensorData);
        return sensorData;
    }

    @Override
    public List<SensorData> findNLatestOfEachSensor(int n) {
        return retrieveAllSensorIds()
                .flatMap(sensorId -> retrieveNLatestSensorDataForSensorId(n, sensorId))
                .collect(Collectors.toList());
    }

    private Stream<SensorData> retrieveNLatestSensorDataForSensorId(int n, String sensorId) {
        return entityManager
                .createQuery(createQueryToRetrieveOrderedSensorDataForSensorId(sensorId))
                .setMaxResults(n)
                .getResultStream();
    }

    private CriteriaQuery<SensorData> createQueryToRetrieveOrderedSensorDataForSensorId(String sensorId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<SensorData> cq = cb.createQuery(SensorData.class);
        Root<SensorData> root = cq.from(SensorData.class);
        return cq.where(cb.equal(root.get("sensorId"), sensorId)).orderBy(cb.desc(root.get("dateTime")));
    }

    private Stream<String> retrieveAllSensorIds() {
        return entityManager
                .createQuery(createQueryToRetrieveAllSensorIds())
                .getResultStream();
    }

    private CriteriaQuery<String> createQueryToRetrieveAllSensorIds() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<String> cq = cb.createQuery(String.class);
        Root<SensorData> root = cq.from(SensorData.class);
        return cq.multiselect(root.get("sensorId")).groupBy(root.get("sensorId"));
    }
}
