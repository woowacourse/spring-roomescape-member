package roomescape.service.policy;

import java.time.LocalDate;

public class CurrentBaseDate implements BaseDate {
    @Override
    public LocalDate getStartDateBefore(long period) {
        return LocalDate.now().minusDays(period);
    }
}
