package roomescape.reservation.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static roomescape.config.TestFixture.futureReservationDate;
import static roomescape.config.TestFixture.nextReservationDate;
import static roomescape.config.TestFixture.pastReservationDate;
import static roomescape.config.TestFixture.reservationRequest;
import static roomescape.config.TestFixture.reservationTimeRequest;
import static roomescape.config.TestFixture.reservationupdateRequest;
import static roomescape.config.TestFixture.themeRequest;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import roomescape.common.exception.AccessDeniedException;
import roomescape.common.exception.DuplicatedException;
import roomescape.common.exception.NotFoundException;
import roomescape.common.exception.PastDateTimeException;
import roomescape.reservation.entity.Reservation;
import roomescape.reservation.payload.ReservationRequest;
import roomescape.reservationtime.entity.ReservationTime;
import roomescape.reservationtime.service.ReservationTimeService;
import roomescape.theme.entity.Theme;
import roomescape.theme.service.ThemeService;

@Transactional
@SpringBootTest
class ReservationServiceTest {

    private static final String DEFAULT_RESERVATION_NAME = "봉구스";
    private static final String OTHER_RESERVATION_NAME = "밀란";
    private static final String DEFAULT_THEME_NAME = "테마";
    private static final String FIRST_THEME_NAME = "테마1";
    private static final String SECOND_THEME_NAME = "테마2";
    private static final LocalTime DEFAULT_START_AT = LocalTime.of(10, 0);
    private static final Long NOT_FOUND_ID = 999L;

    @Autowired
    private Clock clock;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationTimeService reservationTimeService;

    @Autowired
    private ThemeService themeService;

    @Test
    void 예약요청을_올바르게_저장하는지_확인하는_테스트() {
        // given
        ReservationTime reservationTime = reservationTimeService.save(reservationTimeRequest(DEFAULT_START_AT));
        Theme theme = themeService.save(themeRequest(DEFAULT_THEME_NAME));
        ReservationRequest reservationRequest = reservationRequest(
                DEFAULT_RESERVATION_NAME,
                futureReservationDate(clock),
                reservationTime.getId(),
                theme.getId()
        );

        // when
        Reservation reservation = reservationService.save(reservationRequest);

        // then
        assertThat(reservation.getName()).isEqualTo(reservationRequest.name());
        assertThat(reservation.getDate()).isEqualTo(reservationRequest.date());
        assertThat(reservation.getTime().getId()).isEqualTo(reservationRequest.timeId());
        assertThat(reservation.getTheme().getId()).isEqualTo(reservationRequest.themeId());
    }

    @Test
    void 없는_예약시간_id를_입력하면_에러를_던진다() {
        // given
        Theme theme = themeService.save(themeRequest(DEFAULT_THEME_NAME));
        ReservationRequest reservationRequest = reservationRequest(
                DEFAULT_RESERVATION_NAME,
                futureReservationDate(clock),
                NOT_FOUND_ID,
                theme.getId()
        );

        // when & then
        assertThatThrownBy(() -> reservationService.save(reservationRequest))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void 없는_테마_id를_입력하면_에러를_던진다() {
        // given
        ReservationTime reservationTime = reservationTimeService.save(reservationTimeRequest(DEFAULT_START_AT));
        ReservationRequest reservationRequest = reservationRequest(
                DEFAULT_RESERVATION_NAME,
                futureReservationDate(clock),
                reservationTime.getId(),
                NOT_FOUND_ID
        );

        // when & then
        assertThatThrownBy(() -> reservationService.save(reservationRequest))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void 같은_날짜_같은_시간_다른_테마는_예약이_가능하다() {
        // given
        ReservationTime reservationTime = reservationTimeService.save(reservationTimeRequest(DEFAULT_START_AT));
        Theme theme1 = themeService.save(themeRequest(FIRST_THEME_NAME));
        Theme theme2 = themeService.save(themeRequest(SECOND_THEME_NAME));
        ReservationRequest reservationRequest1 = reservationRequest(
                DEFAULT_RESERVATION_NAME,
                futureReservationDate(clock),
                reservationTime.getId(),
                theme1.getId()
        );
        ReservationRequest reservationRequest2 = reservationRequest(
                DEFAULT_RESERVATION_NAME,
                futureReservationDate(clock),
                reservationTime.getId(),
                theme2.getId()
        );
        Reservation reservation1 = reservationService.save(reservationRequest1);
        Reservation reservation2 = reservationService.save(reservationRequest2);

        // when
        List<Reservation> reservations = reservationService.findAll();

        // then
        assertThat(reservations).contains(reservation1, reservation2);
    }

    @Test
    void 이미_있는_예약을_다시_생성하면_에러를_던진다() {
        // given
        ReservationTime reservationTime = reservationTimeService.save(reservationTimeRequest(DEFAULT_START_AT));
        Theme theme = themeService.save(themeRequest(DEFAULT_THEME_NAME));
        ReservationRequest reservationRequest = reservationRequest(
                DEFAULT_RESERVATION_NAME,
                futureReservationDate(clock),
                reservationTime.getId(),
                theme.getId()
        );
        reservationService.save(reservationRequest);

        // when & then
        assertThatThrownBy(() -> reservationService.save(reservationRequest))
                .isInstanceOf(DuplicatedException.class);
    }

    @Test
    void 예약_수정을_올바르게_수행하는_테스트() {
        // given
        ReservationTime reservationTime = reservationTimeService.save(reservationTimeRequest(DEFAULT_START_AT));
        Theme theme = themeService.save(themeRequest(DEFAULT_THEME_NAME));
        LocalDate date1 = futureReservationDate(clock);
        ReservationRequest reservationRequest = reservationRequest(
                DEFAULT_RESERVATION_NAME,
                date1,
                reservationTime.getId(),
                theme.getId()
        );
        Reservation savedReservation = reservationService.save(reservationRequest);

        LocalDate date2 = date1.plusDays(1);

        // when
        Reservation updateReservation = reservationService.update(
                savedReservation.getId(),
                reservationupdateRequest(date2, reservationTime.getId())
        );

        // then
        assertThat(updateReservation.getDate()).isEqualTo(date2);
        assertThat(updateReservation.getTime().getId()).isEqualTo(reservationTime.getId());
    }

    @Test
    void 예약목록을_올바르게_조회하는지_확인하는_테스트() {
        // given
        ReservationTime reservationTime = reservationTimeService.save(reservationTimeRequest(DEFAULT_START_AT));
        Theme theme = themeService.save(themeRequest(DEFAULT_THEME_NAME));
        ReservationRequest reservationRequest = reservationRequest(
                DEFAULT_RESERVATION_NAME,
                futureReservationDate(clock),
                reservationTime.getId(),
                theme.getId()
        );
        Reservation reservation = reservationService.save(reservationRequest);

        // when
        List<Reservation> reservations = reservationService.findAll();

        // then
        assertThat(reservations).contains(reservation);
    }

    @Test
    void 예약을_올바르게_삭제하는지_확인하는_테스트() {
        // given
        ReservationTime reservationTime = reservationTimeService.save(reservationTimeRequest(DEFAULT_START_AT));
        Theme theme = themeService.save(themeRequest(DEFAULT_THEME_NAME));
        ReservationRequest reservationRequest = reservationRequest(
                DEFAULT_RESERVATION_NAME,
                futureReservationDate(clock),
                reservationTime.getId(),
                theme.getId()
        );
        Reservation reservation = reservationService.save(reservationRequest);

        // when
        reservationService.deleteById(reservation.getId());

        // then
        List<Reservation> reservations = reservationService.findAll();
        assertThat(reservations).doesNotContain(reservation);
    }

    @Test
    void 예약을_id와_이름으로_올바르게_삭제하는지_확인하는_테스트() {
        // given
        ReservationTime reservationTime = reservationTimeService.save(reservationTimeRequest(DEFAULT_START_AT));
        Theme theme = themeService.save(themeRequest(DEFAULT_THEME_NAME));
        ReservationRequest reservationRequest1 = reservationRequest(
                OTHER_RESERVATION_NAME,
                futureReservationDate(clock),
                reservationTime.getId(),
                theme.getId()
        );
        ReservationRequest reservationRequest2 = reservationRequest(
                OTHER_RESERVATION_NAME,
                nextReservationDate(clock),
                reservationTime.getId(),
                theme.getId()
        );
        Reservation reservation = reservationService.save(reservationRequest1);
        Reservation sameNameReservation = reservationService.save(reservationRequest2);

        // when
        reservationService.deleteByIdAndName(reservation.getId(), reservation.getName());

        // then
        List<Reservation> reservations = reservationService.findAll();
        assertThat(reservations).doesNotContain(reservation);
        assertThat(reservations).contains(sameNameReservation);
    }

    @Test
    void 예약_id와_이름이_일치하지_않으면_삭제하지_않는다() {
        // given
        ReservationTime reservationTime = reservationTimeService.save(reservationTimeRequest(DEFAULT_START_AT));
        Theme theme = themeService.save(themeRequest(DEFAULT_THEME_NAME));
        ReservationRequest reservationRequest = reservationRequest(
                OTHER_RESERVATION_NAME,
                futureReservationDate(clock),
                reservationTime.getId(),
                theme.getId()
        );
        Reservation reservation = reservationService.save(reservationRequest);

        // when & then
        assertThatThrownBy(() -> reservationService.deleteByIdAndName(reservation.getId(), DEFAULT_RESERVATION_NAME))
                .isInstanceOf(AccessDeniedException.class);
        assertThat(reservationService.findAll()).contains(reservation);
    }

    @Test
    void 없는_예약을_삭제하면_에러를_던진다() {
        // when & then
        assertThatThrownBy(() -> reservationService.deleteById(NOT_FOUND_ID))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void 지난_날짜와_시간으로_예약하면_에러를_던진다() {
        // given
        ReservationTime reservationTime = reservationTimeService.save(reservationTimeRequest(DEFAULT_START_AT));
        Theme theme = themeService.save(themeRequest(DEFAULT_THEME_NAME));
        ReservationRequest reservationRequest = reservationRequest(
                DEFAULT_RESERVATION_NAME,
                pastReservationDate(clock),
                reservationTime.getId(),
                theme.getId()
        );

        // when & then
        assertThatThrownBy(() -> reservationService.save(reservationRequest))
                .isInstanceOf(PastDateTimeException.class);
    }

    @Test
    void 이름으로_예약들을_조회한다() {
        // given
        ReservationTime reservationTime = reservationTimeService.save(reservationTimeRequest(DEFAULT_START_AT));
        Theme theme = themeService.save(themeRequest(DEFAULT_THEME_NAME));
        ReservationRequest reservationRequest = reservationRequest(
                OTHER_RESERVATION_NAME,
                futureReservationDate(clock),
                reservationTime.getId(),
                theme.getId()
        );
        Reservation savedReservation = reservationService.save(reservationRequest);

        // when
        List<Reservation> reservations = reservationService.findAllByName(OTHER_RESERVATION_NAME);

        // then
        assertThat(reservations).contains(savedReservation);
    }

}
