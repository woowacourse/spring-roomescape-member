package roomescape.dto;

import java.time.LocalDate;

public record ReservationRequest(String name, LocalDate date, Long timeId, Long themeId) {
    private static final int MIN_NAME_LENGTH = 2;
    private static final int MAX_NAME_LENGTH = 20;

    public ReservationRequest {
        validateName(name);
    }

    private static void validateName(String name) {
        if (name.length() < MIN_NAME_LENGTH || name.length() > MAX_NAME_LENGTH) {
            throw new IllegalArgumentException("[ERROR] 사용자 이름은 "+MIN_NAME_LENGTH+ "이상 "+MAX_NAME_LENGTH+"자 이하입니다.");
        }
    }
}
