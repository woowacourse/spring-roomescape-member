package roomescape.application;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import roomescape.common.BaseTest;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.fixture.ReservationDateFixture;
import roomescape.fixture.ReservationDbFixture;
import roomescape.fixture.ReservationTimeDbFixture;
import roomescape.fixture.ReserverNameFixture;
import roomescape.fixture.ThemeDbFixture;
import roomescape.presentation.dto.request.ReservationCreateRequest;
import roomescape.presentation.dto.response.ReservationResponse;
import roomescape.presentation.dto.response.ReservationTimeResponse;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;

public class ReservationServiceTest extends BaseTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationTimeDbFixture reservationTimeDbFixture;

    @Autowired
    private ThemeDbFixture themeDbFixture;

    @Autowired
    private ReservationDbFixture reservationDbFixture;

    @Test
    void 예약을_생성한다() {
        ReservationTime reservationTime = reservationTimeDbFixture.예약시간_10시();
        Theme theme = themeDbFixture.공포();

        ReservationCreateRequest request = new ReservationCreateRequest(
                ReserverNameFixture.한스.getName(),
                ReservationDateFixture.예약날짜_25_4_22.getDate(),
                reservationTime.getId(),
                theme.getId()
        );

        ReservationResponse response = reservationService.createReservation(request);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.name()).isEqualTo(ReserverNameFixture.한스.getName());
        assertThat(response.date()).isEqualTo(ReservationDateFixture.예약날짜_25_4_22.getDate());
        assertThat(response.time()).isEqualTo(
                new ReservationTimeResponse(reservationTime.getId(), reservationTime.getStartAt().toString()));
    }

    @Test
    void 같은일시_같은테마_예약이_존재하면_예약을_생성할_수_없다() {
        ReservationTime reservationTime = reservationTimeDbFixture.예약시간_10시();
        Theme theme = themeDbFixture.공포();
        reservationDbFixture.예약_한스_25_4_22_10시_공포(reservationTime, theme);

        ReservationCreateRequest request = new ReservationCreateRequest(
                ReserverNameFixture.한스.getName(),
                ReservationDateFixture.예약날짜_25_4_22.getDate(),
                reservationTime.getId(),
                theme.getId()
        );

        assertThatThrownBy(() -> reservationService.createReservation(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 같은일시_다른테마_예약은_생성할_수_있다() {
        ReservationTime reservationTime = reservationTimeDbFixture.예약시간_10시();
        Theme horror = themeDbFixture.공포();
        Theme mystery = themeDbFixture.커스텀_테마("미스테리");
        reservationDbFixture.예약_한스_25_4_22_10시_공포(reservationTime, horror);

        ReservationCreateRequest request = new ReservationCreateRequest(
                ReserverNameFixture.한스.getName(),
                ReservationDateFixture.예약날짜_25_4_22.getDate(),
                reservationTime.getId(),
                mystery.getId()
        );

        assertThatCode(() -> reservationService.createReservation(request))
                .doesNotThrowAnyException();
    }

    @Test
    void 예약을_모두_조회한다() {
        ReservationTime reservationTime = reservationTimeDbFixture.예약시간_10시();
        Theme theme = themeDbFixture.공포();
        reservationDbFixture.예약_한스_25_4_22_10시_공포(reservationTime, theme);

        List<ReservationResponse> responses = reservationService.getReservations();
        ReservationResponse response = responses.getFirst();

        assertThat(responses).hasSize(1);
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.name()).isEqualTo(ReserverNameFixture.한스.getName());
        assertThat(response.date()).isEqualTo(ReservationDateFixture.예약날짜_25_4_22.getDate());
        assertThat(response.time()).isEqualTo(
                new ReservationTimeResponse(reservationTime.getId(), reservationTime.getStartAt().toString()));
    }

    @Test
    void 예약을_삭제한다() {
        ReservationTime reservationTime = reservationTimeDbFixture.예약시간_10시();
        Theme theme = themeDbFixture.공포();
        Reservation reservation = reservationDbFixture.예약_한스_25_4_22_10시_공포(reservationTime, theme);

        reservationService.deleteReservationById(reservation.getId());

        List<ReservationResponse> responses = reservationService.getReservations();
        assertThat(responses).hasSize(0);
    }

    @Test
    void 존재하지_않는_예약을_삭제할_수_없다() {
        assertThatThrownBy(() -> reservationService.deleteReservationById(3L))
                .isInstanceOf(NoSuchElementException.class);
    }
}
