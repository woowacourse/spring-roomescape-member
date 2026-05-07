package roomescape.date.fixture;

import roomescape.date.domain.ReservationDate;

import java.time.LocalDate;

public class ReservationDateFixture {

    public static ReservationDate oneWeekLater() {
        return ReservationDate.create(LocalDate.now().plusWeeks(1));
    }

    public static ReservationDate twoWeeksLater() {
        return ReservationDate.create(LocalDate.now().plusWeeks(2));
    }

}
