package roomescape.reservation.ui.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;

public record CreateReservationTimeRequest(
        @JsonFormat(pattern = "HH:mm") LocalTime startAt
) {

}
