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
        validateNonNull();
    }

    private void validateNonNull() {
        try {
            Objects.requireNonNull(name);
            Objects.requireNonNull(date);
            Objects.requireNonNull(timeId);
            Objects.requireNonNull(themeId);
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("null 값이 될 수 없습니다.");
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
