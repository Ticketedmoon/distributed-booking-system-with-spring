package com.roombooking.server;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Room {

    @JsonProperty
    @SuppressWarnings("unused")
    private AtomicInteger capacity;

    @JsonProperty
    @SuppressWarnings("unused")
    private ArrayList<Days> days;

    public ArrayList<Days> getDays(){
        return days;
    }

}
