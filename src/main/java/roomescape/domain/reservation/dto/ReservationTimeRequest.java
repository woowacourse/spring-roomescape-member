package roomescape.domain.reservation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import java.time.LocalTime;

public record ReservationTimeRequest(@JsonFormat(pattern = "HH:mm") LocalTime startAt) {

}
