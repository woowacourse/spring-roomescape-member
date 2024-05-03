package roomescape.controller.response;

import roomescape.domain.ReservationDate;

public record ReservationWebResponse(Long id,
                                     String name,
                                     ReservationDate date,
                                     ReservationTimeWebResponse time,
                                     ThemeWebResponse theme) {

}
