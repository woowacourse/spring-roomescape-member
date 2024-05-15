package roomescape.controller.reservation;

import roomescape.domain.Reservation;

public record CreateReservationRequest(String date, Long timeId, Long themeId, Long memberId) {

    public static CreateReservationRequest from(String date, Long timeId, Long themeId) {
        return new CreateReservationRequest(date, timeId, themeId, null);
    }

    public Reservation toDomain() {
        return new Reservation(null, date, timeId, themeId, memberId);
    }

    public CreateReservationRequest assignMemberId(Long memberId) {
        return new CreateReservationRequest(date, timeId, themeId, memberId);
    }
}
