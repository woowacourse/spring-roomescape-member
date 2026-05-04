package roomescape.reservation.domain;

import java.time.LocalDate;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.theme.domain.Theme;

@Builder
@Getter
@EqualsAndHashCode(of = {"name", "date", "theme", "time"})
public class Reservation {

    private final Long id;
    private final String name;
    private final LocalDate date;
    private final Theme theme;
    private final ReservationTime time;

    public Long getThemeId() {
        return theme.getId();
    }

    public Long getTimeId() {
        return time.getId();
    }

    public Reservation withId(Long generatedId) {
        return Reservation.builder()
                .id(generatedId)
                .name(this.name)
                .date(this.date)
                .theme(theme)
                .time(this.time)
                .build();
    }
}
