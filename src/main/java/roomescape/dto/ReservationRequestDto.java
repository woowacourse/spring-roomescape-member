package roomescape.dto;

import java.time.LocalDate;
import roomescape.domain_entity.Reservation;
import roomescape.domain_entity.ReservationTime;
import roomescape.domain_entity.Theme;

public record ReservationRequestDto(
        String name, LocalDate date, long timeId
) {
    public ReservationRequestDto {
        validateNotNull(name, date, timeId);
    }

    private void validateNotNull(String name, LocalDate date, long timeId) {
        if (name == null) {
            throw new IllegalArgumentException("잘못된 name 입력입니다.");
        }
        if (date == null) {
            throw new IllegalArgumentException("잘못된 date 입력입니다.");
        }
        if (timeId < 1) {
            throw new IllegalArgumentException("잘못된 timeId 입력입니다.");
        }
    }

    public Reservation toReservationWith(ReservationTime reservationTime, Theme theme) {
        return new Reservation(
                name, date, reservationTime, theme
        );
    }
}
