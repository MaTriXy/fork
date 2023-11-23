/*
 * Copyright 2019 Apple Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License.
 *
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package com.shazam.fork.injector;

import com.google.gson.*;
import com.shazam.fork.ComputedPooling;

import java.time.Instant;

import static com.shazam.fork.ComputedPooling.Characteristic.valueOf;

public class GsonInjector {
    private static final Gson GSON;
    private static final GsonBuilder GSON_BUILDER = new GsonBuilder();

    static {
        GSON_BUILDER.registerTypeAdapter(ComputedPooling.Characteristic.class, characteristicDeserializer());
        GSON_BUILDER.registerTypeAdapter(ComputedPooling.Characteristic.class, characteristicSerializer());
        GSON_BUILDER.registerTypeAdapter(Instant.class, instantSerializer());
        GSON_BUILDER.registerTypeAdapter(Instant.class, instantDeserializer());
        GSON = GSON_BUILDER.create();
    }

    private GsonInjector() {}

    private static JsonSerializer<ComputedPooling.Characteristic> characteristicSerializer() {
        return (src, typeOfSrc, context) -> new JsonPrimitive(src.name());
    }

    private static JsonDeserializer<ComputedPooling.Characteristic> characteristicDeserializer() {
        return (json, typeOfT, context) -> valueOf(json.getAsJsonPrimitive().getAsString());
    }

    private static JsonSerializer<Instant> instantSerializer() {
        return (src, typeOfSrc, context) -> new JsonPrimitive(src.toEpochMilli());
    }

    private static JsonDeserializer<Instant> instantDeserializer() {
        return (json, typeOfT, context) -> Instant.ofEpochMilli(json.getAsJsonPrimitive().getAsLong());
    }

    public static Gson gson() {
        return GSON;
    }
}
