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

    //TODO revisit converting to map instead of arrays.
    @RequestMapping(method = RequestMethod.GET, value = "/rooms/{roomID}/{day}")
    @ResponseBody
    public Object returnDay(@PathVariable String roomID,@PathVariable int day){
        return rooms.returnDay(roomID, day);
    }

    //TODO PLACEHOLDER. IS GET FOR TESTING. When we have an input form client side we can use POST
    @RequestMapping(method = RequestMethod.GET, value = "/rooms/{roomID}/{day}/{time}")
    @ResponseBody
    public String bookDay(@PathVariable String roomID, @PathVariable int day, @PathVariable String time){
        return "Booked";
    }
}
