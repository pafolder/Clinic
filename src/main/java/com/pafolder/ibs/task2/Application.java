package com.pafolder.ibs.task2;

import java.time.LocalDateTime;
import java.util.List;

import static com.pafolder.ibs.task2.TestData.DR_IVANOV;
import static com.pafolder.ibs.task2.TestData.timeSlotsOfIvanov;

public class Application {
    private static final AppointmentScheduler scheduler = new AppointmentScheduler();
    ;

    public static void main(String[] args) {
        initializeData();
        List<TimeSlot> freeTimeSlots = scheduler.getAvailableTimeSlotsOfSpecialist(DR_IVANOV,
                LocalDateTime.of(2024, 8, 15, 7, 0),
                LocalDateTime.of(2024, 8, 15, 15, 0));
    }

    static void initializeData() {
        scheduler.add(new Specialist(DR_IVANOV, 45));
        timeSlotsOfIvanov.forEach(s -> {
            if (!scheduler.addFreeTimeSlotOfSpecialist(DR_IVANOV,
                    s.startDateTime(), s.endDateTime())) {
                throw new RuntimeException("Initialization Failed");
            }
        });
    }
}
