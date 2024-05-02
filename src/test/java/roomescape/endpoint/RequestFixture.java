package roomescape.endpoint;

import java.time.LocalDate;
import java.time.LocalTime;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationTimeRequest;
import roomescape.dto.ThemeRequest;

public class RequestFixture {

    public static Long reservationTimeId = 2L;

    public static ReservationTimeRequest reservationTimeRequest = new ReservationTimeRequest(
            LocalTime.now().plusHours(1)
    );

    public static Long themeId = 2L;

    public static ThemeRequest themeRequest = new ThemeRequest(
            "이름",
            "요약",
            "썸네일"
    );

    public static ReservationRequest reservationRequest = new ReservationRequest(
            "알파카",
            LocalDate.now().plusDays(1),
            reservationTimeId,
            themeId
    );
}
