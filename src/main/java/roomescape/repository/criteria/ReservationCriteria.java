package roomescape.repository.criteria;

import java.time.LocalDate;
import roomescape.service.dto.request.ReservationCriteriaCreation;

public record ReservationCriteria(Long memberId, Long themeId, LocalDate from, LocalDate to) {
    public static ReservationCriteria from(final ReservationCriteriaCreation creation) {
        return new ReservationCriteria(creation.memberId(), creation.themeId(), creation.from(), creation.to());
    }
}
