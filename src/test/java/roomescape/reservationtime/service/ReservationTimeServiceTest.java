package roomescape.reservationtime.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalTime;
import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import roomescape.global.exception.RoomEscapeException.BadRequestException;
import roomescape.global.exception.RoomEscapeException.ResourceNotFoundException;
import roomescape.reservation.repository.ReservationDao;
import roomescape.reservationtime.controller.ReservationTimeResponse;
import roomescape.reservationtime.dto.request.ReservationTimeRequest;
import roomescape.reservationtime.dto.response.AdminReservationTimePageResponse;
import roomescape.reservationtime.dto.response.CreateReservationTimeResponse;
import roomescape.reservationtime.repository.ReservationTimeDao;

@DisplayNameGeneration(ReplaceUnderscores.class)
class ReservationTimeServiceTest {
    private ReservationTimeService reservationTimeService;

    @BeforeEach
    void setUp() {
        DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
                .addScript("schema.sql")
                .addScript("data.sql")
                .build();

        ReservationTimeDao reservationTimeDao = new ReservationTimeDao(dataSource);
        ReservationDao reservationDao = new ReservationDao(dataSource);
        reservationTimeService = new ReservationTimeService(reservationDao, reservationTimeDao);
    }

    @Test
    void 예약시간을_정상적으로_추가() {
        ReservationTimeRequest request = new ReservationTimeRequest(LocalTime.of(10, 0));
        CreateReservationTimeResponse response = reservationTimeService.addTime(request);

        assertThat(response.id()).isNotNull();
        assertThat(response.startAt()).isEqualTo(LocalTime.of(10, 0).toString());
    }

    @Test
    void 예약시간이_중복인_경우_예외처리() {
        // given
        ReservationTimeRequest request = new ReservationTimeRequest(LocalTime.of(13, 40));

        // when, then
        assertThatThrownBy(() -> reservationTimeService.addTime(request))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    void 예약시간_리스트_정상적으로_조회() {
        List<ReservationTimeResponse> reservationTimes = reservationTimeService.getReservationTimes();

        assertThat(reservationTimes).hasSize(12);
    }

    @Test
    void 예약시간을_정상적으로_삭제() {
        Long id = 4L;

        reservationTimeService.deleteTime(id);
        List<ReservationTimeResponse> reservationTimes = reservationTimeService.getReservationTimes();

        assertThat(reservationTimes).hasSize(11);
    }

    @Test
    void 삭제하려는_예약시간이_존재하지_않으면_예외() {
        assertThatThrownBy(() -> reservationTimeService.deleteTime(999L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void 삭제_시_해당_시간의_예약_존재_시_예외발생() {
        // given
        Long id = 1L;

        // when, then
        assertThatThrownBy(() -> reservationTimeService.deleteTime(id))
                .isInstanceOf(BadRequestException.class);
    }

    @Test
    void 페이지에_해당하는_예약시간_조회() {
        // given
        int page = 2;

        // when
        AdminReservationTimePageResponse reservationTimesByPage = reservationTimeService.getReservationTimesByPage(
                page);

        // then
        assertThat(reservationTimesByPage.totalPages()).isEqualTo(2);
        assertThat(reservationTimesByPage.reservationTimes()).hasSize(2);
    }
}
