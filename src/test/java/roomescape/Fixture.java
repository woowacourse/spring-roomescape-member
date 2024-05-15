package roomescape;

import java.time.LocalDate;

public class Fixture {

    public static final LocalDate TODAY = LocalDate.now();
    public static final LocalDate YESTERDAY = LocalDate.now().minusDays(1);
    public static final LocalDate DAY_BEFORE_YESTERDAY = LocalDate.now().minusDays(2);
    public static final LocalDate TOMORROW = LocalDate.now().plusDays(1L);


}
