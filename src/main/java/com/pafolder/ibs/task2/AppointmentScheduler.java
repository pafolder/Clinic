package com.pafolder.ibs.task2;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class AppointmentScheduler extends HashSet<Specialist> {
    private final List<TimeSlot> initialFreeTimeSlotsOfSpecialists = new ArrayList<>();
    private final List<TimeSlot> currentTimeSlots = new LinkedList<>();

    public boolean addFreeTimeSlotOfSpecialist(String specialistName,
                                               LocalDateTime startDateTime, LocalDateTime endDateTime) {
        Specialist specialist = getSpecialistByName(specialistName);
        if (specialist == null) return false;
        TimeSlot freeTimeSlotOfSpecialist =
                new TimeSlot(specialist, startDateTime, endDateTime, true);
        initialFreeTimeSlotsOfSpecialists.add(freeTimeSlotOfSpecialist);
        return currentTimeSlots.add(freeTimeSlotOfSpecialist);
    }

    public List<TimeSlot> getAvailableTimeSlotsOfSpecialist(String specialistName,
                                                            LocalDateTime startDateTime, LocalDateTime endDateTime) {
        TimeSlot timeSlot = new TimeSlot(null, startDateTime, endDateTime, false);
        List<TimeSlot> result = new ArrayList<>();
        currentTimeSlots.stream()
                .filter(TimeSlot::isFree)
                .filter(s -> specialistName.equals(s.specialist().name()))
                .filter(s -> s.isStartsWithinTimeSlot(timeSlot))
                .map(s -> createAdjustedTimeSlot(s, endDateTime))
                .filter(s -> s.startDateTime().isBefore(s.endDateTime()))
                .forEach(result::add);
        return result;
    }

    private TimeSlot createAdjustedTimeSlot(TimeSlot slot, LocalDateTime endDateTime) {
        LocalDateTime adjustedEndTime = (slot.endDateTime().isAfter(endDateTime) ?
                endDateTime : slot.endDateTime()).minusMinutes(slot.specialist().appointmentDurationInMinutes());
        return new TimeSlot(slot.specialist(), slot.startDateTime(), adjustedEndTime, slot.isFree());
    }

    public List<TimeSlot> getInitialTimeSlotsOfSpecialist(String specialistName, LocalDateTime dateTime) {
        TimeSlot fullDayTimeSlot = getFullDayTimeSlot(specialistName, dateTime);
        return initialFreeTimeSlotsOfSpecialists.stream()
                .filter(s -> specialistName.equals(s.specialist().name()))
                .filter(t -> t.isStartsWithinTimeSlot(fullDayTimeSlot))
                .collect(Collectors.toList());
    }

    private TimeSlot getFullDayTimeSlot(String specialistName, LocalDateTime dateTime) {
        return new TimeSlot(getSpecialistByName(specialistName),
                LocalDateTime.of(dateTime.getYear(), dateTime.getMonth(),
                        dateTime.getDayOfMonth(), 0, 0),
                LocalDateTime.of(dateTime.getYear(), dateTime.getMonth(),
                        dateTime.getDayOfMonth(), 23, 59, 59, 999999999), true);
    }

    private Specialist getSpecialistByName(String name) {
        return this.stream().filter(s -> name.equals(s.name())).findFirst().orElse(null);
    }

}