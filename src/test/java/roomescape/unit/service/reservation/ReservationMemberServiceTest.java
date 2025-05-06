package roomescape.unit.service.reservation;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import roomescape.service.ReservationMemberService;
import roomescape.unit.config.ServiceFixture;

class ReservationMemberServiceTest {

    private static ReservationMemberService reservationMemberService;

    @BeforeAll
    static void setup() {
        reservationMemberService = ServiceFixture.createReservationMemberService();
    }

    @Test
    void 유저정보를_추가하여_예약을_진행한다() {

    }
}
