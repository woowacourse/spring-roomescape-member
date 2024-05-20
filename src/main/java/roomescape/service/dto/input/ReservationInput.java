package roomescape.service.dto.input;

import roomescape.domain.ReservationDate;

public record ReservationInput(ReservationDate date, Long timeId, Long themeId, Long memberId) {

    public static ReservationInput of(final String date,
                                      final Long timeId,
                                      final Long themeId,
                                      final Long memberId) {
        return new ReservationInput(ReservationDate.from(date), timeId, themeId, memberId);
    }
}
