package roomescape.dto.request;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public record ReservationCreateRequest(
        String name,
        String date,
        Long timeId) {

    public ReservationCreateRequest {
        validateBlank(name, date, timeId);
        validateDateFormat(date);
    }

    public LocalDate getLocalDate() {
        return LocalDate.parse(date);
    }

    private void validateBlank(final String name, final String date, final Long timeId) {
        if (name == null || name.isBlank() || date == null || date.isBlank() || timeId == null) {
            throw new IllegalArgumentException("빈 값으로 예약할 수 없습니다.");
        }
    }

    private void validateDateFormat(String date) {
        try {
            LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("날짜 형식이 올바르지 않습니다. YYYY-MM-DD 형식으로 입력해주세요.");
        }
    }


}
