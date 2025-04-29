package roomescape.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

public record ReservationRequest(
    @JsonFormat(pattern = "yyyy-MM-dd") LocalDate date,
    String name,
    Long timeId) {

    public static Reservation toEntity(ReservationRequest request, ReservationTime time) {
        return new Reservation(null, request.name(), request.date(), time);
    }
}
