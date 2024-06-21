package com.pafolder.ibs.task2;

import com.pafolder.ibs.task2.model.Specialist;
import com.pafolder.ibs.task2.model.TimeSlotOfSpecialist;
import com.pafolder.ibs.task2.service.AppointmentScheduler;

import java.time.LocalDateTime;
import java.util.List;

import static com.pafolder.ibs.task2.TestData.*;
import static com.pafolder.ibs.task2.model.TimeSlotOfSpecialist.timeSlotWithoutNameToString;

public class Application {
    public static final String WORKING_HOURS_STRING = "\nИнтервал работы специалиста:";
    public static final String FREE_SLOTS_FOR_PERIOD = "\nВозможные часы посещения в пределах заданного интервала времени:";
    public static final String PREDEFINED_TIME_INTERVAL_STRING = "\nЗаданный интервал времени: ";
    public static final String ERROR_WHEN_ADDING_SPECIALIST = "Ошибка при добавлении специалиста ";
    public static final String ERROR_WHEN_ADDING_TIME_SLOT = "Ошибка при инициализации времени работы специалиста ";
    public static final int DR_IVANOV_APPOINTMENT_DURATION = 45;

    public static void main(String[] args) {
        AppointmentScheduler scheduler = new AppointmentScheduler();
        initializeData(scheduler);

        List<TimeSlotOfSpecialist> initialTimeSlots =
                scheduler.getInitialTimeSlotsOfSpecialist(DR_IVANOV, testDate1);

        List<TimeSlotOfSpecialist> freeTimeSlots = scheduler.getAvailableTimeSlotsOfSpecialist(
                DR_IVANOV,
                LocalDateTime.of(testDate1, testTime1),
                LocalDateTime.of(testDate2, testTime2)
        );

        System.out.println(WORKING_HOURS_STRING);
        initialTimeSlots.forEach(s -> System.out.println(s.toString()));

        System.out.println(PREDEFINED_TIME_INTERVAL_STRING + timeSlotWithoutNameToString(
                new TimeSlotOfSpecialist(null, LocalDateTime.of(testDate1, testTime1),
                        LocalDateTime.of(testDate2, testTime2), true)
        ));

        System.out.println(FREE_SLOTS_FOR_PERIOD);
        freeTimeSlots.forEach(s -> System.out.println(s.toString()));
    }

    static void initializeData(AppointmentScheduler scheduler) {
        if (!scheduler.addSpecialist(new Specialist(DR_IVANOV,
                DR_IVANOV_APPOINTMENT_DURATION))) {
            throw new RuntimeException(ERROR_WHEN_ADDING_SPECIALIST + DR_IVANOV);
        }
        timeSlotsOfIvanov.forEach(s -> {
            if (!scheduler.addFreeTimeSlotOfSpecialist(DR_IVANOV,
                    s.startDateTime(), s.endDateTime())) {
                throw new RuntimeException(ERROR_WHEN_ADDING_TIME_SLOT + DR_IVANOV);
            }
        });
    }

}
