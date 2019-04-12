package application;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Days {

    private String day;
    private ConcurrentHashMap <String, AtomicInteger> timeslotCapacity;

    public String getDay(){
        return day;
    }

    public void setDay(){
        this.day = day;
    }

    public ConcurrentHashMap<String, AtomicInteger> getTimeslotCapacity()
    {
        return timeslotCapacity;
    }

    public void setTimes()
    {
        this.timeslotCapacity = timeslotCapacity;
    }

    public ConcurrentHashMap<String, AtomicInteger> updateTimeSlotCapacity(String timeslot)
    {
        ConcurrentHashMap<String, AtomicInteger> newTimeslots = timeslotCapacity;
        newTimeslots.get(timeslot).updateAndGet(capacity -> capacity > 0 ? capacity - 1 : capacity);
        return newTimeslots;
    }

    public int getTimeSlotCapacityForDay(String timeSlot) {
        return timeslotCapacity.get(timeSlot).get();
    }
}
