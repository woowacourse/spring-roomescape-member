package roomescape.reservation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalTime;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import roomescape.common.RoomescapeTestSupport;
import roomescape.reservation.fixture.ReservationDateFixture;
import roomescape.reservation.fixture.ReservationDbFixture;
import roomescape.reservation.fixture.ReservationTimeDbFixture;
import roomescape.reservation.fixture.ThemeDbFixture;
import roomescape.theme.domain.Theme;
import roomescape.time.controller.request.AvailableReservationTimeRequest;
import roomescape.time.controller.request.ReservationTimeCreateRequest;
import roomescape.time.controller.response.AvailableReservationTimeResponse;
import roomescape.time.controller.response.ReservationTimeResponse;
import roomescape.time.domain.ReservationTime;
import roomescape.time.service.ReservationTimeService;

class ReservationTimeServiceTest extends RoomescapeTestSupport {

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

        ReservationTimeResponse response = reservationTimeService.create(request);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.startAt()).isEqualTo("10:00");
    }

    @Test
    void 예약시간을_모두_조회한다() {
        reservationTimeDbFixture.예약시간_10시();
        List<ReservationTimeResponse> responses = reservationTimeService.getAll();

        assertThat(responses.get(0).startAt()).isEqualTo("10:00");
    }

    @Test
    void 예약시간을_삭제한다() {
        reservationTimeDbFixture.예약시간_10시();
        reservationTimeService.deleteById(1L);

        List<ReservationTimeResponse> responses = reservationTimeService.getAll();

        assertThat(responses).hasSize(0);
    }

    @Test
    void 존재하지_않는_예약시간을_삭제할_수_없다() {
        assertThatThrownBy(() -> reservationTimeService.deleteById(3L))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void 이미_해당_시간의_예약이_존재한다면_삭제할_수_없다() {
        ReservationTime reservationTime = reservationTimeDbFixture.예약시간_10시();
        Theme theme = themeDbFixture.공포();
        reservationDbFixture.예약_한스_내일_10시_공포(reservationTime, theme);

        assertThatThrownBy(() -> reservationTimeService.deleteById(1L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 이미_존재하는_시간은_추가할_수_없다() {
        reservationTimeDbFixture.예약시간_10시();
        ReservationTimeCreateRequest request = new ReservationTimeCreateRequest(
                LocalTime.of(10, 0));

        assertThatThrownBy(() -> reservationTimeService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 예약_가능한_시간을_조회한다() {
        ReservationTime reservationTime = reservationTimeDbFixture.예약시간_10시();
        Theme theme = themeDbFixture.공포();

        reservationDbFixture.예약_한스_내일_10시_공포(reservationTime, theme);

        AvailableReservationTimeRequest request = new AvailableReservationTimeRequest(
                ReservationDateFixture.예약날짜_내일.getDate(),
                theme.getId()
        );

        List<AvailableReservationTimeResponse> responses = reservationTimeService.getAvailableReservationTimes(request);

        assertThat(responses.get(0).id()).isEqualTo(theme.getId());
        assertThat(responses.get(0).startAt()).isEqualTo(reservationTime.getStartAt());
        assertThat(responses.get(0).isReserved()).isEqualTo(true);
    }
}
