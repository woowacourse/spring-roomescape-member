package roomescape.domain.admin.dto;

import roomescape.global.exception.RoomEscapeException;

import java.time.LocalDate;
import java.util.Objects;

public record AdminReservationSaveRequest(
        LocalDate date,
        Long timeId,
        Long themeId,
        Long memberId
) {
    public AdminReservationSaveRequest {
        try {
            Objects.requireNonNull(date, "[ERROR] 날짜는 null일 수 없습니다.");
            Objects.requireNonNull(timeId, "[ERROR] 시간은 null일 수 없습니다.");
            Objects.requireNonNull(themeId, "[ERROR] 테마는 null일 수 없습니다.");
            Objects.requireNonNull(memberId, "[ERROR] 멤버는 null일 수 없습니다.");
        } catch (NullPointerException e) {
            throw new RoomEscapeException(e.getMessage());
        }
    }
}
