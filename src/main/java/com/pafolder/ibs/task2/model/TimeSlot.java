package com.pafolder.ibs.task2.model;

import java.time.LocalDateTime;

public record TimeSlot(Specialist specialist, int appointmentDuration,
                       LocalDateTime startDateTime, LocalDateTime endDateTime, boolean isFree) {

    public boolean isDurationWithinTimeSlot(TimeSlot other) {
        return startDateTime.isAfter(other.startDateTime) &&
                startDateTime.plusMinutes(appointmentDuration()).isBefore(other.endDateTime);
    }
}
