package roomescape.reservation.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldNameConstants;
import roomescape.common.validate.Validator;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.ReservationTime;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@FieldNameConstants(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
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
                .notNullField(Fields.id, id)
                .notNullField(Fields.name, name)
                .notNullField(Fields.date, date)
                .notNullField(Fields.time, time)
                .notNullField(Fields.theme, theme);
    }

    public static void validatePast(final ReservationDate date, final ReservationTime time) {
        final LocalDateTime now = LocalDateTime.now();
        if (date.isAfter(now.toLocalDate())) {
            return;
        }
        // TODO: 메시지 정의
        if (date.isBefore(now.toLocalDate())) {
            throw new IllegalArgumentException();
        }
        if (time.isBefore(now.toLocalTime())) {
            throw new IllegalArgumentException();
        }
    }
}
