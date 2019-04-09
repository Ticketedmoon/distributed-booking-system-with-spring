package application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

@RestController
public class RoomsController {

    @Autowired
    private Rooms rooms;
    RoomsMapper mapper = new RoomsMapper();
    /**
     * Returns the entire week timetable for each room in the application.
     * @return
     */
    @Async
    @RequestMapping(method = RequestMethod.GET, value={"/","/rooms"})
    @ResponseBody
    public CompletableFuture<HashMap<String, Room>> returnAllRooms(){
        try{
            rooms = mapper.readJsonWithObjectMapper();
            return CompletableFuture.completedFuture(rooms.getRooms());
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
    @Async
    @RequestMapping(method = RequestMethod.GET, value ="/rooms/{roomID}")
    @ResponseBody
    public CompletableFuture<Room> returnRoom(@PathVariable String roomID){
        try{
            rooms = mapper.readJsonWithObjectMapper();
            return CompletableFuture.completedFuture(rooms.getRoom(roomID));
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
    @Async
    @RequestMapping(method = RequestMethod.GET, value = "/rooms/{roomID}/{day}")
    @ResponseBody
    public CompletableFuture<Object> returnDay(@PathVariable String roomID,@PathVariable int day){
        try {
            rooms = mapper.readJsonWithObjectMapper();
            return CompletableFuture.completedFuture(rooms.returnDay(roomID, day));
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
    @Async
    @RequestMapping(method = RequestMethod.PUT, value = "/rooms/{roomID}/{day}/{time}")
    @ResponseBody
    public CompletableFuture<Room> bookDay(@PathVariable String roomID, @PathVariable int day, @PathVariable String time){
        //TODO Protect
        //TODO Logic for checking
        RoomsMapper mapper = new RoomsMapper();
        rooms.updateBooking(roomID,day,time);
        mapper.writeJsonWithObjectMapper(rooms);
        return CompletableFuture.completedFuture(rooms.getRoom(roomID));
    }

    /**
     * Returns the if a room is availabe.
     * @return Boolean
     */
    @RequestMapping(method = RequestMethod.GET, value = "/rooms/{roomID}/{day}/{time}")
    @ResponseBody
    public boolean roomAvailableAtSpecificTime(@PathVariable String roomID, @PathVariable int day, @PathVariable String time){
        return rooms.getRoom(roomID)
                .getDays()
                .get(day)
                .getTimeSlotCapacityForDay(time) > 0;
    }
}


