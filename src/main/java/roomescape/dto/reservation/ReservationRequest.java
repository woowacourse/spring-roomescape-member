package roomescape.dto.reservation;

import io.micrometer.common.util.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.theme.Theme;
import roomescape.domain.time.Time;
import roomescape.global.exception.error.ErrorType;
import roomescape.global.exception.model.ValidateException;

import java.time.LocalDate;

public record ReservationRequest(
        String name,

        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate date,

        Long timeId,

        Long themeId
) {

    public ReservationRequest {
        if (StringUtils.isBlank(name) || StringUtils.isBlank(date.toString())
                || StringUtils.isBlank(timeId.toString()) || StringUtils.isBlank(themeId.toString())) {
            throw new ValidateException(ErrorType.REQUEST_DATA_BLANK,
                    String.format("공백 또는 null이 포함된 요청입니다. [values: %s]", this));
        }
    }

    public Reservation toReservation(final Time time, final Theme theme) {
        return new Reservation(this.name, this.date, time, theme);
    }
}
