package roomescape.reservation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import roomescape.common.CleanUp;
import roomescape.member.domain.Member;
import roomescape.reservation.controller.request.ReserveByUserRequest;
import roomescape.reservation.controller.response.ReservationResponse;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.fixture.MemberDbFixture;
import roomescape.reservation.fixture.ReservationDateFixture;
import roomescape.reservation.fixture.ReservationDbFixture;
import roomescape.reservation.fixture.ReservationTimeDbFixture;
import roomescape.reservation.fixture.ThemeDbFixture;
import roomescape.theme.domain.Theme;
import roomescape.time.controller.response.ReservationTimeResponse;
import roomescape.time.domain.ReservationTime;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;
    @Autowired
    private ReservationTimeDbFixture reservationTimeDbFixture;
    @Autowired
    private ThemeDbFixture themeDbFixture;
    @Autowired
    private ReservationDbFixture reservationDbFixture;
    @Autowired
    private MemberDbFixture memberDbFixture;
    @Autowired
    private CleanUp cleanUp;

    @BeforeEach
    void setUp() {
        cleanUp.all();
    }

    @Test
    void 예약을_생성한다() {
        ReservationTime reservationTime = reservationTimeDbFixture.열시();
        Theme theme = themeDbFixture.공포();
        Member reserver = memberDbFixture.유저1_생성();
        LocalDate date = ReservationDateFixture.예약날짜_내일.getDate();

        ReserveByUserRequest request = new ReserveByUserRequest(date, reservationTime.getId(), theme.getId());

        // when
        ReservationResponse response = reservationService.reserve(request, reserver.getId());

        assertThat(response.id()).isNotNull();
        assertThat(response.member().name()).isEqualTo(reserver.getName());
        assertThat(response.date()).isEqualTo(date);
        assertThat(response.time()).isEqualTo(ReservationTimeResponse.from(reservationTime));
    }

    @Test
    void 예약이_존재하면_예약을_생성할_수_없다() {
        Reservation reservation = reservationDbFixture.예약_유저1_내일_10시_공포();

        ReserveByUserRequest request = new ReserveByUserRequest(
                ReservationDateFixture.예약날짜_내일.getDate(),
                reservation.getReservationTime().getId(),
                reservation.getTheme().getId()
        );

        assertThatThrownBy(() -> reservationService.reserve(request, reservation.getReserver().getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 이미 예약이 존재합니다.");
    }

    @Test
    void 예약을_모두_조회한다() {
        Reservation reservation = reservationDbFixture.예약_유저1_내일_10시_공포();

        List<ReservationResponse> responses = reservationService.getAllReservations();
        ReservationResponse response = responses.get(0);

        assertThat(response.id()).isNotNull();
        assertThat(response.member().name()).isEqualTo(reservation.getReserverName());
        assertThat(response.date()).isEqualTo(reservation.getDate());
        assertThat(response.time()).isEqualTo(ReservationTimeResponse.from(reservation.getReservationTime()));
    }

    @Test
    void 예약을_삭제한다() {
        Reservation reservation = reservationDbFixture.예약_유저1_내일_10시_공포();

        reservationService.deleteById(reservation.getId());

        assertThat(reservationService.getAllReservations()).hasSize(0);
    }

    @Test
    void 존재하지_않는_예약을_삭제할_수_없다() {
        assertThatThrownBy(() -> reservationService.deleteById(1L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("[ERROR] 예약을 찾을 수 없습니다.");
    }
}
