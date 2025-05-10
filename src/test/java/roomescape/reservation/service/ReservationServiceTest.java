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
import roomescape.member.infrastructure.repository.MemberRepository;
import roomescape.reservation.controller.request.ReserveByUserRequest;
import roomescape.reservation.controller.response.ReservationResponse;
import roomescape.reservation.domain.Reservation;
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
    private MemberRepository memberRepository;

    @Autowired
    private ReservationTimeDbFixture reservationTimeDbFixture;

    @Autowired
    private ThemeDbFixture themeDbFixture;

    @Autowired
    private ReservationDbFixture reservationDbFixture;

    @Autowired
    private CleanUp cleanUp;

    @BeforeEach
    void setUp() {
        cleanUp.all();
    }

    @Test
    void 예약을_생성한다() {
        ReservationTime reservationTime = reservationTimeDbFixture.예약시간_10시();
        Theme theme = themeDbFixture.공포();
        Member member = memberRepository.save(Member.createUser("폰트", "email@email.com", "1234"));

        LocalDate now = LocalDate.now();

        ReserveByUserRequest request = new ReserveByUserRequest(
                now.plusDays(1),
                reservationTime.getId(),
                theme.getId(),
                member.getId()
        );

        ReservationResponse response = reservationService.reserve(request);
        System.out.println(response.id());

        assertThat(response.id()).isNotNull();
        assertThat(response.name()).isEqualTo(member.getName());
        assertThat(response.date()).isEqualTo(now.plusDays(1));
        assertThat(response.time()).isEqualTo(
                new ReservationTimeResponse(reservationTime.getId(), reservationTime.getStartAt().toString()));
    }

    @Test
    void 예약이_존재하면_예약을_생성할_수_없다() {
        ReservationTime reservationTime = reservationTimeDbFixture.예약시간_10시();
        Theme theme = themeDbFixture.공포();
        Member member = memberRepository.save(Member.createUser("폰트", "email@email.com", "1234"));

        reservationService.reserve(new ReserveByUserRequest(
                ReservationDateFixture.예약날짜_내일.getDate(),
                reservationTime.getId(),
                theme.getId(),
                member.getId()
        ));

        ReserveByUserRequest request = new ReserveByUserRequest(
                ReservationDateFixture.예약날짜_내일.getDate(),
                reservationTime.getId(),
                theme.getId(),
                member.getId()
        );

        assertThatThrownBy(() -> reservationService.reserve(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 이미 예약이 찼습니다.");
    }

    @Test
    void 예약을_모두_조회한다() {
        ReservationTime reservationTime = reservationTimeDbFixture.예약시간_10시();
        Theme theme = themeDbFixture.공포();
        Reservation reservation = reservationDbFixture.예약_한스_내일_10시_공포(reservationTime, theme);

        List<ReservationResponse> responses = reservationService.getAllReservations();
        ReservationResponse response = responses.get(0);

        assertThat(response.id()).isNotNull();
        assertThat(response.name()).isEqualTo(reservation.getReserverName());
        assertThat(response.date()).isEqualTo(reservation.getDate());
        assertThat(response.time()).isEqualTo(
                new ReservationTimeResponse(reservationTime.getId(), reservationTime.getStartAt().toString()));
    }

    @Test
    void 예약을_삭제한다() {
        ReservationTime reservationTime = reservationTimeDbFixture.예약시간_10시();
        Theme theme = themeDbFixture.공포();
        Reservation reservation = reservationDbFixture.예약_한스_내일_10시_공포(reservationTime, theme);

        reservationService.deleteById(reservation.getId());

        List<ReservationResponse> responses = reservationService.getAllReservations();

        assertThat(responses).hasSize(0);
    }

    @Test
    void 존재하지_않는_예약을_삭제할_수_없다() {
        assertThatThrownBy(() -> reservationService.deleteById(3L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("[ERROR] 예약을 찾을 수 없습니다.");
    }
}
