package application;

import com.fasterxml.jackson.databind.ObjectMapper;
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

    /**
     * Returns the entire week timetable for each room in the application.
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value={"/","/rooms"})
    @ResponseBody
    public HashMap<String, Room> returnAllRooms(){
        return rooms.getRooms();
    }

    /**
     * Filter by roomID to give the rooms timetable for the entire week
     * @param roomID
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value ="/rooms/{roomID}")
    @ResponseBody
    public Room returnRoom(@PathVariable String roomID){
        return rooms.getRoom(roomID);
    }

    //TODO revisit converting to map instead of arrays.

    /**
     * Filter by roomID and day to return the timetable for that entire day.
     * @param roomID
     * @param day
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/rooms/{roomID}/{day}")
    @ResponseBody
    public Object returnDay(@PathVariable String roomID,@PathVariable int day){
        return rooms.returnDay(roomID, day);
    }


    /**
     * Takes roomID, day and time to issue a new booking and decrements the available slots for that time/day.
     * Post is only allowed when submitting a form client side. If arguments are only provided as pathvariables this
     * will fail and say that method GET is not allowed.
     * @param roomID
     * @param day
     * @param time
     * @return
     */
    @RequestMapping(method = RequestMethod.PUT, value = "/rooms/{roomID}/{day}/{time}")
    @ResponseBody
    public HashMap<String, Room> bookDay(@PathVariable String roomID, @PathVariable int day, @PathVariable String time){
        //TODO Protect
        //TODO Logic for checking
        RoomsMapper mapper = new RoomsMapper();
        rooms.updateBooking(roomID,day,time);
        try {
            mapper.writeJsonWithObjectMapper(rooms);
        }catch (IOException e)
        {
            e.printStackTrace();
        }
        return rooms.getRooms();
    }
}
