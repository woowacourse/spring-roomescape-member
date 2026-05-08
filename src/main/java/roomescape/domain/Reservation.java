package roomescape.domain;

import roomescape.command.ReservationSaveCommand;

import java.time.LocalDate;
import java.util.Objects;

public record Reservation(Long id, String name, LocalDate date, ReservationTime time, Theme theme) {
    public Reservation {
        validateName(name);
        validateDate(date);
        validateTime(time);
        validateTheme(theme);
    }

    public static Reservation forSave(ReservationSaveCommand command, ReservationTime reservationTime, Theme theme) {
        new Reservation(null, command.name(), command.date(), reservationTime, theme);
    }

    private void validateTheme(Theme theme) {
        if (Objects.isNull(theme)) {
            throw new IllegalArgumentException("유효하지 않는 테마입니다.");
        }
    }

    private void validateTime(ReservationTime time) {
        if (Objects.isNull(time)) {
            throw new IllegalArgumentException("유효하지 않은 시간입니다.");
        }
    }

    private void validateName(String name) {
        if (Objects.isNull(name)) {
            throw new IllegalArgumentException("유효하지 않은 이름입니다.");
        }
        if (name.isBlank()) {
            throw new IllegalArgumentException("이름은 공백일 수 없습니다.");
        }
    }

    private void validateDate(LocalDate date) {
        if (Objects.isNull(date)) {
            throw new IllegalArgumentException("유효하지 않은 날짜입니다");
        }
    }

    public long timeId() {
        return time.id();
    }

    public long themeId() {
        return theme.id();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Reservation that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
