package roomescape.dto.request;

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
        validateLengthOfName(name);
    }

    private void validateNull(LocalDate date, String name, Long timeId, Long themeId) {
        if(date == null || name == null || timeId == null || themeId == null) {
            throw new InvalidInputException("선택되지 않은 값이 있거나 입력되지 않은 값이 있습니다.");
        }
    }

    private void validateLengthOfName(String name) {
        if (name.isBlank()) {
            throw new InvalidInputException("이름은 한 글자 이상 입력해야 합니다.");
        }
    }
}
