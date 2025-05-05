package roomescape.reservation.domain;

import java.time.LocalDate;
import roomescape.time.domain.Time;
import roomescape.theme.domain.Theme;

public record Reservation(Long id, String name, LocalDate date, Time time, Theme theme) {

    private static Long NOT_SAVED_ID = 0L;

    public static Reservation createBeforeSaved(String name, LocalDate date, Time time, Theme theme) {
        return new Reservation(NOT_SAVED_ID, name, date, time, theme);
    }

    public Reservation {
        if (id == null) {
            throw new IllegalArgumentException("[ERROR] id가 null이 되어서는 안 됩니다.");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("[ERROR] 이름은 null이 되어서는 안 됩니다.");
        }
        if (date == null) {
            throw new IllegalArgumentException("[ERROR] 날짜가 null이 되어서는 안 됩니다.");
        }
        if (time == null) {
            throw new IllegalArgumentException("[ERROR] 시간이 null이 되어서는 안 됩니다.");
        }
        if (theme == null) {
            throw new IllegalArgumentException("[ERROR] 테마가 null이 되어서는 안 됩니다.");
        }
    }
}
