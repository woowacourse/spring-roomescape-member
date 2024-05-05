package roomescape.controller.response;

import java.time.LocalDate;
import roomescape.service.response.ReservationAppResponse;

public record ReservationWebResponse(
    Long id,
    String name,
    LocalDate date,
    ReservationTimeWebResponse time,
    ThemeWebResponse theme) {

    public static ReservationWebResponse from(ReservationAppResponse appResponse) {
        return new ReservationWebResponse(
            appResponse.id(),
            appResponse.name(),
            appResponse.date().getDate(),
            ReservationTimeWebResponse.from(appResponse.reservationTimeAppResponse()),
            ThemeWebResponse.from(appResponse.themeAppResponse())
        );
    }
}
