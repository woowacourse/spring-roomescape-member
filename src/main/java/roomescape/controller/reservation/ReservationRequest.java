package roomescape.controller.reservation;

import roomescape.domain.Reservation;

public record ReservationRequest(String date, Long timeId, Long themeId, Long memberId) {

    public ReservationRequest(String date, Long timeId, Long themeId) {
        this(date, timeId, themeId, null);
    }

    public Reservation toDomain() {
        return new Reservation(null, date, timeId, themeId, memberId);
    }

    public ReservationRequest assignMemberId(Long memberId) {
        return new ReservationRequest(date, timeId, themeId, memberId);
    }
}
