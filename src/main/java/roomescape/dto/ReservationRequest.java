package roomescape.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import roomescape.exception.InvalidInputException;

public record ReservationRequest(
    @JsonFormat(pattern = "yyyy-MM-dd") LocalDate date,
    String name,
    Long timeId,
    Long themeId) {

    public ReservationRequest {
        validateNull(date, name, timeId, themeId);
        validateName(name);
    }

    private void validateNull(LocalDate date, String name, Long timeId, Long themeId) {
        if(date == null || name == null || timeId == null || themeId == null) {
            throw new InvalidInputException("값을 모두 선택해라.");
        }
    }

    private void validateName(String name) {
        if (name.isBlank()) {
            throw new InvalidInputException("이름은 한 글자 이상이어야 한다.");
        }
    }
}
