package com.pafolder.ibs.task2;

import java.time.LocalDateTime;

public record TimeSlot(Specialist specialist,
                       LocalDateTime startDateTime, LocalDateTime endDateTime, boolean isFree) {

    public static final String TIME_2DIGITS_FORMAT = "%02d";

    public boolean isStartsWithinTimeSlot(TimeSlot other) {
        return startDateTime.isAfter(other.startDateTime) &&
                startDateTime.plusMinutes(specialist.appointmentDurationInMinutes())
                        .isBefore(other.endDateTime);
    }

    public String toString() {
        return specialist.name() + " " +
                startDateTime.getDayOfMonth() + "." +
                startDateTime.getMonth().getValue() + "." +
                startDateTime.getYear() + " " +
                String.format(TIME_2DIGITS_FORMAT, startDateTime.getHour()) + ":" +
                String.format(TIME_2DIGITS_FORMAT, startDateTime.getMinute()) + " ... " +
                String.format(TIME_2DIGITS_FORMAT, endDateTime.getHour()) + ":" +
                String.format(TIME_2DIGITS_FORMAT, endDateTime.getMinute()) + " " +
                (isFree ? "свободно" : "занято");
    }

}
