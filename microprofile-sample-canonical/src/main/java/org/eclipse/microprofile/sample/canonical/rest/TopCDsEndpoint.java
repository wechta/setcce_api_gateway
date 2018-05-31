/*
 * Copyright (C) 2016, 2017 Antonio Goncalves and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.eclipse.microprofile.sample.canonical.rest;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.sample.canonical.utils.QLogger;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

@Path("/ccc")
@RequestScoped
public class TopCDsEndpoint {

    @Inject
    @QLogger
    private Logger logger;

    @Context
    HttpServletRequest request;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getTopCDs() {

        logger.info("request: "+ request.toString() );

        final JsonArrayBuilder array = Json.createArrayBuilder();
        final List<Integer> randomCDs = getRandomNumbers();
        for (final Integer randomCD : randomCDs) {
            array.add(Json.createObjectBuilder().add("id", randomCD));
        }
        return array.build().toString();
    }

    @POST
    @Path("/registration")
    @Produces(MediaType.APPLICATION_JSON)
    public Response registration(@FormParam("attributes") String attributes) {
        String json = "{\"id\": 123456, \"title\": \"Fun with JSON-Processing\", \"published\": true}";
        JsonReader jsonReader = Json.createReader(new StringReader(json));
        JsonObject jsonObject = jsonReader.readObject();
        jsonReader.close();
        return prepareResponse(jsonObject, 200, "registration", logger);
    }

    private static Response prepareResponse(Object entity, int code, String logMsg, Logger log){
        if(logMsg!=null && !logMsg.isEmpty()){
            log.info(logMsg);
        }
        Response.ResponseBuilder response = Response.ok().header("X-Content-Type-Option", "nosniff");
        response.entity(entity);
        response.status(code);
        return response.build();
    }

    private List<Integer> getRandomNumbers() {
        final List<Integer> randomCDs = new ArrayList<>();
        final Random r = new Random();
        randomCDs.add(r.nextInt(100) + 1101);
        randomCDs.add(r.nextInt(100) + 1101);
        randomCDs.add(r.nextInt(100) + 1101);
        randomCDs.add(r.nextInt(100) + 1101);
        randomCDs.add(r.nextInt(100) + 1101);

        logger.info("Top CDs are " + randomCDs);

        return randomCDs;
    }
}
