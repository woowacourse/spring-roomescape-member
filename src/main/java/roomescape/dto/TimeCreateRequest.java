package roomescape.dto;

import roomescape.domain.ReservationTime;

public record TimeCreateRequest(String startAt) {
    public ReservationTime createReservationTime() {
        return new ReservationTime(startAt);
    }
}
