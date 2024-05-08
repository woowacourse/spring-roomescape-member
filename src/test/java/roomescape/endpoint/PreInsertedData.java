package roomescape.endpoint;

import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

import java.time.LocalDate;
import java.time.LocalTime;

public class PreInsertedData {

    public static ReservationTime preInsertedReservationTime1 = new ReservationTime(
            1L,
            LocalTime.parse("10:00:00")
    );

    public static ReservationTime preInsertedReservationTime2 = new ReservationTime(
            2L,
            LocalTime.parse("11:00:00")
    );

    public static Theme preInsertedTheme1 = new Theme(
            1L,
            "이름1",
            "설명1",
            "썸네일1"
    );

    public static Theme preInsertedTheme2 = new Theme(
            2L,
            "이름2",
            "설명2",
            "썸네일2"
    );

    public static Reservation preInsertedReservation = new Reservation(
            1L,
            "칸쵸와 알파고",
            LocalDate.parse("2024-05-01"),
            preInsertedReservationTime1,
            preInsertedTheme2
    );
}
