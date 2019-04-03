package application;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class RoomsController {

    private static final String template = "These are the available rooms: %s";
    private final AtomicLong counter = new AtomicLong();
    Rooms room = new Rooms();
    @RequestMapping(method = RequestMethod.GET, value="/rooms")

    @ResponseBody
    public String returnAllRooms(){
        return room.getRooms();
    }
}
