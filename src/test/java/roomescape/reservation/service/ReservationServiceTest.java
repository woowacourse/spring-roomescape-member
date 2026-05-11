package roomescape.reservation.service;

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
import roomescape.reservation.exception.ReservationDuplicatedException;
import roomescape.reservation.exception.ReservationNotFoundException;
import roomescape.reservation.payload.ReservationRequest;
import roomescape.reservationtime.entity.ReservationTime;
import roomescape.reservationtime.exception.ReservationTimeNotFoundException;
import roomescape.reservationtime.service.ReservationTimeService;
import roomescape.theme.entity.Theme;
import roomescape.theme.exception.ThemeNotFoundException;
import roomescape.theme.service.ThemeService;

@Transactional
@SpringBootTest
class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationTimeService reservationTimeService;

    @Autowired
    private ThemeService themeService;

    @Test
    void 예약요청을_올바르게_저장하는지_확인하는_테스트() {
        ReservationTime reservationTime = reservationTimeService.save(reservationTimeRequest(LocalTime.of(10, 0)));
        Theme theme = themeService.save(themeRequest("테마"));
        ReservationRequest reservationRequest = reservationRequest(
                "봉구스",
                LocalDate.of(2026, 5, 6),
                reservationTime.getId(),
                theme.getId()
        );

        Reservation reservation = reservationService.save(reservationRequest);

        assertThat(reservation.getName()).isEqualTo(reservationRequest.name());
        assertThat(reservation.getDate()).isEqualTo(reservationRequest.date());
        assertThat(reservation.getTime().getId()).isEqualTo(reservationRequest.timeId());
        assertThat(reservation.getTheme().getId()).isEqualTo(reservationRequest.themeId());
    }

    @Test
    void 없는_예약시간_id를_입력하면_에러를_던진다() {
        Theme theme = themeService.save(themeRequest("테마"));
        ReservationRequest reservationRequest = reservationRequest(
                "봉구스",
                LocalDate.of(2026, 5, 6),
                999L,
                theme.getId()
        );

        assertThatThrownBy(() -> reservationService.save(reservationRequest))
                .isInstanceOf(ReservationTimeNotFoundException.class);
    }

    @Test
    void 없는_테마_id를_입력하면_에러를_던진다() {
        ReservationTime reservationTime = reservationTimeService.save(reservationTimeRequest(LocalTime.of(10, 0)));
        ReservationRequest reservationRequest = reservationRequest(
                "봉구스",
                LocalDate.of(2026, 5, 6),
                reservationTime.getId(),
                999L
        );

        assertThatThrownBy(() -> reservationService.save(reservationRequest))
                .isInstanceOf(ThemeNotFoundException.class);
    }

    @Test
    void 같은_날짜_같은_시간_다른_테마는_예약이_가능하다() {
        ReservationTime reservationTime = reservationTimeService.save(reservationTimeRequest(LocalTime.of(10, 0)));
        Theme theme1 = themeService.save(themeRequest("테마1"));
        Theme theme2 = themeService.save(themeRequest("테마2"));
        ReservationRequest reservationRequest1 = reservationRequest(
                "봉구스",
                LocalDate.of(2026, 5, 6),
                reservationTime.getId(),
                theme1.getId()
        );
        ReservationRequest reservationRequest2 = reservationRequest(
                "봉구스",
                LocalDate.of(2026, 5, 6),
                reservationTime.getId(),
                theme2.getId()
        );
        Reservation reservation1 = reservationService.save(reservationRequest1);
        Reservation reservation2 = reservationService.save(reservationRequest2);

        List<Reservation> reservations = reservationService.findAll();
        assertThat(reservations).contains(reservation1, reservation2);
    }

    @Test
    void 이미_있는_예약을_다시_생성하면_에러를_던진다() {
        ReservationTime reservationTime = reservationTimeService.save(reservationTimeRequest(LocalTime.of(10, 0)));
        Theme theme = themeService.save(themeRequest("테마"));
        ReservationRequest reservationRequest = reservationRequest(
                "봉구스",
                LocalDate.of(2026, 5, 6),
                reservationTime.getId(),
                theme.getId()
        );
        reservationService.save(reservationRequest);

        assertThatThrownBy(() -> reservationService.save(reservationRequest))
                .isInstanceOf(ReservationDuplicatedException.class);
    }


    @Test
    void 예약목록을_올바르게_조회하는지_확인하는_테스트() {
        ReservationTime reservationTime = reservationTimeService.save(reservationTimeRequest(LocalTime.of(10, 0)));
        Theme theme = themeService.save(themeRequest("테마"));
        ReservationRequest reservationRequest = reservationRequest(
                "봉구스",
                LocalDate.of(2026, 5, 6),
                reservationTime.getId(),
                theme.getId()
        );
        Reservation reservation = reservationService.save(reservationRequest);

        List<Reservation> reservations = reservationService.findAll();

        assertThat(reservations).contains(reservation);
    }

    @Test
    void 예약을_올바르게_삭제하는지_확인하는_테스트() {
        ReservationTime reservationTime = reservationTimeService.save(reservationTimeRequest(LocalTime.of(10, 0)));
        Theme theme = themeService.save(themeRequest("테마"));
        ReservationRequest reservationRequest = reservationRequest(
                "봉구스",
                LocalDate.of(2026, 5, 6),
                reservationTime.getId(),
                theme.getId()
        );
        Reservation reservation = reservationService.save(reservationRequest);

        reservationService.deleteById(reservation.getId());

        List<Reservation> reservations = reservationService.findAll();
        assertThat(reservations).doesNotContain(reservation);
    }

    @Test
    void 없는_예약을_삭제하면_에러를_던진다() {
        assertThatThrownBy(() -> reservationService.deleteById(999L))
                .isInstanceOf(ReservationNotFoundException.class);
    }

}
