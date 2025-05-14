package roomescape.domain.reservation.dto.reservationtime;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalTime;

public record ReservationTimeRequest(@JsonFormat(pattern = "HH:mm") LocalTime startAt) {

}
