package com.pafolder.ibs.task2.service;

import com.pafolder.ibs.task2.model.Specialist;
import com.pafolder.ibs.task2.model.TimeSlot;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class TimeSlotScheduler {
    public static final int MAX_NANO_OF_SECOND = 999_999_999;

    private final Set<Specialist> specialists = new HashSet<>();
    private final List<TimeSlot> initialFreeTimeSlotsOfSpecialists = new ArrayList<>();
    private final List<TimeSlot> scheduledTimeSlots = new LinkedList<>();

    public int getAppointmentDuration(String specialistName) {
        return getSpecialistByName(specialistName).appointmentDurationInMinutes();
    }

    public boolean addFreeTimeSlotOfSpecialist(String specialistName,
                                               LocalDateTime startDateTime, LocalDateTime endDateTime) {
        Specialist specialist = getSpecialistByName(specialistName);
        if (specialist == null || !endDateTime.isAfter(startDateTime)) return false;
        TimeSlot freeTimeSlotOfSpecialist =
                new TimeSlot(specialist, startDateTime, endDateTime, true);
        initialFreeTimeSlotsOfSpecialists.add(freeTimeSlotOfSpecialist);
        return scheduledTimeSlots.add(freeTimeSlotOfSpecialist);
    }

    public List<TimeSlot> getAvailableTimeSlotsOfSpecialist(String specialistName,
                                                            LocalDateTime startDateTime,
                                                            LocalDateTime endDateTime)
            throws IllegalArgumentException {
        TimeSlot timeSlot = new TimeSlot(null, startDateTime, endDateTime, false);
        List<TimeSlot> result = new ArrayList<>();
        scheduledTimeSlots.stream()
                .filter(TimeSlot::isFree)
                .filter(s -> getSpecialistByName(specialistName).name().equals(s.specialist().name()))
                .filter(s -> s.isDurationWithinTimeSlot(timeSlot))
                .map(s -> createAdjustedTimeSlot(s, endDateTime))
                .filter(s -> s.startDateTime().isBefore(s.endDateTime()))
                .forEach(result::add);
        return result;
    }

    private TimeSlot createAdjustedTimeSlot(TimeSlot slot, LocalDateTime endDateTime) {
        LocalDateTime adjustedEndTime = (slot.endDateTime().isAfter(endDateTime) ?
                endDateTime : slot.endDateTime()).minusMinutes(slot.specialist().appointmentDurationInMinutes());
        return new TimeSlot(slot.specialist(),
                slot.startDateTime(), adjustedEndTime, slot.isFree());
    }

    public List<TimeSlot> getInitialTimeSlotsOfSpecialist(String specialistName, LocalDate date)
            throws IllegalArgumentException {
        TimeSlot fullDayTimeSlot = getFullDayTimeSlot(specialistName, date);
        return initialFreeTimeSlotsOfSpecialists.stream()
                .filter(s -> specialistName.equals(s.specialist().name()))
                .filter(t -> t.isDurationWithinTimeSlot(fullDayTimeSlot))
                .collect(Collectors.toList());
    }

    public boolean addSpecialist(Specialist specialist) {
        return specialists.add(specialist);
    }

    private TimeSlot getFullDayTimeSlot(String specialistName, LocalDate dateTime) {
        return new TimeSlot(getSpecialistByName(specialistName),
                LocalDateTime.of(dateTime.getYear(), dateTime.getMonth(),
                        dateTime.getDayOfMonth(), 0, 0),
                LocalDateTime.of(dateTime.getYear(), dateTime.getMonth(), dateTime.getDayOfMonth(),
                        23, 59, 59, MAX_NANO_OF_SECOND), true);
    }

    private Specialist getSpecialistByName(String name) throws IllegalArgumentException {
        return specialists.stream().filter(s -> name.equals(s.name())).findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}