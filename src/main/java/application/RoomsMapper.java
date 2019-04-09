package application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class RoomsMapper {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    public Rooms readJsonWithObjectMapper() throws IOException
    {
        //TODO sort out proper representation for all classes.
        ObjectMapper objectMapper = new ObjectMapper();
        File jsonFile = new File("src/rooms.json");
        try{

            if(!jsonFile.exists()){
                jsonFile = new File("rooms.json");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        Rooms classRooms = objectMapper.readValue(jsonFile, Rooms.class);
        return classRooms;
    }

    public void writeJsonWithObjectMapper(Rooms classRooms)
    {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        File jsonFile = new File("src/rooms.json");
        try{

            if(!jsonFile.exists()){
                jsonFile = new File("rooms.json");
                objectMapper.writeValue(jsonFile, classRooms);
            }
            else{
                objectMapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, classRooms);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
