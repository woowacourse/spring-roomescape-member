package roomescape.reservationtime.domain;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import roomescape.reservationtime.exception.InValidReservationTimeException;
import roomescape.reservationtime.exception.ReservationTimeErrorCode;
import roomescape.theme.domain.Theme;

@Getter
@EqualsAndHashCode(of = "id")
public class ReservationTime {

    private final Long id;
    private final LocalTime startAt;
    private final Theme theme;

    private ReservationTime(final Long id, final LocalTime startAt, final Theme theme) {
        validateStartAt(startAt);

        this.id = id;
        this.startAt = startAt;
        this.theme = theme;
    }

    public static ReservationTime createNew(final LocalTime startAt, final Theme theme) {
        return new ReservationTime(null, startAt, theme);
    }

    public static ReservationTime of(final long id, final LocalTime startAt, final Theme theme) {
        return new ReservationTime(id, startAt, theme);
    }

    private void validateStartAt(LocalTime startAt) {
        List<String> errors = new ArrayList<>();

        if (startAt == null) {
            errors.add(ReservationTimeErrorCode.RESERVATION_TIME_NOT_FOUND.getMessage());
        }

        if (!errors.isEmpty()) {
            throw new InValidReservationTimeException(errors);
        }
    }

    public ReservationTime withId(final long id) {
        return new ReservationTime(id, this.startAt, this.theme);
    }

}
