package roomescape.domain.reservation;

import lombok.Getter;
import roomescape.domain.reservationdate.ReservationDate;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.domain.theme.Theme;
import roomescape.support.exception.BadRequestException;
import roomescape.support.exception.ReservationErrorCode;
import roomescape.support.exception.ReservationTimeErrorCode;
import roomescape.support.exception.ThemeErrorCode;

@Getter
public class Reservation {

    private final Long id;
    private final String name;
    private final ReservationDate date;
    private final ReservationTime time;
    private final Theme theme;

    private Reservation(
        Long id,
        String name,
        ReservationDate date,
        ReservationTime time,
        Theme theme
    ) {
        validate(name, date, time, theme);
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public static Reservation createWithoutId(
        String name,
        ReservationDate date,
        ReservationTime time,
        Theme theme
    ) {
        return new Reservation(
            null,
            name,
            date,
            time,
            theme
        );
    }

    public static Reservation createWithId(long id, Reservation reservation) {
        return of(
            id,
            reservation.getName(),
            reservation.getDate(),
            reservation.getTime(),
            reservation.getTheme()
        );
    }

    public static Reservation of(
        long id,
        String name,
        ReservationDate date,
        ReservationTime time,
        Theme theme
    ) {
        return new Reservation(
            id,
            name,
            date,
            time,
            theme
        );
    }

    private static void validate(String name, ReservationDate date, ReservationTime time, Theme theme) {
        if (name == null || name.isBlank()) {
            throw new BadRequestException(ReservationErrorCode.INVALID_RESERVATION_NAME);
        }
        if (date == null) {
            throw new BadRequestException(ReservationErrorCode.INVALID_RESERVATION_DATE);
        }
        if (time == null) {
            throw new BadRequestException(ReservationTimeErrorCode.INVALID_RESERVATION_TIME);
        }
        if (theme == null) {
            throw new BadRequestException(ThemeErrorCode.INVALID_THEME);
        }
    }
}
