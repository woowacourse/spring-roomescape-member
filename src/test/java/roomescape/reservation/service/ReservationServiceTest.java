package roomescape.reservation.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import roomescape.global.exception.InvalidRequestException;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.repository.ReservationTimeRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Test
    @DisplayName("예약을 생성한다.")
    public void create_success() {
        // given
        ReservationTime time = reservationTimeRepository.save(new ReservationTime(LocalTime.of(10, 0)));
        Theme theme = themeRepository.save(new Theme("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.", "https://example.com/theme.png"));

        // when
        Reservation reservation = reservationService.create("브라운", LocalDate.of(2026, 5, 8), time.getId(), theme.getId());

        // then
        assertThat(reservationRepository.findAll()).containsExactly(reservation);
    }

    @Test
    @DisplayName("이미 예약된 날짜, 시간, 테마로 예약하면 예외가 발생한다.")
    public void create_fail_whenDuplicatedReservation() {
        // given
        String name = "브라운";
        LocalDate date = LocalDate.of(2026, 5, 8);
        ReservationTime time = reservationTimeRepository.save(new ReservationTime(LocalTime.of(10, 0)));
        Theme theme = themeRepository.save(new Theme("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.", "https://example.com/theme.png"));

        reservationService.create(name, date, time.getId(), theme.getId());

        // when, then
        assertThatThrownBy(() -> reservationService.create(name, date, time.getId(), theme.getId()))
                .isInstanceOf(InvalidRequestException.class)
                .hasMessage("이미 예약된 시간입니다.");
    }
}
