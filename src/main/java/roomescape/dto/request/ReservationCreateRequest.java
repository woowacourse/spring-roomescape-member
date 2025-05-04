package roomescape.dto.request;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;
import roomescape.common.exception.InvalidRequestException;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

public record ReservationCreateRequest(
        String name,
        @JsonFormat(pattern = "yyyy-MM-dd") LocalDate date,
        Long timeId,
        Long themeId
) {
    public ReservationCreateRequest {
        validate(name, date, timeId, themeId);
    }

    public Reservation toReservation(ReservationTime time, Theme theme) {
        return new Reservation(null, name, date, time, theme);
    }

    private void validate(String name, LocalDate date, Long timeId, Long themeId) {
        if (name == null || name.isBlank()) {
            throw new InvalidRequestException("잘못된 요청입니다.");
        }
        if (date == null) {
            throw new InvalidRequestException("잘못된 요청입니다.");
        }
        if (timeId == null) {
            throw new InvalidRequestException("잘못된 요청입니다.");
        }
        if (themeId == null) {
            throw new InvalidRequestException("잘못된 요청입니다.");
        }
    }
}
