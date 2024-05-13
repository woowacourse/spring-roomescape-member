package roomescape.dto.response;

import roomescape.domain.ReservationTime;

import java.time.format.DateTimeFormatter;

public record AvailableReservationTimeResponse(Long id, String startAt, boolean alreadyBooked) {

    public AvailableReservationTimeResponse {
        validate(id, startAt);
    }

    private void validate(Long id, String startAt) {
        if (id == null || startAt == null) {
            throw new IllegalArgumentException("잘못된 응답입니다. id = " + id + ", startAt = " + startAt);
        }
    }

    public AvailableReservationTimeResponse(ReservationTime reservationTime, boolean alreadyBooked) {
        this(reservationTime.getId(), reservationTime.getStartAt(DateTimeFormatter.ofPattern("HH:mm")), alreadyBooked);
    }
}
