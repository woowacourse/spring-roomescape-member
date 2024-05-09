package roomescape;

import roomescape.domain.reservation.domain.ReservationTime;
import roomescape.domain.theme.domain.Theme;

import java.time.LocalTime;

public class ReservationFixture {

    public static final ReservationTime RESERVATION_TIME_NOW = new ReservationTime(1L, LocalTime.now());
    public static final Theme THEME = new Theme(1L, "테니", "설명", "썸네일");
}
