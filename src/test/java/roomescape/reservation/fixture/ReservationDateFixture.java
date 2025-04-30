package roomescape.reservation.fixture;

import java.time.LocalDate;
import roomescape.reservation.domain.ReservationDate;

public class ReservationDateFixture {

    public static ReservationDate 예약날짜_25_4_22 = new ReservationDate(LocalDate.of(2025, 4, 22));

    public static ReservationDate 예약날짜_오늘 = new ReservationDate(LocalDate.of(2025, 4, 20));
    public static ReservationDate 예약날짜_어제 = new ReservationDate(LocalDate.of(2025, 4, 19));
    public static ReservationDate 예약날짜_3일전 = new ReservationDate(LocalDate.of(2025, 4, 17));
    public static ReservationDate 예약날짜_7일전 = new ReservationDate(LocalDate.of(2025, 4, 13));
    public static ReservationDate 예약날짜_8일전 = new ReservationDate(LocalDate.of(2025, 4, 12));
}
