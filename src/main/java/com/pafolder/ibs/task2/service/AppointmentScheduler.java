package com.pafolder.ibs.task2.service;

import com.pafolder.ibs.task2.model.Specialist;
import com.pafolder.ibs.task2.model.TimeSlotOfSpecialist;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class AppointmentScheduler {
    public static final int MAX_NANO_OF_SECOND = 999_999_999;
    private final Set<Specialist> specialists = new HashSet<>();
    private final List<TimeSlotOfSpecialist> initialFreeTimeSlotsOfSpecialists = new ArrayList<>();
    private final List<TimeSlotOfSpecialist> currentTimeSlots = new LinkedList<>();

    public boolean addFreeTimeSlotOfSpecialist(String specialistName,
                                               LocalDateTime startDateTime, LocalDateTime endDateTime) {
        Specialist specialist = getSpecialistByName(specialistName);
        if (specialist == null || !endDateTime.isAfter(startDateTime)) return false;
        TimeSlotOfSpecialist freeTimeSlotOfSpecialist =
                new TimeSlotOfSpecialist(specialist, startDateTime, endDateTime, true);
        initialFreeTimeSlotsOfSpecialists.add(freeTimeSlotOfSpecialist);
        return currentTimeSlots.add(freeTimeSlotOfSpecialist);
    }

    public List<TimeSlotOfSpecialist> getAvailableTimeSlotsOfSpecialist(String specialistName,
                                                                        LocalDateTime startDateTime,
                                                                        LocalDateTime endDateTime) {
        TimeSlotOfSpecialist timeSlot = new TimeSlotOfSpecialist(null, startDateTime, endDateTime, false);
        List<TimeSlotOfSpecialist> result = new ArrayList<>();
        currentTimeSlots.stream()
                .filter(TimeSlotOfSpecialist::isFree)
                .filter(s -> specialistName.equals(s.specialist().name()))
                .filter(s -> s.isDurationWithinTimeSlot(timeSlot))
                .map(s -> createAdjustedTimeSlot(s, endDateTime))
                .filter(s -> s.startDateTime().isBefore(s.endDateTime()))
                .forEach(result::add);
        return result;
    }

    private TimeSlotOfSpecialist createAdjustedTimeSlot(TimeSlotOfSpecialist slot, LocalDateTime endDateTime) {
        LocalDateTime adjustedEndTime = (slot.endDateTime().isAfter(endDateTime) ?
                endDateTime : slot.endDateTime()).minusMinutes(slot.specialist().appointmentDurationInMinutes());
        return new TimeSlotOfSpecialist(slot.specialist(),
                slot.startDateTime(), adjustedEndTime, slot.isFree());
    }

    public List<TimeSlotOfSpecialist> getInitialTimeSlotsOfSpecialist(String specialistName, LocalDate date) {
        TimeSlotOfSpecialist fullDayTimeSlot = getFullDayTimeSlot(specialistName, date);
        return initialFreeTimeSlotsOfSpecialists.stream()
                .filter(s -> specialistName.equals(s.specialist().name()))
                .filter(t -> t.isDurationWithinTimeSlot(fullDayTimeSlot))
                .collect(Collectors.toList());
    }

    public boolean addSpecialist(Specialist specialist) {
        return specialists.add(specialist);
    }

    private TimeSlotOfSpecialist getFullDayTimeSlot(String specialistName, LocalDate dateTime) {
        return new TimeSlotOfSpecialist(getSpecialistByName(specialistName),
                LocalDateTime.of(dateTime.getYear(), dateTime.getMonth(),
                        dateTime.getDayOfMonth(), 0, 0),
                LocalDateTime.of(dateTime.getYear(), dateTime.getMonth(), dateTime.getDayOfMonth(),
                        23, 59, 59, MAX_NANO_OF_SECOND), true);
    }

    private Specialist getSpecialistByName(String name) {
        return specialists.stream().filter(s -> name.equals(s.name())).findFirst().orElse(null);
    }

}