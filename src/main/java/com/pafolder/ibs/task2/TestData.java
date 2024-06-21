package com.pafolder.ibs.task2;

import com.pafolder.ibs.task2.model.TimeSlotOfSpecialist;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

public class TestData {
    public static final String DR_IVANOV = "Иванов";
    public static final String DR_PETROV = "Петров";
    public static final String DR_SMITH = "Кузнецов";
    public static final LocalDate testDate1 = LocalDate.of(2024, 8, 15);
    public static final LocalDate testDate2 = LocalDate.of(2024, 8, 16);
    public static final LocalTime testTime1 = LocalTime.of(7, 0);
    public static final LocalTime testTime2 = LocalTime.of(13, 46);

    public static final List<TimeSlotOfSpecialist> timeSlotsOfIvanov = Arrays.asList(
            new TimeSlotOfSpecialist(null,
                    LocalDateTime.of(2024, 8, 15, 8, 0),
                    LocalDateTime.of(2024, 8, 15, 12, 0), true),
            new TimeSlotOfSpecialist(null,
                    LocalDateTime.of(2024, 8, 15, 12, 45),
                    LocalDateTime.of(2024, 8, 15, 17, 0), true),
            new TimeSlotOfSpecialist(null,
                    LocalDateTime.of(2024, 8, 16, 8, 0),
                    LocalDateTime.of(2024, 8, 16, 12, 0), true),
            new TimeSlotOfSpecialist(null,
                    LocalDateTime.of(2024, 8, 16, 12, 45),
                    LocalDateTime.of(2024, 8, 16, 16, 30), true));
}
