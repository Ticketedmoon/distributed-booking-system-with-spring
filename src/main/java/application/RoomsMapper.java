package application;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class RoomsMapper {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    public Rooms readJsonWithObjectMapper() throws IOException
    {
        //TODO sort out pathing/resource folder for json file
        //TODO sort out proper representation for all classes.
        ObjectMapper objectMapper = new ObjectMapper();
        Rooms classRooms = objectMapper.readValue(new File("/home/ark/Documents/room-booking-distributed-assignment/src/rooms.json"), Rooms.class);
        logger.info(classRooms.toString());
        return classRooms;
    }
}
