package com.pafolder.ibs.task2;

import com.pafolder.ibs.task2.model.Specialist;
import com.pafolder.ibs.task2.model.TimeSlot;
import com.pafolder.ibs.task2.service.TimeSlotScheduler;

import java.time.LocalDateTime;
import java.util.List;

import static com.pafolder.ibs.task2.TestData.*;

public class Application {
    public static final String WORKING_HOURS_STRING = "\nИнтервал работы специалиста";
    public static final String FREE_SLOTS_FOR_PERIOD = "\nВозможные часы посещения в пределах заданного интервала времени:";
    public static final String PREDEFINED_TIME_INTERVAL_STRING = "\nЗаданный интервал времени: ";
    public static final String ERROR_WHEN_ADDING_SPECIALIST = "Ошибка при добавлении специалиста ";
    public static final String ERROR_WHEN_ADDING_TIME_SLOT = "Ошибка при инициализации времени работы специалиста ";
    public static final String ERROR_SPECIALIST_NOT_FOUND = "\nСпециалист не найден";
    public static final String APPOINTMENT_STRING = "Приём ";
    public static final String MINUTES_STRING = " минут";
    public static final String TIME_2DIGIT_FORMAT = "%02d";

    public static void main(String[] args) {
        TimeSlotScheduler<Specialist> scheduler = new TimeSlotScheduler<>();
        initializeData(scheduler);
        try {
            List<TimeSlot> initialTimeSlots = scheduler.getInitialTimeSlotsOfSpecialist(
                    DR_IVANOV, testDate1);
            List<TimeSlot> freeTimeSlots = scheduler.getAvailableTimeSlotsOfSpecialist(
                    DR_IVANOV,
                    LocalDateTime.of(testDate1, testTime1),
                    LocalDateTime.of(testDate2, testTime2)
            );
            printInitialTimeSlots(initialTimeSlots, DR_IVANOV);
            printPredefinedPeriod();
            printFreeSlotsForPeriod(freeTimeSlots);
        } catch (IllegalArgumentException e) {
            System.out.println(ERROR_SPECIALIST_NOT_FOUND);
        }
    }

    static void initializeData(TimeSlotScheduler<Specialist> scheduler) {
        if (!scheduler.addSpecialist(new Specialist(DR_IVANOV))) {
            throw new RuntimeException(ERROR_WHEN_ADDING_SPECIALIST + DR_IVANOV);
        }
        timeSlotsOfIvanov.forEach(s -> {
            if (!scheduler.addFreeTimeSlotOfSpecialist(DR_IVANOV, s.appointmentDuration(),
                    s.startDateTime(), s.endDateTime())) {
                throw new RuntimeException(ERROR_WHEN_ADDING_TIME_SLOT + DR_IVANOV);
            }
        });
    }

    static void printInitialTimeSlots(List<TimeSlot> initialTimeSlots, String specialist) {
        System.out.printf("%s %s:%n", WORKING_HOURS_STRING, specialist);
        initialTimeSlots.forEach(s -> System.out.println(timeSlotWithoutNameToString(s)));
    }

    static void printPredefinedPeriod() {
        System.out.println(PREDEFINED_TIME_INTERVAL_STRING + timeSlotWithoutNameToString(
                new TimeSlot(null, DR_IVANOV_APPOINTMENT_DURATION,
                        LocalDateTime.of(testDate1, testTime1),
                        LocalDateTime.of(testDate2, testTime2), true)
        ));
    }

    static void printFreeSlotsForPeriod(List<TimeSlot> freeTimeSlots) {
        System.out.println(FREE_SLOTS_FOR_PERIOD);
        freeTimeSlots.forEach(s -> System.out.println(s.specialist().getName() + " " +
                timeSlotWithoutNameToString(s)));
    }

    public static String timeSlotWithoutNameToString(TimeSlot timeSlot) {
        return timeSlot.startDateTime().getDayOfMonth() + "." +
                timeSlot.startDateTime().getMonth().getValue() + "." +
                timeSlot.startDateTime().getYear() + " " +
                String.format(TIME_2DIGIT_FORMAT, timeSlot.startDateTime().getHour()) + ":" +
                String.format(TIME_2DIGIT_FORMAT, timeSlot.startDateTime().getMinute()) + " … " +
                String.format(TIME_2DIGIT_FORMAT, timeSlot.endDateTime().getHour()) + ":" +
                String.format(TIME_2DIGIT_FORMAT, timeSlot.endDateTime().getMinute()) + " " +
                APPOINTMENT_STRING + timeSlot.appointmentDuration() + MINUTES_STRING;
    }
}