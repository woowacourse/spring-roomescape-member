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
import roomescape.reservationtime.service.dto.ReservationTimeResult;
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
    private ReservationTimeRepository reservationTimeRepository;

    @BeforeEach
    void setUp() {
        reservationRepository = new FakeReservationRepository();
        reservationTimeRepository = new FakeReservationTimeRepository(reservationRepository);
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
        // given
        Theme theme = themeRepository.save(Theme.createNew("미술관의 밤", "설명", "thumb"));

        reservationTimeRepository.save(ReservationTime.createNew(LocalTime.of(10, 0), theme));

        // when & then
        assertThatThrownBy(() -> reservationTimeService.save(LocalTime.of(10, 0), theme.getId()))
                .isInstanceOf(ReservationTimeDuplicateException.class);
    }

    @Test
    @DisplayName("예약이 있는 시간 삭제 예외")
    void delete_whenReserved_throws() {
        // given
        Theme theme = themeRepository.save(Theme.createNew("미술관의 밤", "설명", "thumb"));
        ReservationTime time = reservationTimeRepository.save(ReservationTime.createNew(LocalTime.of(10, 0), theme));

        reservationRepository.save(
                Reservation.createNew("쿠다", LocalDate.now().plusDays(1), time)
        );

        // when & then
        assertThatThrownBy(() -> reservationTimeService.deleteById(time.getId()))
                .isInstanceOf(ReservationTimeConstraintException.class);
    }

    @Test
    @DisplayName("예약 가능 시간 조회 시 이미 예약된 시간 제외")
    void findAvailableTimes_excludesReservedTimes() {
        // given
        Theme theme = themeRepository.save(Theme.createNew("미술관의 밤", "설명", "thumb"));

        ReservationTime time1 = reservationTimeRepository.save(ReservationTime.createNew(LocalTime.of(10, 0), theme));
        ReservationTime time2 = reservationTimeRepository.save(ReservationTime.createNew(LocalTime.of(11, 0), theme));

        LocalDate date = LocalDate.now().plusDays(1);
        reservationRepository.save(Reservation.createNew("쿠다", date, time1));

        // when
        List<ReservationTimeResult> availableTimes = reservationTimeService.findAvailableTimes(date, theme.getId());

        // then
        assertThat(availableTimes).hasSize(1);
        assertThat(availableTimes.getFirst().id()).isEqualTo(time2.getId());
    }

}
