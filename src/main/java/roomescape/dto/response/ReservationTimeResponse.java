package roomescape.dto.response;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import roomescape.domain.ReservationTime;

public record ReservationTimeResponse(Long id, String startAt) {

    public ReservationTimeResponse {
        validate(id, startAt);
    }

    private void validate(Long id, String startAt) {
        if (id == null || startAt == null) {
            throw new IllegalArgumentException("잘못된 응답입니다. id =" + id + ", startAt =" + startAt);
        }
    }

    public static ReservationTimeResponse from(ReservationTime reservationTime) {
        return new ReservationTimeResponse(
                reservationTime.getId(),
                reservationTime.getStartAt(DateTimeFormatter.ofPattern("HH:mm"))
        );
    }

    public ReservationTime toReservationTime() {
        return new ReservationTime(id, LocalTime.parse(startAt));
    }
}
