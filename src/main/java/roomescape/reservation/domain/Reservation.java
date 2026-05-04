package roomescape.reservation.domain;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import roomescape.time.domain.ReservationTime;

@Getter
@Builder
@EqualsAndHashCode
@AllArgsConstructor
public class Reservation {

    @Setter
    private Long id;
    private String name;
    private LocalDate date;
    private ReservationTime time;
}
