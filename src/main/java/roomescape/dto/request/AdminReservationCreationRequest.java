package roomescape.dto.request;

import java.time.LocalDate;

public record AdminReservationCreationRequest(LocalDate date, Long timeId, Long themeId, Long memberId) {

    public AdminReservationCreationRequest {
        validate(date, timeId, themeId, memberId);
    }

    private void validate(LocalDate date, Long timeId, Long themeId, Long memberId) {
        validateDate(date);
        validateTime(timeId);
        validateTheme(themeId);
        validateMember(memberId);
    }

    private void validateDate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("[ERROR] 날짜는 빈 값을 허용하지 않습니다.");
        }
    }

    private void validateTime(Long timeId) {
        if (timeId == null) {
            throw new IllegalArgumentException("[ERROR] 시간은 빈 값을 허용하지 않습니다.");
        }
    }

    private void validateTheme(Long themeId) {
        if (themeId == null) {
            throw new IllegalArgumentException("[ERROR] 테마는 빈 값을 허용하지 않습니다.");
        }
    }

    private void validateMember(Long memberId) {
        if (memberId == null) {
            throw new IllegalArgumentException("[ERROR] 회원은 빈 값이나 공백값을 허용하지 않습니다.");
        }
    }
}
