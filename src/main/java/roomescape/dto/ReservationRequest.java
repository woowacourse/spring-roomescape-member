package roomescape.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import org.hibernate.validator.constraints.Length;
import roomescape.model.Reservation;
import roomescape.model.ReservationTime;

public record ReservationRequest(
        String name,
        @NotNull
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate date,
        Long timeId
) {
    public Reservation toEntityWithReservationTime(ReservationTime reservationTime) {
        return new Reservation(
                null,
                name,
                date,
                reservationTime
        );
    }
}
