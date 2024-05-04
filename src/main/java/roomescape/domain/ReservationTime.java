package roomescape.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import roomescape.domain.builder.ReservationTimeBuilder;
import roomescape.domain.builder.ReservationTimeBuilderImpl;

import java.time.LocalTime;

public class ReservationTime {
    private Long id;

    @JsonFormat(pattern = "kk:mm")
    private LocalTime startAt;

    public ReservationTime(final Long id, final LocalTime startAt) {
        validateTime(startAt);
        this.id = id;
        this.startAt = startAt;
    }

    public static ReservationTimeBuilder builder() {
        return new ReservationTimeBuilderImpl();
    }

    private void validateTime(final LocalTime startAt) {
        if (startAt == null) {
            throw new IllegalArgumentException("[ERROR] 잘못된 예약 시작 시간을 입력하였습니다.");
        }
    }

    public boolean isBefore(final LocalTime now) {
        return startAt.isBefore(now);
    }

    public long getId() {
        return id;
    }

    public LocalTime getStartAt() {
        return startAt;
    }
}
