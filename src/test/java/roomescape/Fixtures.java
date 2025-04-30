package roomescape;

import java.time.LocalDate;

public class Fixtures {

    public static LocalDate ofYesterday() {
        return LocalDate.now().minusDays(1);
    }

    public static LocalDate ofToday() {
        return LocalDate.now();
    }

    public static LocalDate ofTomorrow() {
        return LocalDate.now().plusDays(1);
    }
}
