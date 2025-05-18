package roomescape.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import roomescape.model.Reservation;
import roomescape.model.ReservationTime;
import roomescape.model.Theme;
import roomescape.model.User;

public record AdminReservationRequest(
        @NotNull @JsonFormat(pattern = "yyyy-MM-dd") LocalDate date,
        @NotNull Long memberId,
        @NotNull Long timeId,
        @NotNull Long themeId
) {
    public Reservation toEntityWith(User user, ReservationTime reservationTime, Theme theme) {
        return new Reservation(
                null,
                date,
                user,
                reservationTime,
                theme
        );
    }
}
