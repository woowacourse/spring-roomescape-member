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

    public static final Long JUNK_THEME_ID = 1L;
    private static final String JUNK_THEME_NAME = "레벨2";
    private static final String JUNK_THEME_DESCRIPTION = "우테코 레벨2를 탈출하는 내용입니다.";
    private static final String JUNK_THEME_THUMBNAIL = "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg";

    public static final Long JUNK_TIME_SLOT_ID = 1L;
    private static final LocalTime JUNK_TIME_SLOT_START_AT =  LocalTime.of(10, 0);

    public static final AddThemeRequest JUNK_THEME_REQUEST = new AddThemeRequest(
            JUNK_THEME_NAME, JUNK_THEME_DESCRIPTION, JUNK_THEME_THUMBNAIL
    );
    public static final Theme JUNK_THEME = new Theme(
            JUNK_THEME_ID, JUNK_THEME_NAME, JUNK_THEME_DESCRIPTION, JUNK_THEME_THUMBNAIL
    );

    public static final AddTimeSlotRequest JUNK_TIME_SLOT_REQUEST = new AddTimeSlotRequest(
            JUNK_TIME_SLOT_START_AT
    );
    public static final TimeSlot JUNK_TIME_SLOT = new TimeSlot(
            JUNK_TIME_SLOT_ID, JUNK_TIME_SLOT_START_AT
    );

    public static LocalDate getDateOfYesterday() {
        return LocalDate.now().minusDays(1);
    }

    public static LocalDate getDateOfToday() {
        return LocalDate.now();
    }

    public static LocalDate getDateOfTomorrow() {
        return LocalDate.now().plusDays(1);
    }

    public static AddReservationRequest getAddReservationRequestOfYesterday() {
        return getAddReservationRequestOfDate(getDateOfYesterday());
    }

    public static AddReservationRequest getAddReservationRequestOfToday() {
        return getAddReservationRequestOfDate(getDateOfToday());
    }

    public static AddReservationRequest getAddReservationRequestOfTomorrow() {
        return getAddReservationRequestOfDate(getDateOfTomorrow());
    }

    public static Reservation getReservationOfYesterday() {
        var request = getAddReservationRequestOfYesterday();
        return request.toEntity(JUNK_TIME_SLOT, JUNK_THEME);
    }

    public static Reservation getReservationOfToday() {
        var request = getAddReservationRequestOfToday();
        return request.toEntity(JUNK_TIME_SLOT, JUNK_THEME);
    }

    public static Reservation getReservationOfTomorrow() {
        var request = getAddReservationRequestOfTomorrow();
        return request.toEntity(JUNK_TIME_SLOT, JUNK_THEME);
    }

    private static AddReservationRequest getAddReservationRequestOfDate(LocalDate date) {
        return new AddReservationRequest("브라운", date, JUNK_TIME_SLOT_ID, JUNK_THEME_ID);
    }
}
