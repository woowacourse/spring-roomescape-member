package roomescape.controller.request;

import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Objects;

public record ReservationRequest(
        String name,
        String date,
        Long timeId,
        Long themeId
) {
    public ReservationRequest {
        Objects.requireNonNull(name);
        Objects.requireNonNull(date);
        Objects.requireNonNull(timeId);
        Objects.requireNonNull(themeId);
        validateDate(date);
    }

    private void validateDate(String date) {
        try {
            LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("날짜는 yyyy-MM-dd 형식으로 입력해야 합니다. 입력한 값: " + date);
        }
    }

    public Reservation toEntity(ReservationTime reservationTime, Theme theme) {
        return new Reservation(name, LocalDate.parse(date), reservationTime, theme);
    }
}
