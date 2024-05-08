package roomescape.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public record ReservationRequest(
        @NotBlank
        String name,
        String date,
        @NotNull
        Long timeId,
        @NotNull
        Long themeId
) {
    public ReservationRequest {
        validateDate(date);
    }

    private void validateDate(String date) {
        if (date == null) {
            throw new IllegalArgumentException("날짜는 null 값 일 수 없습니다.");
        }
        try {
            LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("날짜는 yyyy-MM-dd 형식으로 입력해야 합니다. 입력한 값: " + date);
        }
    }
}
