package roomescape.time.fixture;

import roomescape.time.domain.ReservationTime;
import roomescape.time.dto.request.ReservationTimeSaveDto;

import java.time.LocalTime;

public class ReservationTimeFixture {

    public static ReservationTime time15() {
        return ReservationTime.create(LocalTime.of(15, 0));
    }

    public static ReservationTime time16() {
        return ReservationTime.create(LocalTime.of(16, 0));
    }

    public static ReservationTime time17() {
        return ReservationTime.create(LocalTime.of(17, 0));
    }

    public static ReservationTime time(LocalTime time) {
        return ReservationTime.create(time);
    }

    public static ReservationTimeSaveDto saveDto(LocalTime startAt) {
        return new ReservationTimeSaveDto(startAt);
    }

}
