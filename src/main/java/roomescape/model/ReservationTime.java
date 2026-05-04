package roomescape.model;

import java.time.LocalTime;

public class ReservationTime {

    private Long id;
    private LocalTime startTime;

    protected ReservationTime() {}

    public ReservationTime(LocalTime startTime) {
        this(null, startTime);
    }

    public ReservationTime(Long id, LocalTime startTime) {
        validateStartTime(startTime);
        this.id = id;
        this.startTime = startTime;
    }

    public Long getId() {
        return id;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    private void validateStartTime(LocalTime startTime) {
        if (startTime == null) {
            throw new IllegalArgumentException("예약 시작 시간은 필수입니다.");
        }
    }
}
