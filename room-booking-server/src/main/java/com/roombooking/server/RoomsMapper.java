package com.roombooking.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RoomsMapper {

    private static final Logger logger = LoggerFactory.getLogger(RoomsMapper.class);

    public Rooms readJsonWithObjectMapper() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        File jsonFile = null;
        try {
            jsonFile = new File(RoomsMapper.class.getResource( "/data-store/rooms.json" ).toURI());
        } catch (URISyntaxException e) {
            logger.error("Room file not found, server data not loaded - Error", e);
        }
        return objectMapper.readValue(jsonFile, Rooms.class);
    }

    public void writeJsonWithObjectMapper(Rooms classRooms) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            File jsonFile = new File(RoomsMapper.class.getResource( "/data-store/rooms.json" ).toURI());
            if(!jsonFile.exists()) {
                jsonFile = new File("data-store/rooms.json");
                objectMapper.writeValue(jsonFile, classRooms);
            }
            else {
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, classRooms);
            }
        } catch (NullPointerException | IOException | URISyntaxException e) {
            logger.error("Error writing to room file", e);
        }
    }
}
