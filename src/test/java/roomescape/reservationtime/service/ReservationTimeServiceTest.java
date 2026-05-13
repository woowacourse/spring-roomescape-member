package roomescape.reservationtime.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static roomescape.config.TestFixture.reservationRequest;
import static roomescape.config.TestFixture.reservationTimeRequest;
import static roomescape.config.TestFixture.themeRequest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservation.entity.Reservation;
import roomescape.reservation.payload.ReservationRequest;
import roomescape.reservation.service.ReservationService;
import roomescape.reservationtime.entity.ReservationTime;
import roomescape.reservationtime.exception.ReservationTimeNotFoundException;
import roomescape.reservationtime.payload.ReservationTimeRequest;
import roomescape.theme.entity.Theme;
import roomescape.theme.service.ThemeService;

@Transactional
@SpringBootTest
class ReservationTimeServiceTest {

    @Autowired
    private ReservationTimeService reservationTimeService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ThemeService themeService;

    @Test
    void 예약시간요청을_올바르게_저장하는지_확인하는_테스트() {
        ReservationTimeRequest request = reservationTimeRequest(LocalTime.of(11, 1));

        ReservationTime reservationTime = reservationTimeService.save(request);

        assertThat(reservationTime.getStartAt()).isEqualTo(request.startAt());
    }

    @Test
    void 예약시간목록을_올바르게_조회하는지_확인하는_테스트() {
        ReservationTimeRequest request1 = reservationTimeRequest(LocalTime.of(11, 2));
        ReservationTimeRequest request2 = reservationTimeRequest(LocalTime.of(11, 3));
        ReservationTime reservationTime1 = reservationTimeService.save(request1);
        ReservationTime reservationTime2 = reservationTimeService.save(request2);

        List<ReservationTime> reservationTimes = reservationTimeService.findAll();

        assertThat(reservationTimes).contains(reservationTime1, reservationTime2);
    }

    @Test
    void 예약가능한_예약시간목록을_올바르게_조회하는지_확인하는_테스트() {
        ReservationTime reservationTime1 = reservationTimeService.save(reservationTimeRequest(LocalTime.of(11, 4)));
        ReservationTime reservationTime2 = reservationTimeService.save(reservationTimeRequest(LocalTime.of(11, 5)));
        Theme theme = themeService.save(themeRequest("테마"));
        ReservationRequest reservationRequest = reservationRequest(
                "봉구스",
                LocalDate.of(2026, 5, 10),
                reservationTime1.getId(),
                theme.getId()
        );
        Reservation reservation = reservationService.save(reservationRequest);

        List<ReservationTime> reservationTimes = reservationTimeService.findAvailableReservationTimes(
                reservation.getDate(),
                reservation.getTheme().getId()
        );

        assertThat(reservationTimes).doesNotContain(reservation.getTime());
        assertThat(reservationTimes).contains(reservationTime2);
    }

    @Test
    void 예약시간을_올바르게_삭제하는지_확인하는_테스트() {
        ReservationTimeRequest request = reservationTimeRequest(LocalTime.of(11, 6));
        ReservationTime reservationTime = reservationTimeService.save(request);

        reservationTimeService.deleteById(reservationTime.getId());

        List<ReservationTime> reservationTimes = reservationTimeService.findAll();
        assertThat(reservationTimes).doesNotContain(reservationTime);
    }

    @Test
    void 없는_예약시간을_삭제하면_에러를_던진다() {
        assertThatThrownBy(() -> reservationTimeService.deleteById(999L))
                .isInstanceOf(ReservationTimeNotFoundException.class);
    }

}
