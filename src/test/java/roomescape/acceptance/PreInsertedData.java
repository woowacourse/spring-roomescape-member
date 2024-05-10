package roomescape.acceptance;

import roomescape.domain.*;

import java.time.LocalDate;
import java.time.LocalTime;

public class PreInsertedData { // todo: 전부 final 붙이고 상수 네이밍으로 바꿔주기

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

    public static Member preInsertedAdmin = new Member(
            1L,
            "어드민",
            "admin@email.com",
            "admin",
            Role.ADMIN
    );

    public static Member preInsertedCustomer = new Member(
            2L,
            "고객",
            "customer@email.com",
            "customer",
            Role.CUSTOMER
    );

    public static Reservation preInsertedReservation = new Reservation(
            1L,
            preInsertedCustomer,
            LocalDate.parse("2024-05-01"),
            preInsertedReservationTime2,
            preInsertedTheme2
    );
}
