package roomescape.global.time;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public interface TimeProvider {

    LocalDateTime nowDateTime();

    LocalDate nowDate();

    LocalTime nowTime();
}
