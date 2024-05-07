package roomescape.dto.reservation;

import java.time.LocalDate;

public record ReservationRequest(String name, LocalDate date, Long timeId, Long themeId) {

    public ReservationRequest {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("이름이 입력되지 않았습니다.");
        }
        if (date == null) {
            throw new IllegalArgumentException("날짜가 입력되지 않았습니다.");
        }
    }
}
