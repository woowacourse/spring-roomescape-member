package roomescape.dto;

import java.time.LocalDate;

public record ReservationRequestDto(LocalDate date, Long timeId, Long themeId) {

    public ReservationRequestDto {
        if (date == null) {
            throw new IllegalArgumentException("날짜를 입력하여야 합니다.");
        }
    }
}
