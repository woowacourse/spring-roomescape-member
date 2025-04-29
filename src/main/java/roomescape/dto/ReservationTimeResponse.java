package roomescape.dto;

import java.time.LocalTime;
import java.util.List;
import roomescape.domain.ReservationTime;

public class ReservationTimeResponse {
    private final Long id;
    private final LocalTime startAt;

    private ReservationTimeResponse(Long id, LocalTime startAt) {
        this.id = id;
        this.startAt = startAt;
    }

    public static ReservationTimeResponse from(ReservationTime time) {
        return new ReservationTimeResponse(time.getId(), time.getStartAt());
    }

    public static List<ReservationTimeResponse> from(List<ReservationTime> times) {
        return times.stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }

    public Long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }
}
