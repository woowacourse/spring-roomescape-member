package roomescape.reservation.presentation.fixture;

import java.time.LocalDate;
import java.time.LocalTime;
import roomescape.reservation.presentation.dto.MemberReservationRequest;
import roomescape.reservation.presentation.dto.ReservationTimeRequest;
import roomescape.reservation.presentation.dto.ThemeRequest;

public class ReservationFixture {

    public MemberReservationRequest createMemberReservation(String date, String themeId, String timeId) {
        return new MemberReservationRequest(LocalDate.parse(date), Long.valueOf(themeId), Long.valueOf(timeId));
    }

    public ReservationTimeRequest createReservationTime(String startAt) {
        return new ReservationTimeRequest(LocalTime.parse(startAt));
    }

    public ThemeRequest createTheme(String name, String description, String thumbnail) {
        return new ThemeRequest(name, description, thumbnail);
    }
}
