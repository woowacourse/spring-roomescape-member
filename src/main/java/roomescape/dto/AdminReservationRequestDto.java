package roomescape.dto;

import java.time.LocalDate;

public record AdminReservationRequestDto(LocalDate date, Long timeId, Long themeId, Long memberId) {

    public AdminReservationRequestDto {
        if (date == null) {
            throw new IllegalArgumentException("날짜를 입력하여야 합니다.");
        }
    }
}
