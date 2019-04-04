package application;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class RoomsController {

    private static final String template = "These are the available rooms: %s";
    private final AtomicLong counter = new AtomicLong();
    RoomsMapper mapper = new RoomsMapper();
    Rooms rooms;


    @RequestMapping(method = RequestMethod.GET, value="/rooms")

    @ResponseBody
    public Room[] returnAllRooms(){
        try {
            rooms = mapper.readJsonWithObjectMapper();
        }catch (IOException e){
            e.printStackTrace();
        }
        return rooms.getRooms();
    }
}
