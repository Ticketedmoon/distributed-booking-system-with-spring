package application;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;

@RestController
public class RoomsController {

    @Autowired
    private Rooms rooms;
    RoomsMapper mapper = new RoomsMapper();
    /**
     * Returns the entire week timetable for each room in the application.
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value={"/","/rooms"})
    @ResponseBody
    public HashMap<String, Room> returnAllRooms(){
        try{
            rooms = mapper.readJsonWithObjectMapper();
            return rooms.getRooms();
        }catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Filter by roomID to give the rooms timetable for the entire week
     * @param roomID
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value ="/rooms/{roomID}")
    @ResponseBody
    public Room returnRoom(@PathVariable String roomID){
        try{
            rooms = mapper.readJsonWithObjectMapper();
            return rooms.getRoom(roomID);
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
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
        try {
            rooms = mapper.readJsonWithObjectMapper();
            return rooms.returnDay(roomID, day);
        }catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
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
        mapper.writeJsonWithObjectMapper(rooms);

        return rooms.getRooms();
    }

    /**
     * Returns the if a room is availabe.
     * @return Booleam
     */
    @RequestMapping(method = RequestMethod.GET, value = "/rooms/{roomID}/{day}/{time}")
    @ResponseBody
    public boolean roomAvailableAtSpecificTime(@PathVariable String roomID, @PathVariable int day, @PathVariable String time){
        return rooms.getRoom(roomID).getDays().get(day).getTimeSlotCapacityForDay(time) > 0;
    }
}


