package roomescape.domain.reservation.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

public record ReservationCreateRequestDto(String name,
                                          @JsonFormat(pattern = "yyyy-MM-dd") LocalDate date,
                                          Long timeId, Long themeId) {

    public ReservationCreateRequestDto {
        validateName(name);
    }

    private static void validateName(String name) {
        if (name.isBlank()) {
            throw new IllegalArgumentException("이름은 빈 문자열일 수 없습니다.");
        }
    }
}
