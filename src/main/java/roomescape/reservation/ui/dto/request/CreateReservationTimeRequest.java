package roomescape.reservation.ui.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;

public record CreateReservationTimeRequest(
        @JsonFormat(pattern = "HH:mm") LocalTime startAt
) {

}
