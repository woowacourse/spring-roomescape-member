package roomescape.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import roomescape.model.Reservation;
import roomescape.model.ReservationTime;
import roomescape.model.Theme;
import roomescape.model.UserName;

public record AdminReservationRequest(
        @NotNull @JsonFormat(pattern = "yyyy-MM-dd") LocalDate date,
        @NotNull Long timeId,
        @NotNull Long themeId,
        @NotNull Long memberId
) {
    public Reservation toEntityWith(UserName name, ReservationTime reservationTime, Theme theme) {
        return new Reservation(
                null,
                name,
                date,
                reservationTime,
                theme
        );
    }
}
