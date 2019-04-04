package application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class RoomsController {

    @Autowired
    private Rooms rooms;

    @RequestMapping(method = RequestMethod.GET, value="/")
    @ResponseBody
    public HashMap<String, Room> returnAllRooms(){
        return rooms.getRooms();
    }

    @RequestMapping(method = RequestMethod.GET, value ="/rooms/{roomID}")
    @ResponseBody
    public Room returnRoom(@PathVariable String roomID){
        return rooms.getRoom(roomID);
    }

}
