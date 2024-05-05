package roomescape.service.policy;

import java.time.LocalDate;

public interface BaseDate {
    LocalDate getStartDateBefore(long period);
}
