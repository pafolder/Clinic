package com.pafolder.ibs.task2;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class TestData {
    public static final String DR_IVANOV = "Иванов";
    public static final String DR_PETROV = "Петров";
    public static final String DR_SMITH = "Кузнецов";
    public static final List<TimeSlot> timeSlotsOfIvanov = Arrays.asList(
            new TimeSlot(null,
                    LocalDateTime.of(2024, 8, 15, 8, 0),
                    LocalDateTime.of(2024, 8, 15, 12, 0), true),
            new TimeSlot(null,
                    LocalDateTime.of(2024, 8, 15, 12, 45),
                    LocalDateTime.of(2024, 8, 15, 16, 59), true),
            new TimeSlot(null,
                    LocalDateTime.of(2024, 8, 16, 8, 0),
                    LocalDateTime.of(2024, 8, 16, 12, 0), true),
            new TimeSlot(null,
                    LocalDateTime.of(2024, 8, 16, 12, 45),
                    LocalDateTime.of(2024, 8, 16, 16, 29), true));
}
