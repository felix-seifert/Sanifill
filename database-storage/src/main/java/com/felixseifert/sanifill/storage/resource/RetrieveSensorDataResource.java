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

package com.felixseifert.sanifill.storage.resource;

import com.felixseifert.sanifill.storage.model.SensorData;
import com.felixseifert.sanifill.storage.service.RetrieveService;
import io.vertx.core.http.HttpServerRequest;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("api/v1/sensors")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class RetrieveSensorDataResource {

    private static final Logger LOGGER = Logger.getLogger(RetrieveSensorDataResource.class);

    @Context
    HttpServerRequest httpServerRequest;

    @Inject
    RetrieveService retrieveService;

    @GET
    public Response getNLatest(@QueryParam("n") int n) {
        List<SensorData> latestN = retrieveService.getNLatestSensorDataOfEachSensor(n);
        LOGGER.infov("Return {0} SensorData to {1}",
                latestN.size(),
                httpServerRequest.remoteAddress().toString());
        return Response
                .status(Response.Status.OK)
                .entity(latestN).build();
    }

    @GET
    public Response getLatest() {
        List<SensorData> latest = retrieveService.getLatestSensorDataOfEachSensor();
        LOGGER.infov("Return {0} SensorData to {1}",
                latest.size(),
                httpServerRequest.remoteAddress().toString());
        return Response
                .status(Response.Status.OK)
                .entity(latest).build();
    }
}
