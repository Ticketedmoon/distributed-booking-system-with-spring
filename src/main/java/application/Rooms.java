package application;

import java.util.HashMap;

public class Rooms {
    //private Room [] rooms;
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
}
