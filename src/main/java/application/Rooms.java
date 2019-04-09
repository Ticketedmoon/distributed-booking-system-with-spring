package application;

import java.util.ArrayList;
import java.util.HashMap;

public class Rooms {
    private HashMap <String, Room> rooms;
    public ArrayList<String> bookings = new ArrayList<>();

    public HashMap<String, Room> getRooms(){
        return rooms;
    }

    public void setRooms(){
        this.rooms = rooms;
    }

    public ArrayList<String> getBookings(){ return bookings;}

    public void setBookings() {this.bookings = bookings;}

    public void addBooking(String s)
    {
        bookings.add(s);
    }
    public Room getRoom(String roomName){
        return rooms.get(roomName);
    }

    public Object returnDay(String roomName, int dayOfWeek){
        return rooms.get(roomName).getDays().get(dayOfWeek);
    }

    public HashMap <String, Room> updateBooking(String roomName, int dayOfWeek, String timeslot, String bookingRequest)
    {
        HashMap<String, Room> updatedRooms = rooms;
        addBooking(bookingRequest);
        updatedRooms.get(roomName)
                .getDays().get(dayOfWeek)
                .updateTimeSlotCapacity(timeslot);
        return updatedRooms;
    }
}
