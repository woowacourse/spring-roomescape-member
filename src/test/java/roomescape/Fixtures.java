package roomescape;

import java.time.LocalDate;
import java.time.LocalTime;
import roomescape.controller.reservation.dto.AddReservationRequest;
import roomescape.controller.theme.dto.AddThemeRequest;
import roomescape.controller.timeslot.dto.AddTimeSlotRequest;
import roomescape.model.Reservation;
import roomescape.model.Theme;
import roomescape.model.TimeSlot;

public class Fixtures {

    // Inserted Reservations
    public static final TimeSlot JUNK_TIME_SLOT_1 = new TimeSlot(1L, LocalTime.of(10, 0));
    public static final TimeSlot JUNK_TIME_SLOT_2 = new TimeSlot(2L, LocalTime.of(12, 0));
    public static final TimeSlot JUNK_TIME_SLOT_3 = new TimeSlot(3L, LocalTime.of(14, 0));
    public static final Theme JUNK_THEME_1 = new Theme(1L, "레벨1", "설명1", "썸네일1");
    public static final Theme JUNK_THEME_2 = new Theme(2L, "레벨2", "설명2", "썸네일2");
    public static final Theme JUNK_THEME_3 = new Theme(3L, "레벨3", "설명3", "썸네일3");

    public static final Reservation JUNK_RESERVATION_1 = new Reservation(1L, "예약1", LocalDate.of(2025, 4, 24), JUNK_TIME_SLOT_1, JUNK_THEME_2);
    public static final Reservation JUNK_RESERVATION_2 = new Reservation(2L, "예약2", LocalDate.of(2025, 4, 24), JUNK_TIME_SLOT_2, JUNK_THEME_2);
    public static final Reservation JUNK_RESERVATION_3 = new Reservation(3L, "예약3", LocalDate.of(2025, 4, 24), JUNK_TIME_SLOT_3, JUNK_THEME_2);
    public static final Reservation JUNK_RESERVATION_4 = new Reservation(4L, "예약4", LocalDate.of(2025, 4, 24), JUNK_TIME_SLOT_1, JUNK_THEME_1);
    public static final Reservation JUNK_RESERVATION_5 = new Reservation(5L, "예약5", LocalDate.of(2025, 4, 25), JUNK_TIME_SLOT_2, JUNK_THEME_3);
    public static final Reservation JUNK_RESERVATION_6 = new Reservation(6L, "예약6", LocalDate.of(2025, 4, 25), JUNK_TIME_SLOT_3, JUNK_THEME_3);

    // TimeSlot to insert
    public static final AddTimeSlotRequest JUNK_TIME_SLOT_REQUEST = new AddTimeSlotRequest(LocalTime.of(16, 0));
    public static final TimeSlot JUNK_TIME_SLOT = new TimeSlot(4L, LocalTime.of(16, 0));

    // Theme to insert
    public static final AddThemeRequest JUNK_THEME_REQUEST = new AddThemeRequest("레벨4", "설명4", "썸네일4");
    public static final Theme JUNK_THEME = new Theme(4L, "레벨4", "설명4", "썸네일4");

    // Reservation to insert
    public static final Reservation JUNK_RESERVATION = new Reservation(7L, "예약", LocalDate.of(2025, 4, 26), JUNK_TIME_SLOT_1, JUNK_THEME_1);


    public static LocalDate getDateOfTomorrow() {
        return LocalDate.now().plusDays(1);
    }

    public static AddReservationRequest getAddReservationRequestOfTomorrow() {
        return getAddReservationRequestOfDate(getDateOfTomorrow());
    }

    public static Reservation getReservationOfTomorrow() {
        var request = getAddReservationRequestOfTomorrow();
        return request.toEntity(JUNK_TIME_SLOT_1, JUNK_THEME_1);
    }

    private static AddReservationRequest getAddReservationRequestOfDate(LocalDate date) {
        return new AddReservationRequest("리버", date, 1L, 1L);
    }
}
