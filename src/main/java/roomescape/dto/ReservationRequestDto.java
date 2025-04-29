package roomescape.dto;

import java.time.LocalDate;
import roomescape.domain_entity.Reservation;
import roomescape.domain_entity.ReservationTime;

public record ReservationRequestDto(
        String name, LocalDate date, long timeId
) {
    public ReservationRequestDto {
        validateNotNull(name, date, timeId);
        validateMaxLength(name);
    }

    private void validateNotNull(String name, LocalDate date, long timeId) {
        if (name == null || date == null || timeId == 0) {
            throw new IllegalArgumentException("요청 필드가 올바르지 않습니다.");
        }
    }

    private void validateMaxLength(String name) {
        if (name.length() > 255) {
            throw new IllegalArgumentException("요청 필드가 최대 제한 길이를 초과했습니다.");
        }
    }

    public Reservation toReservationWith(ReservationTime reservationTime) {
        return new Reservation(
                name, date, reservationTime
        );
    }
}
