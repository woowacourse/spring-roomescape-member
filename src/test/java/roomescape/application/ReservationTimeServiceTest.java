package roomescape.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import roomescape.common.BaseTest;
import roomescape.fixture.ReservationDbFixture;
import roomescape.fixture.ReservationTimeDbFixture;
import roomescape.fixture.ThemeDbFixture;
import roomescape.domain.Theme;
import roomescape.presentation.dto.request.ReservationTimeCreateRequest;
import roomescape.presentation.dto.response.ReservationTimeResponse;
import roomescape.domain.ReservationTime;

public class ReservationTimeServiceTest extends BaseTest {

    @Autowired
    private ReservationTimeService reservationTimeService;

    @Autowired
    private ReservationTimeDbFixture reservationTimeDbFixture;

    @Autowired
    private ReservationDbFixture reservationDbFixture;

    @Autowired
    private ThemeDbFixture themeDbFixture;

    @Test
    void 예약시간을_생성한다() {
        ReservationTimeCreateRequest request = new ReservationTimeCreateRequest(LocalTime.of(10, 0));

        ReservationTimeResponse response = reservationTimeService.createReservationTime(request);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.startAt()).isEqualTo("10:00");
    }

    @Test
    void 예약시간을_모두_조회한다() {
        reservationTimeDbFixture.예약시간_10시();
        List<ReservationTimeResponse> responses = reservationTimeService.getReservationTimes();

        assertThat(responses.get(0).startAt()).isEqualTo("10:00");
    }

    @Test
    void 예약시간을_삭제한다() {
        reservationTimeDbFixture.예약시간_10시();
        reservationTimeService.deleteReservationTimeById(1L);

        List<ReservationTimeResponse> responses = reservationTimeService.getReservationTimes();

        assertThat(responses).hasSize(0);
    }

    @Test
    void 존재하지_않는_예약시간을_삭제할_수_없다() {
        assertThatThrownBy(() -> reservationTimeService.deleteReservationTimeById(3L))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void 이미_해당_시간의_예약이_존재한다면_삭제할_수_없다() {
        ReservationTime reservationTime = reservationTimeDbFixture.예약시간_10시();
        Theme theme = themeDbFixture.공포();
        reservationDbFixture.예약_한스_25_4_22_10시_공포(reservationTime, theme);

        assertThatThrownBy(() -> reservationTimeService.deleteReservationTimeById(1L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 이미_존재하는_시간은_추가할_수_없다() {
        reservationTimeDbFixture.예약시간_10시();
        ReservationTimeCreateRequest request = new ReservationTimeCreateRequest(
                LocalTime.of(10, 0));

        assertThatThrownBy(() -> reservationTimeService.createReservationTime(request))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
