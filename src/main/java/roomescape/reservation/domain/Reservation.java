package roomescape.reservation.domain;

import java.time.LocalDate;
import java.util.Objects;
import roomescape.time.domain.Time;
import roomescape.theme.domain.Theme;

public record Reservation(Long id, String name, LocalDate date, Time time, Theme theme) {

    @Override
    public boolean equals(Object other) {
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        Reservation that = (Reservation) other;
        return Objects.equals(id, that.id) && Objects.equals(date, that.date)
                && Objects.equals(time, that.time) && Objects.equals(theme, that.theme);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, date, time, theme);
    }
}
