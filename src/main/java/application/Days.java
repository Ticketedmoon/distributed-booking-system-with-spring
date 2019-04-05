package application;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Days {

    private String day;
    //private int [] timeslotCapacity;
    private HashMap<String, AtomicInteger> timeslotCapacity;

    public String getDay(){
        return day;
    }

    public void setDay(){
        this.day = day;
    }

    public HashMap<String, AtomicInteger> getTimeslotCapacity()
    {
        return timeslotCapacity;
    }

    public void setTimes()
    {
        this.timeslotCapacity = timeslotCapacity;
    }

    public HashMap<String, AtomicInteger> updateTimeSlotCapacity(String timeslot)
    {
        HashMap<String, AtomicInteger> newTimeslots = timeslotCapacity;
        newTimeslots.get(timeslot).getAndDecrement();
        return newTimeslots;
    }
}
