package roomescape.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.util.Objects;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.theme.Theme;

public record ReservationRequest(
        String name,
        @JsonFormat(pattern = "yyyy-MM-dd") LocalDate date,
        Long timeId,
        Long themeId
) {

    public ReservationRequest {
        validateNameNonNull(name);
        validateDateNonNull(date);
        validateTimeIdNonNull(timeId);
        validateThemeIdNonNull(themeId);
    }

    private void validateThemeIdNonNull(final Long themeId) {
        if (Objects.isNull(themeId)) {
            throw new IllegalArgumentException("테마 번호는 null 값이 될 수 없습니다.");
        }
    }

    private void validateTimeIdNonNull(final Long timeId) {
        if (Objects.isNull(timeId)) {
            throw new IllegalArgumentException("시간 번호는 null 값이 될 수 없습니다.");
        }
    }

    private void validateDateNonNull(final LocalDate date) {
        if (Objects.isNull(date)) {
            throw new IllegalArgumentException("날짜는 null 값이 될 수 없습니다.");
        }
    }

    private void validateNameNonNull(final String name) {
        if (Objects.isNull(name)) {
            throw new IllegalArgumentException("이름은 null 값이 될 수 없습니다.");
        }
    }

    public Reservation toEntity(ReservationTime reservationTime, Theme theme) {
        return new Reservation(
                name,
                date,
                reservationTime,
                theme
        );
    }
}
