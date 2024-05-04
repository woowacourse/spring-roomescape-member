package roomescape.fixture;

import java.time.LocalDate;
import java.time.LocalTime;

public class DateTimeFixture {

    public static final LocalDate DAY_AFTER_TOMORROW = LocalDate.now().plusDays(2);
    public static final LocalTime TIME_11_00 = LocalTime.of(11, 0);
}
