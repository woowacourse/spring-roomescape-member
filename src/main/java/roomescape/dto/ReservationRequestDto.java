package roomescape.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import roomescape.model.Reservation;
import roomescape.model.ReservationDate;
import roomescape.model.ReservationDateTime;
import roomescape.model.ReservationTime;
import roomescape.model.UserName;

public record ReservationRequestDto(@JsonProperty(value = "name", defaultValue = "name") String name,
                                    @JsonProperty("date") LocalDate date,
                                    @JsonProperty("timeId") Long timeId) {

    public Reservation toEntity(Long id, ReservationTime reservationTime) {
        return new Reservation(id,
                new UserName(name()),
                new ReservationDateTime(new ReservationDate(date()), reservationTime));
    }
}
