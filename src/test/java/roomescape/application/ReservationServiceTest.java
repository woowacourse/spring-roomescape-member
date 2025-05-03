package roomescape.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import roomescape.common.BaseTest;
import roomescape.presentation.dto.request.ReservationCreateRequest;
import roomescape.presentation.dto.response.ReservationResponse;
import roomescape.fixture.ReservationDateFixture;
import roomescape.fixture.ReservationTimeDbFixture;
import roomescape.fixture.ReserverNameFixture;
import roomescape.fixture.ThemeDbFixture;
import roomescape.domain.Theme;
import roomescape.presentation.dto.response.ReservationTimeResponse;
import roomescape.domain.ReservationTime;

public class ReservationServiceTest extends BaseTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationTimeDbFixture reservationTimeDbFixture;

    @Autowired
    private ThemeDbFixture themeDbFixture;

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

        ReservationResponse response = reservationService.create(request);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.name()).isEqualTo(ReserverNameFixture.한스.getName());
        assertThat(response.date()).isEqualTo(ReservationDateFixture.예약날짜_25_4_22.getDate());
        assertThat(response.time()).isEqualTo(
                new ReservationTimeResponse(reservationTime.getId(), reservationTime.getStartAt().toString()));
    }

    @Test
    void 예약이_존재하면_예약을_생성할_수_없다() {
        ReservationTime reservationTime = reservationTimeDbFixture.예약시간_10시();
        Theme theme = themeDbFixture.공포();

        reservationService.create(new ReservationCreateRequest(
                ReserverNameFixture.한스.getName(),
                ReservationDateFixture.예약날짜_25_4_22.getDate(),
                reservationTime.getId(),
                theme.getId()
        ));

        ReservationCreateRequest request = new ReservationCreateRequest(
                ReserverNameFixture.한스.getName(),
                ReservationDateFixture.예약날짜_25_4_22.getDate(),
                reservationTime.getId(),
                theme.getId()
        );

        assertThatThrownBy(() -> reservationService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 예약을_모두_조회한다() {
        ReservationTime reservationTime = reservationTimeDbFixture.예약시간_10시();
        Theme theme = themeDbFixture.공포();

        reservationService.create(new ReservationCreateRequest(
                ReserverNameFixture.한스.getName(),
                ReservationDateFixture.예약날짜_25_4_22.getDate(),
                reservationTime.getId(),
                theme.getId()
        ));
        List<ReservationResponse> responses = reservationService.getAll();
        ReservationResponse response = responses.get(0);

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

        reservationService.create(new ReservationCreateRequest(
                ReserverNameFixture.한스.getName(),
                ReservationDateFixture.예약날짜_25_4_22.getDate(),
                reservationTime.getId(),
                theme.getId()
        ));
        reservationService.deleteById(1L);

        List<ReservationResponse> responses = reservationService.getAll();

        assertThat(responses).hasSize(0);
    }

    @Test
    void 존재하지_않는_예약을_삭제할_수_없다() {
        assertThatThrownBy(() -> reservationService.deleteById(3L))
                .isInstanceOf(NoSuchElementException.class);
    }
}
