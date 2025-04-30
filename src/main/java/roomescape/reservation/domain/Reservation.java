package roomescape.reservation.domain;

import java.time.LocalDate;
import roomescape.time.domain.Time;
import roomescape.theme.domain.Theme;

public record Reservation(Long id, String name, LocalDate date, Time time, Theme theme) {
}
