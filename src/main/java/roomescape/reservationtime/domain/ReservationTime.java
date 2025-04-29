package roomescape.reservationtime.domain;

import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@Getter
@AllArgsConstructor
public class ReservationTime {
    @NonNull
    private Long id;

    @NonNull
    private LocalTime startAt;
}
