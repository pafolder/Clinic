package com.pafolder.ibs.task2;

import java.time.LocalDateTime;

public record TimeSlot(Specialist specialist,
                       LocalDateTime startDateTime, LocalDateTime endDateTime, boolean isFree) {
    public boolean isStartsWithinTimeSlot(TimeSlot other) {
        return startDateTime.isAfter(other.startDateTime) &&
                startDateTime.plusMinutes(specialist.appointmentDurationInMinutes())
                        .isBefore(other.endDateTime);
    }

}
