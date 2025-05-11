package roomescape.admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

public record AdminReservationRequest(
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate date,
        Long timeId,
        Long themeId,
        Long memberId
) {

}
