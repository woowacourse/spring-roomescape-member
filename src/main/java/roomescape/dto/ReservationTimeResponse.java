package roomescape.dto;

import java.time.LocalTime;
import java.util.List;
import roomescape.entity.ReservationTimeEntity;

public class ReservationTimeResponse {
    private final Long id;
    private final LocalTime startAt;

    private ReservationTimeResponse(Long id, LocalTime startAt) {
        this.id = id;
        this.startAt = startAt;
    }

    public static ReservationTimeResponse from(ReservationTimeEntity time) {
        return new ReservationTimeResponse(time.id(), time.startAt());
    }

    public static List<ReservationTimeResponse> from(List<ReservationTimeEntity> times) {
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
