package roomescape.reservation.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldNameConstants;
import roomescape.common.validate.Validator;
import roomescape.reservation_time.domain.ReservationTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@FieldNameConstants(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
public class Reservation {

    private final ReservationId id;
    private final ReserverName name;
    private final ReservationDate date;
    private final ReservationTime time;

    public static Reservation of(final ReservationId id,
                                 final ReserverName name,
                                 final ReservationDate date,
                                 final ReservationTime time) {
        validate(id, name, date, time);
        return new Reservation(id, name, date, time);
    }

    private static void validate(final ReservationId id,
                                 final ReserverName name,
                                 final ReservationDate date,
                                 final ReservationTime time) {
        Validator.of(Reservation.class)
                .notNullField(Fields.id, id)
                .notNullField(Fields.name, name)
                .notNullField(Fields.date, date)
                .notNullField(Fields.time, time);
    }
}
