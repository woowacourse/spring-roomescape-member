package roomescape.unit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import roomescape.common.BaseTest;
import roomescape.unit.fixture.ReservationDbFixture;
import roomescape.unit.fixture.ReservationTimeDbFixture;
import roomescape.unit.fixture.ThemeDbFixture;
import roomescape.theme.domain.Theme;
import roomescape.time.controller.request.CreateReservationTimeRequest;
import roomescape.time.controller.response.ReservationTimeResponse;
import roomescape.time.domain.ReservationTime;
import roomescape.time.service.ReservationTimeService;

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
        CreateReservationTimeRequest request = new CreateReservationTimeRequest(LocalTime.of(10, 0));

        ReservationTimeResponse response = reservationTimeService.createReservationTime(request);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.startAt()).isEqualTo("10:00");
    }

    @Test
    void 예약시간을_모두_조회한다() {
        reservationTimeDbFixture.예약시간_10시();
        List<ReservationTimeResponse> responses = reservationTimeService.getAllReservationTimes();

        assertThat(responses.get(0).startAt()).isEqualTo("10:00");
    }

    @Test
    void 예약시간을_삭제한다() {
        reservationTimeDbFixture.예약시간_10시();
        reservationTimeService.deleteReservationTimeById(1L);

        List<ReservationTimeResponse> responses = reservationTimeService.getAllReservationTimes();

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
        CreateReservationTimeRequest request = new CreateReservationTimeRequest(
                LocalTime.of(10, 0));

        assertThatThrownBy(() -> reservationTimeService.createReservationTime(request))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
