package roomescape.dto.member;

import java.time.LocalDate;

public record UserReservationRequest(
        LocalDate date,
        Long timeId,
        Long themeId
) {

}
