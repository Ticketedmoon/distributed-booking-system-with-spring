package com.roombooking.server;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashMap;

public class Rooms {

    @JsonProperty
    @SuppressWarnings("unused")
    private HashMap <String, Room> rooms;

    public HashMap<String, Room> getRooms(){
        return rooms;
    }

    public Room getRoom(String roomName){
        return rooms.get(roomName);
    }

    public Object returnDay(String roomName, int dayOfWeek) {
        return rooms.get(roomName).getDays().get(dayOfWeek);
    }

    public void updateBooking(String roomName, int dayOfWeek, String timeSlot) {
        HashMap<String, Room> updatedRooms = rooms;
        updatedRooms.get(roomName)
                .getDays().get(dayOfWeek)
                .updateTimeSlotCapacity(timeSlot);
    }
}
