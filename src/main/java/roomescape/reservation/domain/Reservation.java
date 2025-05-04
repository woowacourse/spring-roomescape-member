package roomescape.reservation.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;
import roomescape.common.validate.Validator;
import roomescape.reservation.exception.PastDateReservationException;
import roomescape.reservation.exception.PastTimeReservationException;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@FieldNameConstants(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
@ToString
public class Reservation {

    public static final String domainName = "예약";

    private final ReservationId id;
    private final ReserverName name;
    private final ReservationDate date;
    private final ReservationTime time;
    private final Theme theme;

    private static Reservation of(final ReservationId id,
                                  final ReserverName name,
                                  final ReservationDate date,
                                  final ReservationTime time,
                                  final Theme theme) {
        validate(id, name, date, time, theme);
        return new Reservation(id, name, date, time, theme);
    }

    public static Reservation withId(final ReservationId id,
                                     final ReserverName name,
                                     final ReservationDate date,
                                     final ReservationTime time,
                                     final Theme theme) {
        return of(id, name, date, time, theme);
    }

    public static Reservation withoutId(final ReserverName name,
                                        final ReservationDate date,
                                        final ReservationTime time,
                                        final Theme theme) {

        validatePast(date, time);
        return of(ReservationId.unassigned(), name, date, time, theme);
    }

    private static void validate(final ReservationId id,
                                 final ReserverName name,
                                 final ReservationDate date,
                                 final ReservationTime time,
                                 final Theme theme) {

        Validator.of(Reservation.class)
                .notNullField(Fields.id, id, ReservationId.domainName)
                .notNullField(Fields.name, name, ReserverName.domainName)
                .notNullField(Fields.date, date, ReservationDate.domainName)
                .notNullField(Fields.time, time, ReservationTime.domainName)
                .notNullField(Fields.theme, theme, Theme.domainName);
    }

    public static void validatePast(final ReservationDate date, final ReservationTime time) {
        final LocalDateTime now = LocalDateTime.now();
        if (date.isAfter(now.toLocalDate())) {
            return;
        }

        if (date.isBefore(now.toLocalDate())) {
            throw new PastDateReservationException(date, now);
        }

        if (time.isBefore(now.toLocalTime())) {
            throw new PastTimeReservationException(time, now);
        }
    }
}
