package roomescape.time.domain;

import roomescape.common.domain.DomainId;

import java.util.UUID;

public class ReservationTimeId extends DomainId {

    public static final String domainName = "예약 시간 식별자";

    private ReservationTimeId(final Long value, final boolean assigned) {
        super(value, assigned);
    }

    public static ReservationTimeId unassigned() {
        return new ReservationTimeId(UUID.randomUUID().getMostSignificantBits(), false);
    }

    public static ReservationTimeId from(final Long id) {
        return new ReservationTimeId(id, true);
    }
}
