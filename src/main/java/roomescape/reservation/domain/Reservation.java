package roomescape.reservation.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Reservation {
    private final Long id;
    private String name;
    private Long scheduleId;
}
