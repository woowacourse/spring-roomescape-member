package roomescape.domain;

import java.time.LocalDate;
import java.util.Optional;

public record Reservation(Long id, String name, LocalDate date, TimeSlot timeSlot, Theme theme) {

    public Reservation {
        validateName(name);
        validateDate(date);
        validateTimeSlot(timeSlot);
        validateTheme(theme);
    }

    public static Reservation transientOf(String name, LocalDate date, TimeSlot timeSlot, Theme theme) {
        return new Reservation(
                null,
                name,
                date,
                timeSlot,
                theme
        );
    }

    public Reservation patch(String name, LocalDate date, TimeSlot timeSlot, Theme theme) {
        return new Reservation(
                this.id,
                Optional.ofNullable(name).orElse(this.name),
                Optional.ofNullable(date).orElse(this.date),
                Optional.ofNullable(timeSlot).orElse(this.timeSlot),
                Optional.ofNullable(theme).orElse(this.theme)
        );
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("예약자 이름은 필수이며 비어있을 수 없습니다.");
        }
    }

    private void validateDate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("예약 날짜는 필수입니다.");
        }
    }

    private void validateTimeSlot(TimeSlot timeSlot) {
        if (timeSlot == null) {
            throw new IllegalArgumentException("존재하지 않는 예약 시간대입니다.");
        }
    }

    private void validateTheme(Theme theme) {
        if (theme == null) {
            throw new IllegalArgumentException("존재하지 않는 테마입니다.");
        }
    }
}
