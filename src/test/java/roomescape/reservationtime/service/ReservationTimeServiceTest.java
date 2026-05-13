package roomescape.reservationtime.service;

import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import roomescape.global.exception.ConflictException;
import roomescape.global.exception.NotFoundException;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.repository.ReservationTimeRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class ReservationTimeServiceTest {

    @Autowired
    private ReservationTimeService reservationTimeService;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Test
    @DisplayName("예약 시간을 생성한다.")
    public void create_success() {
        // when
        ReservationTime reservationTime = reservationTimeService.create(LocalTime.of(10, 0));

        // then
        assertThat(reservationTimeService.findAll()).containsExactly(reservationTime);
    }

    @Test
    @DisplayName("이미 존재하는 예약 시간을 생성하면 예외가 발생한다.")
    public void create_fail() {
        // given
        LocalTime startAt = LocalTime.of(23, 59);
        reservationTimeService.create(startAt);

        // when, then
        assertThatThrownBy(() -> reservationTimeService.create(startAt))
                .isInstanceOf(ConflictException.class)
                .hasMessage("이미 등록된 예약 시간입니다. 다른 시간을 입력해주세요.");
    }

    @Test
    @DisplayName("예약 시간을 삭제한다.")
    public void delete_success() {
        // given
        ReservationTime reservationTime = reservationTimeService.create(LocalTime.of(10, 0));

        // when
        reservationTimeService.delete(reservationTime.getId());

        // then
        assertThat(reservationTimeService.findAll()).isEmpty();
    }

    @Test
    @DisplayName("예약이 존재하는 예약 시간을 삭제하면 예외가 발생한다.")
    public void delete_fail_whenReservationExists() {
        // given
        ReservationTime reservationTime = reservationTimeRepository.save(new ReservationTime(LocalTime.of(10, 0)));
        Theme theme = themeRepository.save(new Theme("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.", "https://example.com/theme.png"));
        reservationRepository.save(new Reservation("브라운", LocalDate.of(2026, 5, 14), reservationTime, theme));

        // when, then
        assertThatThrownBy(() -> reservationTimeService.delete(reservationTime.getId()))
                .isInstanceOf(ConflictException.class)
                .hasMessage("예약이 존재하는 시간은 삭제할 수 없습니다. 먼저 해당 예약들을 삭제해주세요.");

        assertThat(reservationTimeService.findAll()).containsExactly(reservationTime);
    }

    @Test
    @DisplayName("특정 날짜 및 테마의 예약 가능한 시간들을 반환한다.")
    public void findAvailableTimes_success() {
        // given
        ReservationTime time = reservationTimeRepository.save(new ReservationTime(LocalTime.of(10, 0)));
        ReservationTime time2 = reservationTimeRepository.save(new ReservationTime(LocalTime.of(12, 0)));
        Theme targetTheme = themeRepository.save(new Theme("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.", "https://example.com/theme.png"));
        Theme nonTargetTheme = themeRepository.save(new Theme("레벨3 탈출", "우테코 레벨3를 탈출하는 내용입니다.", "https://example.com/theme.png"));

        LocalDate targetDate = LocalDate.of(2023, 8, 5);

        reservationRepository.save(new Reservation("브라운", targetDate, time, targetTheme));
        reservationRepository.save(new Reservation("브라운", LocalDate.of(2024, 9, 10), time, targetTheme));
        reservationRepository.save(new Reservation("브라운", targetDate, time, nonTargetTheme));

        // when
        List<ReservationTimeAvailability> availableTimes = reservationTimeService.findAvailableTimes(targetDate, targetTheme.getId());

        // then
        assertThat(availableTimes).hasSize(2)
                .extracting(ReservationTimeAvailability::getReservationTime,
                        ReservationTimeAvailability::isAvailable)
                .containsExactlyInAnyOrder(Tuple.tuple(time, false), Tuple.tuple(time2, true));
    }

    @Test
    @DisplayName("특정 날짜 및 테마의 예약 가능한 시간들을 찾을 때 테마 id가 없으면 예외가 발생한다.")
    public void findAvailableTimes_fail() {
        // given
        LocalDate date = LocalDate.of(2026, 5, 6);
        Long notFoundThemeId = 37L;

        // when, then
        assertThatThrownBy(() -> reservationTimeService.findAvailableTimes(date, notFoundThemeId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("선택한 테마가 존재하지 않습니다. 다른 테마를 선택해주세요.");
    }
}
