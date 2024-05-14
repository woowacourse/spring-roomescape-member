package roomescape.dto.response;

import java.time.format.DateTimeFormatter;
import roomescape.domain.ReservationTime;

public class ReservationTimeResponseDto {
    public static final String TIME_PATTERN = "HH:mm";

    private Long id;
    private String startAt;

    public ReservationTimeResponseDto() {
    }

    public ReservationTimeResponseDto(final ReservationTime reservationTime) {
        this(reservationTime.getId(), reservationTime);
    }

    public ReservationTimeResponseDto(final Long id, final ReservationTime time) {
        this.id = id;
        this.startAt = time.getStartAt().format(DateTimeFormatter.ofPattern(TIME_PATTERN));
    }

    public Long getId() {
        return id;
    }

    public String getStartAt() {
        return startAt;
    }
}
