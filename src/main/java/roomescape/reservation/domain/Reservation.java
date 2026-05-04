package roomescape.reservation.domain;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;

@Getter
@Builder
@EqualsAndHashCode
@AllArgsConstructor
public class Reservation {

    private Long id;
    private String name;
    private LocalDate date;
    private ReservationTime time;
    private Theme theme;

    public Reservation withId(Long id) {
        return Reservation.builder()
                .id(id)
                .name(this.name)
                .date(this.date)
                .time(this.time)
                .theme(this.theme)
                .build();
    }
}
