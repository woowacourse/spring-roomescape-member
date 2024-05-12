package roomescape.reservation.response;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public record ReservationResponse(long id, String memberName, String themeName, LocalDate date,
                                  @JsonFormat(pattern = "HH:mm") LocalTime startAt) {
}
