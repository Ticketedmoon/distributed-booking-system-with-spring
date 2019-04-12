package application;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Room {

    private AtomicInteger capacity;
    private ArrayList<Days> days;

    public int getCapacity(){
        return capacity.get();
    }

    public void setCapacity(){
        this.capacity = capacity;
    }

    public ArrayList<Days> getDays(){
        return days;
    }

    public void setDays(){
        this.days = days;
    }

}
