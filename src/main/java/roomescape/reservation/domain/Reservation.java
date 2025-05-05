package roomescape.reservation.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;
import roomescape.common.domain.DomainTerm;
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
        id.requireAssigned();
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
                .validateNotNull(Fields.id, id, DomainTerm.RESERVATION_ID.label())
                .validateNotNull(Fields.name, name, DomainTerm.RESERVER_NAME.label())
                .validateNotNull(Fields.date, date, DomainTerm.RESERVATION_DATE.label())
                .validateNotNull(Fields.time, time, DomainTerm.RESERVATION_TIME.label())
                .validateNotNull(Fields.theme, theme, DomainTerm.THEME.label());
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
