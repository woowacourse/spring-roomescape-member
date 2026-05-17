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
import roomescape.common.exception.NotFoundException;
import roomescape.reservation.entity.Reservation;
import roomescape.reservation.payload.ReservationRequest;
import roomescape.reservation.service.ReservationService;
import roomescape.reservationtime.entity.ReservationTime;
import roomescape.reservationtime.payload.ReservationTimeRequest;
import roomescape.theme.entity.Theme;
import roomescape.theme.service.ThemeService;

@Transactional
@SpringBootTest
class ReservationTimeServiceTest {

    private static final String RESERVATION_NAME = "밀란";
    private static final String THEME_NAME = "테마";
    private static final LocalDate RESERVATION_DATE = LocalDate.of(2026, 5, 10);
    private static final LocalTime FIRST_START_AT = LocalTime.of(11, 1);
    private static final LocalTime SECOND_START_AT = LocalTime.of(11, 2);
    private static final LocalTime THIRD_START_AT = LocalTime.of(11, 3);
    private static final LocalTime RESERVED_START_AT = LocalTime.of(11, 4);
    private static final LocalTime AVAILABLE_START_AT = LocalTime.of(11, 5);
    private static final LocalTime DELETE_START_AT = LocalTime.of(11, 6);
    private static final Long NOT_FOUND_ID = 999L;

    @Autowired
    private ReservationTimeService reservationTimeService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ThemeService themeService;

    @Test
    void 예약시간요청을_올바르게_저장하는지_확인하는_테스트() {
        // given
        ReservationTimeRequest request = reservationTimeRequest(FIRST_START_AT);

        // when
        ReservationTime reservationTime = reservationTimeService.save(request);

        // then
        assertThat(reservationTime.getStartAt()).isEqualTo(request.startAt());
    }

    @Test
    void 예약시간목록을_올바르게_조회하는지_확인하는_테스트() {
        // given
        ReservationTimeRequest request1 = reservationTimeRequest(SECOND_START_AT);
        ReservationTimeRequest request2 = reservationTimeRequest(THIRD_START_AT);
        ReservationTime reservationTime1 = reservationTimeService.save(request1);
        ReservationTime reservationTime2 = reservationTimeService.save(request2);

        // when
        List<ReservationTime> reservationTimes = reservationTimeService.findAll();

        // then
        assertThat(reservationTimes).contains(reservationTime1, reservationTime2);
    }

    @Test
    void 예약가능한_예약시간목록을_올바르게_조회하는지_확인하는_테스트() {
        // given
        ReservationTime reservationTime1 = reservationTimeService.save(reservationTimeRequest(RESERVED_START_AT));
        ReservationTime reservationTime2 = reservationTimeService.save(reservationTimeRequest(AVAILABLE_START_AT));
        Theme theme = themeService.save(themeRequest(THEME_NAME));
        ReservationRequest reservationRequest = reservationRequest(
                RESERVATION_NAME,
                RESERVATION_DATE,
                reservationTime1.getId(),
                theme.getId()
        );
        Reservation reservation = reservationService.save(reservationRequest);

        // when
        List<ReservationTime> reservationTimes = reservationTimeService.findAvailableReservationTimes(
                reservation.getDate(),
                reservation.getTheme().getId()
        );

        // then
        assertThat(reservationTimes).doesNotContain(reservation.getTime());
        assertThat(reservationTimes).contains(reservationTime2);
    }

    @Test
    void 존재하지_않는_테마의_예약가능시간을_조회하면_에러를_던진다() {
        // when & then
        assertThatThrownBy(() -> reservationTimeService.findAvailableReservationTimes(RESERVATION_DATE, NOT_FOUND_ID))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void 예약시간을_올바르게_삭제하는지_확인하는_테스트() {
        // given
        ReservationTimeRequest request = reservationTimeRequest(DELETE_START_AT);
        ReservationTime reservationTime = reservationTimeService.save(request);

        // when
        reservationTimeService.deleteById(reservationTime.getId());

        // then
        List<ReservationTime> reservationTimes = reservationTimeService.findAll();
        assertThat(reservationTimes).doesNotContain(reservationTime);
    }

    @Test
    void 없는_예약시간을_삭제하면_에러를_던진다() {
        // when & then
        assertThatThrownBy(() -> reservationTimeService.deleteById(NOT_FOUND_ID))
                .isInstanceOf(NotFoundException.class);
    }

}
