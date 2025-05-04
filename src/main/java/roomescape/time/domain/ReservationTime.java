package roomescape.time.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;
import roomescape.common.validate.Validator;

import java.time.LocalTime;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@FieldNameConstants(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = "id")
@ToString
public class ReservationTime {

    public static final String domainName = "예약 시간";

    private final ReservationTimeId id;
    private final LocalTime startAt;

    private static ReservationTime of(final ReservationTimeId id, final LocalTime value) {
        validate(id, value);
        return new ReservationTime(id, value);
    }

    public static ReservationTime withId(final ReservationTimeId id, final LocalTime value) {
        id.requireAssigned();
        return of(id, value);
    }

    public static ReservationTime withoutId(final LocalTime value) {
        return of(ReservationTimeId.unassigned(), value);
    }

    private static void validate(final ReservationTimeId id, final LocalTime value) {
        Validator.of(ReservationTime.class)
                .notNullField(Fields.id, id, ReservationTimeId.domainName)
                .notNullField(Fields.startAt, value, domainName);
    }

    public boolean isBefore(final LocalTime time) {
        return startAt.isBefore(time);
    }
}
