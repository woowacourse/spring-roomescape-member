package roomescape.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.util.Objects;
import roomescape.domain.ReservationTime;
import roomescape.domain.member.Member;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.theme.Theme;

public record ReservationRequest(
        @JsonFormat(pattern = "yyyy-MM-dd") LocalDate date,
        Long timeId,
        Long themeId
) {

    public ReservationRequest {
        validateDateNonNull(date);
        validateTimeIdNonNull(timeId);
        validateThemeIdNonNull(themeId);
    }

    private void validateThemeIdNonNull(Long themeId) {
        if (Objects.isNull(themeId)) {
            throw new IllegalArgumentException("테마 번호는 null 값이 될 수 없습니다.");
        }
    }

    private void validateTimeIdNonNull(Long timeId) {
        if (Objects.isNull(timeId)) {
            throw new IllegalArgumentException("시간 번호는 null 값이 될 수 없습니다.");
        }
    }

    private void validateDateNonNull(LocalDate date) {
        if (Objects.isNull(date)) {
            throw new IllegalArgumentException("날짜는 null 값이 될 수 없습니다.");
        }
    }

    public Reservation toEntity(Member member, ReservationTime reservationTime, Theme theme) {
        return new Reservation(
                member,
                date,
                reservationTime,
                theme
        );
    }
}
