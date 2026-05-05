package roomescape.time.domain;

import java.time.LocalTime;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode
public class ReservationTime {

    private Long id;
    private LocalTime startAt;

    public ReservationTime withId(Long id) {
        return ReservationTime.builder()
                .id(id)
                .startAt(this.startAt)
                .build();
    }
}
