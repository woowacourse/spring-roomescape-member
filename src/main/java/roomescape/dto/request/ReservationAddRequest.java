package roomescape.dto.request;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public record ReservationAddRequest(LocalDate date, Long timeId, Long themeId) {

    public ReservationAddRequest {
        validate(date, timeId, themeId);
    }

    private void validate(LocalDate date, Long timeId, Long themeId) {
        if (date == null || timeId == null || themeId == null) {
            throw new IllegalArgumentException("잘못된 요청입니다. 모든 데이터를 입력해주세요");
        }
    }

    public String getStringDate() {
        return date().format(DateTimeFormatter.ISO_DATE);
    }
}
