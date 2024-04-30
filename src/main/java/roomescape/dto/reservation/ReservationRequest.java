package roomescape.dto.reservation;

import java.time.LocalDate;
import java.util.Objects;

import roomescape.application.dto.ReservationCreationRequest;

public record ReservationRequest(String name, LocalDate date, Long timeId) {
    public ReservationRequest {
        Objects.requireNonNull(name, "예약자명은 필수입니다.");
        Objects.requireNonNull(date, "예약날짜는 필수입니다.");
        Objects.requireNonNull(timeId, "예약시간은 필수입니다.");
    }

    public ReservationCreationRequest toReservationCreationRequest() {
        return new ReservationCreationRequest(name, date, timeId);
    }
}
