package roomescape.controller.dto.response;

import java.time.LocalDate;
import roomescape.service.dto.response.ReservationResult;

public record ReservationResponse(long id,
                                  MemberResponse member,
                                  LocalDate date,
                                  ReservationTimeResponse time,
                                  RoomThemeResponse theme) {

    public static ReservationResponse from(ReservationResult result) {
        return new ReservationResponse(result.id(),
                MemberResponse.from(result.member()),
                result.date(),
                ReservationTimeResponse.from(result.time()),
                RoomThemeResponse.from(result.theme()));
    }
}
