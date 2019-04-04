package application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class RoomsController {

    private static final String template = "These are the available rooms: %s";
    private final AtomicLong counter = new AtomicLong();

    @Autowired
    private Rooms rooms;

    @RequestMapping(method = RequestMethod.GET, value="/rooms")
    @ResponseBody
    public HashMap<String, Room> returnAllRooms(){
        return rooms.getRooms();
    }

    @RequestMapping(method = RequestMethod.GET, value ="/rooms/L221")
    @ResponseBody
    public Room returnRoom(){
        return returnRoom();
    }

}
