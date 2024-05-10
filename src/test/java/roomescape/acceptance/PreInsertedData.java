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

    public static ReservationTime preInsertedReservationTime3 = new ReservationTime(
            3L,
            LocalTime.parse("12:00:00")
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

    public static Theme preInsertedTheme3 = new Theme(
            3L,
            "이름3",
            "설명3",
            "썸네일3"
    );

    public static Member preInsertedAdmin = new Member(
            1L,
            "어드민",
            "admin@email.com",
            "admin",
            Role.ADMIN
    );

    public static Member preInsertedCustomer1 = new Member(
            2L,
            "고객1",
            "customer1@email.com",
            "customer1",
            Role.CUSTOMER
    );

    public static Member preInsertedCustomer2 = new Member(
            3L,
            "고객2",
            "customer2@email.com",
            "customer2",
            Role.CUSTOMER
    );


    public static Reservation preInsertedReservation1 = new Reservation(
            1L,
            preInsertedCustomer1,
            LocalDate.parse("2024-05-01"),
            preInsertedReservationTime2,
            preInsertedTheme2
    );

    public static Reservation preInsertedReservation2 = new Reservation(
            2L,
            preInsertedCustomer1,
            LocalDate.parse("2024-05-02"),
            preInsertedReservationTime2,
            preInsertedTheme3
    );

    public static Reservation preInsertedReservation3 = new Reservation(
            3L,
            preInsertedCustomer1,
            LocalDate.parse("2024-05-01"),
            preInsertedReservationTime3,
            preInsertedTheme2
    );

    public static Reservation preInsertedReservation4 = new Reservation(
            4L,
            preInsertedCustomer2,
            LocalDate.parse("2024-05-02"),
            preInsertedReservationTime3,
            preInsertedTheme3
    );
}
