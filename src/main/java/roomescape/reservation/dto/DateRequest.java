package roomescape.reservation.dto;

import org.springframework.web.bind.MethodArgumentNotValidException;

public class DateRequest {
    private static final String regexp = "^(?:(?:19|20)\\d{2})-(?:0[1-9]|1[0-2])-(?:0[1-9]|[1-2][0-9]|3[0-1])$";
    String date;

    public DateRequest (String date) {
        validateDate(date);
        this.date = date;
    }

    private void validateDate(String date) {
        if (!date.matches(regexp) || date.isBlank()) {
            throw new IllegalArgumentException("날짜를 잘못 입력하였습니다: " + date);
        }
    }

    public String getDate() {
        return date;
    }
}
