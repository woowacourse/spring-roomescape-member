package roomescape.service.dto.reservation;

import java.time.LocalDate;
import roomescape.global.exception.InvalidReservationException;

public record CreateReservationCommand(
        String name,
        LocalDate date,
        Long timeId,
        Long themeId
) {

    private static final int MAX_NAME_LENGTH = 50;

    public CreateReservationCommand {
        validateName(name);
        validateDate(date);
        validateTimeId(timeId);
        validateThemeId(themeId);
        name = name.trim();
    }

    private static void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new InvalidReservationException("예약자 이름은 필수입니다.");
        }
        if (name.length() > MAX_NAME_LENGTH) {
            throw new InvalidReservationException("예약자 이름은 " + MAX_NAME_LENGTH + "자를 넘을 수 없습니다.");
        }
    }

    private static void validateDate(LocalDate date) {
        if (date == null) {
            throw new InvalidReservationException("예약 날짜는 필수입니다.");
        }
    }

    private static void validateTimeId(Long timeId) {
        if (timeId == null) {
            throw new InvalidReservationException("예약 시간은 필수입니다.");
        }
    }

    private static void validateThemeId(Long themeId) {
        if (themeId == null) {
            throw new InvalidReservationException("테마는 필수입니다.");
        }
    }
}
