package roomescape.reservation.domain;

import roomescape.common.domain.DomainId;

import java.util.UUID;

public class ReservationId extends DomainId {

    private ReservationId(final Long value, final boolean assigned) {
        super(value, assigned);
    }

    public static ReservationId unassigned() {
        return new ReservationId(UUID.randomUUID().getMostSignificantBits(), false);
    }

    public static ReservationId from(final Long id) {
        return new ReservationId(id, true);
    }
}
