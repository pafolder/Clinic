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
        return specialist.name() + " " + timeSlotWithoutNameToString(this);
    }

    public static String timeSlotWithoutNameToString(TimeSlot timeSlot) {
        return timeSlot.startDateTime.getDayOfMonth() + "." +
                timeSlot.startDateTime.getMonth().getValue() + "." +
                timeSlot.startDateTime.getYear() + " " +
                String.format(TIME_2DIGITS_FORMAT, timeSlot.startDateTime.getHour()) + ":" +
                String.format(TIME_2DIGITS_FORMAT, timeSlot.startDateTime.getMinute()) + " … " +
                String.format(TIME_2DIGITS_FORMAT, timeSlot.endDateTime.getHour()) + ":" +
                String.format(TIME_2DIGITS_FORMAT, timeSlot.endDateTime.getMinute());
    }

}
