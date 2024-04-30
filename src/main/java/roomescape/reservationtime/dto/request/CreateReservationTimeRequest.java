package roomescape.reservationtime.dto.request;

import java.time.LocalTime;
import roomescape.reservationtime.model.ReservationTime;

// TODO: 날짜 범위나, 형식에 맞지 않을 경우 HttpMessageNotReadableException 발생, 자동 400에러 응답
public record CreateReservationTimeRequest(LocalTime startAt) {
    public CreateReservationTimeRequest(final LocalTime startAt) {
        if (startAt == null) {
            throw new IllegalArgumentException("올바른 시간을 입력해주세요.");
        }
        this.startAt = startAt;
    }

    public ReservationTime toReservationTime() {
        return new ReservationTime(
                null,
                startAt);
    }
}
