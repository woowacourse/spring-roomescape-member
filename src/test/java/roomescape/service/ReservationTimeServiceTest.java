package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.exception.ReservationTimeConstraintException;
import roomescape.reservationtime.exception.ReservationTimeDuplicateException;
import roomescape.reservationtime.repository.ReservationTimeRepository;
import roomescape.reservationtime.service.ReservationTimeService;
import roomescape.service.stub.FakeReservationRepository;
import roomescape.service.stub.FakeReservationTimeRepository;
import roomescape.service.stub.FakeThemeRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.exception.ThemeNotFoundException;
import roomescape.theme.repository.ThemeRepository;

class ReservationTimeServiceTest {

    private ReservationTimeService reservationTimeService;
    private ReservationRepository reservationRepository;
    private ThemeRepository themeRepository;

    @BeforeEach
    void setUp() {
        ReservationTimeRepository reservationTimeRepository = new FakeReservationTimeRepository();
        reservationRepository = new FakeReservationRepository();
        themeRepository = new FakeThemeRepository();
        reservationTimeService = new ReservationTimeService(
                reservationTimeRepository,
                reservationRepository,
                themeRepository
        );
    }

    @Test
    @DisplayName("없는 테마 예약 시간 예외")
    void save_whenThemeNotExists_throws() {
        assertThatThrownBy(() -> reservationTimeService.save(LocalTime.of(10, 0), 999L))
                .isInstanceOf(ThemeNotFoundException.class);
    }

    @Test
    @DisplayName("같은 테마 내 같은 시작 시간 중복 생성 예외")
    void save_whenDuplicateStartAtInTheme_throws() {
        Theme theme = themeRepository.save(Theme.createNew("미술관의 밤", "설명", "thumb"));

        reservationTimeService.save(LocalTime.of(10, 0), theme.getId());

        assertThatThrownBy(() -> reservationTimeService.save(LocalTime.of(10, 0), theme.getId()))
                .isInstanceOf(ReservationTimeDuplicateException.class);
    }

    @Test
    @DisplayName("예약이 있는 시간 삭제 예외")
    void delete_whenReserved_throws() {
        Theme theme = themeRepository.save(Theme.createNew("미술관의 밤", "설명", "thumb"));
        ReservationTime time = reservationTimeService.save(LocalTime.of(10, 0), theme.getId());

        reservationRepository.save(
                Reservation.createNew("쿠다", LocalDate.now().plusDays(1), time)
        );

        assertThatThrownBy(() -> reservationTimeService.deleteById(time.getId()))
                .isInstanceOf(ReservationTimeConstraintException.class);
    }

    @Test
    @DisplayName("예약 가능 시간 조회 시 이미 예약된 시간 제외")
    void findAvailableTimes_excludesReservedTimes() {
        Theme theme = themeRepository.save(Theme.createNew("미술관의 밤", "설명", "thumb"));
        ReservationTime time1 = reservationTimeService.save(LocalTime.of(10, 0), theme.getId());
        ReservationTime time2 = reservationTimeService.save(LocalTime.of(11, 0), theme.getId());

        LocalDate date = LocalDate.now().plusDays(1);
        reservationRepository.save(Reservation.createNew("쿠다", date, time1));

        List<ReservationTime> availableTimes = reservationTimeService.findAvailableTimes(date, theme.getId());

        assertThat(availableTimes).hasSize(1);
        assertThat(availableTimes.get(0).getId()).isEqualTo(time2.getId());
    }

}
