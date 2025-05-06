package roomescape.dto.request;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import roomescape.common.exception.InvalidRequestException;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

public record ReservationCreateRequest(
        @NotBlank(message = "이름은 빈 값이 올 수 없습니다")
        String name,
        @NotNull(message = "예약 날짜는 빈 값이 올 수 없습니다")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate date,
        @NotNull(message = "예약 시간이 올바르지 않습니다")
        Long timeId,
        @NotNull(message = "예약 테마가 올바르지 않습니다")
        Long themeId
) {
    public ReservationCreateRequest {
//        validate(name, date, timeId, themeId);
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
