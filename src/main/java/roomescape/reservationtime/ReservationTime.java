package roomescape.reservationtime;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalTime;

@Getter
@RequiredArgsConstructor
public class ReservationTime {
    private final Long id;
    private final LocalTime startAt;
}
