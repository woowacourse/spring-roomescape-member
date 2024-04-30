package roomescape.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.util.Objects;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

public record ReservationRequest(
        String name,
        @JsonFormat(pattern = "yyyy-MM-dd") LocalDate date,
        Long timeId
) {

    public ReservationRequest {
        Objects.requireNonNull(name);
        Objects.requireNonNull(date);
        Objects.requireNonNull(timeId);
    }

    public Reservation toEntity(ReservationTime reservationTime) {
        return new Reservation(
                name,
                date,
                reservationTime
        );
    }
}
