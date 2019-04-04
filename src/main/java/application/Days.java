package application;

import java.util.HashMap;

public class Days {

    private String day;
    //private int [] timeslotCapacity;
    private HashMap<String, Integer> timeslotCapacity;

    public String getDay(){
        return day;
    }

    public void setDay(){
        this.day = day;
    }

    public HashMap<String, Integer> getTimeslotCapacity()
    {
        return timeslotCapacity;
    }

    public void setTimes()
    {
        this.timeslotCapacity = timeslotCapacity;
    }
}
