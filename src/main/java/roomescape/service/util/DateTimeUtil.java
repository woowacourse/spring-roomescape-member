package roomescape.service.util;

import java.time.LocalDate;
import java.time.LocalTime;

public class DateTimeUtil {

    public static LocalDate getNowDate() {
        return LocalDate.now();
    }

    public static LocalTime getNowTime() {
        return LocalTime.now();
    }
}
