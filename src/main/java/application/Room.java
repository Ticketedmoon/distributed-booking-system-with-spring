package application;

import java.util.concurrent.atomic.AtomicInteger;

public class Room {

    private String roomName;
    private AtomicInteger capacity;
    private Days [] days;


    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(){
        this.roomName = roomName;
    }

    public int getCapacity(){
        return capacity.get();
    }

    public void setCapacity(){
        this.capacity = capacity;
    }

    public Days [] getDays(){
        return days;
    }

    public void setDays(){
        this.days = days;
    }
}
