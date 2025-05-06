package roomescape.reservation.dto;

import java.time.LocalDate;

public record ReservationRequest(String name, LocalDate date, Long timeId, Long themeId) {
    public ReservationRequest {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("이름은 비어있을 수 없습니다.");
        }

        if (date == null) {
            throw new IllegalArgumentException("날짜는 null 일 수 없습니다.");
        }

        if (timeId == null) {
            throw new IllegalArgumentException("예약 시간 번호는 null 일 수 없습니다.");
        }

        if (themeId == null) {
            throw new IllegalArgumentException("테마 번호는 null 일 수 없습니다.");
        }
    }
}
