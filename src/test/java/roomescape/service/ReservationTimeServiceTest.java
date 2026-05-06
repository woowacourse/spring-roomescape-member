package roomescape.service;

import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReservationTimeAvailability;
import roomescape.domain.Theme;
import roomescape.exception.DomainException;
import roomescape.exception.ErrorCode;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationTimeServiceTest {

    @Autowired
    ReservationTimeService reservationTimeService;

    @Autowired
    ReservationTimeRepository reservationTimeRepository;

    @Autowired
    ThemeRepository themeRepository;

    @Autowired
    ReservationRepository reservationRepository;

    @Test
    @DisplayName("이미 존재하는 예약 시간을 생성하면 예외가 발생한다.")
    public void create_fail() {
        // given
        LocalTime startAt = LocalTime.of(23, 59);
        reservationTimeService.create(startAt);

        // when, then
        assertThatThrownBy(() -> reservationTimeService.create(startAt))
                .isInstanceOf(DomainException.class)
                .hasMessage(ErrorCode.RESERVATION_TIME_ALREADY_EXISTS.message());
    }

    @Test
    @DisplayName("특정 날짜 및 테마의 예약 가능한 시간들을 반환한다.")
    public void findAvailableTimes_success() {
        // given
        ReservationTime time = reservationTimeRepository.save(new ReservationTime(LocalTime.of(10, 0)));
        ReservationTime time2 = reservationTimeRepository.save(new ReservationTime(LocalTime.of(12, 0)));
        Theme targetTheme = themeRepository.save(new Theme("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.", "https://example.com/theme.png"));
        Theme nonTargetTheme = themeRepository.save(new Theme("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.", "https://example.com/theme.png"));

        LocalDate targetDate = LocalDate.of(2023, 8, 5);
        Reservation targetReservation = reservationRepository.save(new Reservation("브라운", targetDate, time, targetTheme));
        Reservation nonTargetReservation1 = reservationRepository.save(new Reservation("브라운", LocalDate.of(2024, 9, 10), time, targetTheme));
        Reservation nonTargetReservation2 = reservationRepository.save(new Reservation("브라운", targetDate, time, nonTargetTheme));


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
        // when, then
        assertThatThrownBy(() -> reservationTimeService.findAvailableTimes(LocalDate.of(26,5,6), 37L))
                .isInstanceOf(DomainException.class)
                .hasMessage(ErrorCode.THEME_NOT_FOUND.message());
    }
}
