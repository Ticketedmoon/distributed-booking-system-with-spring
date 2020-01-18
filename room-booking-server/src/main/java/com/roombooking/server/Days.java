package com.roombooking.server;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Days {

    @JsonProperty
    private String day;

    @JsonProperty
    private ConcurrentHashMap <String, AtomicInteger> timeSlotCapacity;

    public void updateTimeSlotCapacity(String timeSlot) {
        ConcurrentHashMap<String, AtomicInteger> newTimeSlots = timeSlotCapacity;
        newTimeSlots.get(timeSlot).updateAndGet(capacity -> capacity > 0 ? capacity - 1 : capacity);
    }

    public int getTimeSlotCapacityForDay(String timeSlot) {
        return timeSlotCapacity.get(timeSlot).get();
    }
}
