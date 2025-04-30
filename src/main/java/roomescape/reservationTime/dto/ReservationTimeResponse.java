package roomescape.reservationTime.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;

public record ReservationTimeResponse(Long id, @JsonFormat(pattern = "HH:mm") LocalTime startAt) {

}
