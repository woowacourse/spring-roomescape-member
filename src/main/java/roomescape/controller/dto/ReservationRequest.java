package roomescape.controller.dto;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import roomescape.global.exception.InvalidReservationException;

public record ReservationRequest(
        String name,
        String date,
        Long timeId,
        Long themeId
) {

    public CreateReservationCommand toCommand() {
        validateName();
        validateTimeId();
        validateThemeId();

        return new CreateReservationCommand(
                name.trim(),
                parseDate(),
                timeId,
                themeId
        );
    }

    private void validateName() {
        if (name == null || name.isBlank()) {
            throw new InvalidReservationException("예약자 이름은 필수입니다.");
        }
    }

    private void validateTimeId() {
        if (timeId == null) {
            throw new InvalidReservationException("예약 시간은 필수입니다.");
        }
    }

    private void validateThemeId() {
        if (themeId == null) {
            throw new InvalidReservationException("테마는 필수입니다.");
        }
    }

    private LocalDate parseDate() {
        if (date == null || date.isBlank()) {
            throw new InvalidReservationException("예약 날짜는 필수입니다.");
        }

        try {
            return LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            throw new InvalidReservationException("예약 날짜 형식은 yyyy-MM-dd 이어야 합니다.");
        }
    }
}
