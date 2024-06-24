package com.pafolder.ibs.task2.service;

import com.pafolder.ibs.task2.model.Specialist;
import com.pafolder.ibs.task2.model.TimeSlot;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class TimeSlotScheduler<T extends Specialist> {
    public static final int MAX_NANO_OF_SECOND = 999_999_999;

    private final Set<T> specialists = new HashSet<>();
    private final List<TimeSlot> initialFreeTimeSlotsOfSpecialists = new ArrayList<>();
    private final List<TimeSlot> scheduledTimeSlots = new LinkedList<>();

    public boolean addFreeTimeSlotOfSpecialist(String specialistName, int appointmentDuration,
                                               LocalDateTime startDateTime, LocalDateTime endDateTime) {
        Specialist specialist = getSpecialistByName(specialistName);
        if (specialist == null || !endDateTime.isAfter(startDateTime)) return false;
        TimeSlot freeTimeSlotOfSpecialist =
                new TimeSlot(specialist, appointmentDuration, startDateTime, endDateTime, true);
        initialFreeTimeSlotsOfSpecialists.add(freeTimeSlotOfSpecialist);
        return scheduledTimeSlots.add(freeTimeSlotOfSpecialist);
    }

    public List<TimeSlot> getAvailableTimeSlotsOfSpecialist(String specialistName,
                                                            LocalDateTime startDateTime, LocalDateTime endDateTime)
            throws IllegalArgumentException {
        TimeSlot timeSlot = new TimeSlot(getSpecialistByName(specialistName), 0,
                startDateTime, endDateTime, false);
        List<TimeSlot> result = new ArrayList<>();
        scheduledTimeSlots.stream()
                .filter(TimeSlot::isFree)
                .filter(s -> getSpecialistByName(specialistName).getName().equals(s.specialist().getName()))
                .filter(s -> s.isDurationWithinTimeSlot(timeSlot))
                .map(s -> createAdjustedTimeSlot(s, endDateTime))
                .filter(s -> s.startDateTime().isBefore(s.endDateTime()))
                .forEach(result::add);
        return result;
    }

    private TimeSlot createAdjustedTimeSlot(TimeSlot slot, LocalDateTime endDateTime) {
        LocalDateTime adjustedEndTime = (slot.endDateTime().isAfter(endDateTime) ?
                endDateTime : slot.endDateTime()).minusMinutes(slot.appointmentDuration());
        return new TimeSlot(slot.specialist(), slot.appointmentDuration(),
                slot.startDateTime(), adjustedEndTime, slot.isFree());
    }

    public List<TimeSlot> getInitialTimeSlotsOfSpecialist(String specialistName, LocalDate date)
            throws IllegalArgumentException {
        TimeSlot fullDayTimeSlot = getFullDayTimeSlot(specialistName, date);
        return initialFreeTimeSlotsOfSpecialists.stream()
                .filter(s -> specialistName.equals(s.specialist().getName()))
                .filter(t -> t.isDurationWithinTimeSlot(fullDayTimeSlot))
                .collect(Collectors.toList());
    }

    public boolean addSpecialist(T specialist) {
        return specialists.add(specialist);
    }

    private TimeSlot getFullDayTimeSlot(String specialistName, LocalDate dateTime) {
        int UNDEFINED = -1;
        return new TimeSlot(getSpecialistByName(specialistName), UNDEFINED,
                LocalDateTime.of(dateTime.getYear(), dateTime.getMonth(),
                        dateTime.getDayOfMonth(), 0, 0),
                LocalDateTime.of(dateTime.getYear(), dateTime.getMonth(), dateTime.getDayOfMonth(),
                        23, 59, 59, MAX_NANO_OF_SECOND), true);
    }

    private Specialist getSpecialistByName(String name) throws IllegalArgumentException {
        return specialists.stream().filter(s -> name.equals(s.getName())).findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}