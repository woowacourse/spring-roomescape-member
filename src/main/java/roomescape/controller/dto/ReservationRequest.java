package roomescape.controller.dto;

import java.time.LocalDate;

public record ReservationRequest(String name, LocalDate date, Long timeId, Long themeId) {

    public ReservationRequest {
        validate(name, date, timeId, themeId);
    }

    private void validate(String name, LocalDate date, Long timeId, Long themeId) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("예약자 이름은 필수이며 비어있을 수 없습니다.");
        }
        if (date == null) {
            throw new IllegalArgumentException("예약 날짜는 필수입니다.");
        }
        if (date.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("과거 날짜는 예약이 불가능합니다.");
        }
        if (timeId == null) {
            throw new IllegalArgumentException("예약 시간은 필수입니다.");
        }
        if (themeId == null) {
            throw new IllegalArgumentException("예약 테마는 필수입니다.");
        }
    }
}
