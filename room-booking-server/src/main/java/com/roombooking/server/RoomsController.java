package com.roombooking.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

@RestController
public class RoomsController {

    private static final Logger LOGGER = Logger.getLogger(RoomsController.class.getName());

    // Constructor-injected class attributes
    private Rooms rooms;
    private RoomsMapper mapper;

    @Autowired
    public RoomsController(Rooms rooms, RoomsMapper mapper) {
        this.rooms = rooms;
        this.mapper = mapper;
    }

    /**
     * Returns the entire week timetable for each room in the application.
     * @return A completable future object which will contain the mapping from
     * The String name of a room to a room Object.
     */
    @Async
    @RequestMapping(method = RequestMethod.GET, value={"/","/rooms"})
    @ResponseBody
    public CompletableFuture<HashMap<String, Room>> returnAllRooms(){
        try {
            rooms = mapper.readJsonWithObjectMapper();
            return CompletableFuture.completedFuture(rooms.getRooms());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Filter by roomID to give the rooms timetable for the entire week
     * @param roomID String representing the room name
     * @return Returns a specific rooms time slot listings.
     */
    @Async
    @RequestMapping(method = RequestMethod.GET, value ="/rooms/{roomID}")
    @ResponseBody
    public CompletableFuture<Room> returnRoom(@PathVariable String roomID){
        try{
            rooms = mapper.readJsonWithObjectMapper();
            return CompletableFuture.completedFuture(rooms.getRoom(roomID));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Filter by roomID and day to return the timetable for that entire day.
     * @param roomID String parameter indicative of the room's name.
     * @param day int parameter representing the day (0 - 6)
     * @return Returns a specific Day object
     */
    @Async
    @RequestMapping(method = RequestMethod.GET, value = "/rooms/{roomID}/{day}")
    @ResponseBody
    public CompletableFuture<Object> returnDay(@PathVariable String roomID, @PathVariable int day){
        try {
            rooms = mapper.readJsonWithObjectMapper();
            return CompletableFuture.completedFuture(rooms.returnDay(roomID, day));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Takes roomID, day and time to issue a new booking and decrements the available slots for that time/day.
     * Post is only allowed when submitting a form client side. If arguments are only provided as pathvariables this
     * will fail and say that method GET is not allowed.
     * @param roomID The room name
     * @param day The day of the week in integer representation (0-6)
     * @param time The time of the day as a String
     * @return Returns the updated room after the booking has been made.
     * Alternatively, if the room is full the same room will be returned without the booking.
     */
    @Async
    @RequestMapping(method = RequestMethod.PUT, value = "/rooms/{roomID}/{day}/{time}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public CompletableFuture<Room> bookDay(@RequestBody String bookingRequest,
                                           @PathVariable String roomID,
                                           @PathVariable int day,
                                           @PathVariable String time){
        rooms.updateBooking(roomID,day,time);
        mapper.writeJsonWithObjectMapper(rooms);
        LOGGER.info(bookingRequest);
        return CompletableFuture.completedFuture(rooms.getRoom(roomID));
    }

    /**
     * Returns the if a room is available.
     * @param roomID The room name
     * @param day The day of the week in integer representation (0-6)
     * @param time The time of the day as a String
     * @return Boolean checking if a room is available at a specific time on a specific day.
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


