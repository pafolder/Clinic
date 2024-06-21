package com.pafolder.ibs.task2;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static com.pafolder.ibs.task2.TestData.DR_IVANOV;
import static com.pafolder.ibs.task2.TestData.timeSlotsOfIvanov;
import static com.pafolder.ibs.task2.TimeSlot.timeSlotWithoutNameToString;

public class Application {
    private static final LocalDate testDate1 = LocalDate.of(2024, 8, 15);
    private static final LocalDate testDate2 = LocalDate.of(2024, 8, 16);
    private static final LocalTime testTime1 = LocalTime.of(7, 0);
    private static final LocalTime testTime2 = LocalTime.of(13, 46);
    public static final String WORKING_HOURS_STRING = "\nИнтервал работы специалиста:";
    public static final String FREE_SLOTS_FOR_PERIOD = "\nВозможные часы посещения в пределах заданного интервала времени:";
    public static final String PREDEFINED_TIME_INTERVAL_STRING = "\nЗаданный интервал времени: ";

    public static void main(String[] args) {
        AppointmentScheduler scheduler = new AppointmentScheduler();
        initializeData(scheduler);

        List<TimeSlot> initialTimeSlots =
                scheduler.getInitialTimeSlotsOfSpecialist(DR_IVANOV, testDate1);

        List<TimeSlot> freeTimeSlots = scheduler.getAvailableTimeSlotsOfSpecialist(
                DR_IVANOV,
                LocalDateTime.of(testDate1, testTime1),
                LocalDateTime.of(testDate2, testTime2)
        );

        System.out.println(WORKING_HOURS_STRING);
        initialTimeSlots.forEach(s -> System.out.println(s.toString()));

        System.out.println(PREDEFINED_TIME_INTERVAL_STRING + timeSlotWithoutNameToString(
                new TimeSlot(null, LocalDateTime.of(testDate1, testTime1),
                        LocalDateTime.of(testDate2, testTime2), true)
        ));

        System.out.println(FREE_SLOTS_FOR_PERIOD);
        freeTimeSlots.forEach(s -> System.out.println(s.toString()));
    }

    static void initializeData(AppointmentScheduler scheduler) {
        scheduler.add(new Specialist(DR_IVANOV, 45));
        timeSlotsOfIvanov.forEach(s -> {
            if (!scheduler.addFreeTimeSlotOfSpecialist(DR_IVANOV,
                    s.startDateTime(), s.endDateTime())) {
                throw new RuntimeException("Ошибка при инициализации данных");
            }
        });
    }

}
