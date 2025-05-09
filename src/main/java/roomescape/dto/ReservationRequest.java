package roomescape.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import roomescape.model.Reservation;
import roomescape.model.ReservationDateTime;
import roomescape.model.ReservationTime;
import roomescape.model.Theme;
import roomescape.model.user.UserName;

public record ReservationRequest(
        @JsonProperty(value = "name", defaultValue = "name") @NotNull String name,
        @JsonProperty("date") @NotNull LocalDate date,
        @JsonProperty("timeId") @NotNull Long timeId,
        @JsonProperty("themeId") @NotNull Long themeId
) {
    public Reservation toEntity(Long id, ReservationTime reservationTime, Theme theme) {
        return new Reservation(id,
                new UserName(name()),
                new ReservationDateTime(date(), reservationTime), theme);
    }
}
