package roomescape.dto;

import java.time.LocalDate;

public record ReservationRequest(String name, LocalDate date, Long timeId, Long themeId) {
    public ReservationRequest {
        validateName(name);
    }

    private static void validateName(String name) {
        if (name.length() < 2 || name.length() > 20) {
            throw new IllegalArgumentException("[ERROR] 사용자 이름은 2자 이상 20자 이하입니다.");
        }
    }
}
