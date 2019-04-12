package application;

import java.util.HashMap;

public class Rooms {
    private HashMap <String, Room> rooms;

    public HashMap<String, Room> getRooms(){
        return rooms;
    }

    public void setRooms(){
        this.rooms = rooms;
    }

    public Room getRoom(String roomName){
        return rooms.get(roomName);
    }

    public Object returnDay(String roomName, int dayOfWeek){
        return rooms.get(roomName).getDays().get(dayOfWeek);
    }

    public HashMap <String, Room> updateBooking(String roomName, int dayOfWeek, String timeslot)
    {
        HashMap<String, Room> updatedRooms = rooms;
        updatedRooms.get(roomName)
                .getDays().get(dayOfWeek)
                .updateTimeSlotCapacity(timeslot);
        return updatedRooms;
    }
}
