package roomescape.reservationtime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.reservation.Reservation;
import roomescape.repository.reservation.MemoryReservationRepository;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.repository.reservationtime.ReservationTimeRepository;
import roomescape.service.reservationtime.ReservationTimeService;
import roomescape.domain.theme.Theme;

class ReservationTimeServiceTest {

    @Test
    @DisplayName("예약 시간을 저장한다")
    void save() {
        ReservationTimeService reservationTimeService = new ReservationTimeService(
                new TestReservationTimeRepository(),
                new MemoryReservationRepository()
        );

        ReservationTime saved = reservationTimeService.save(LocalTime.parse("10:00"));

        assertThat(saved.getId()).isEqualTo(1L);
        assertThat(saved.getStartAt()).isEqualTo(LocalTime.parse("10:00"));
    }

    @Test
    @DisplayName("중복된 예약 시간은 저장할 수 없다")
    void saveDuplicateTime() {
        ReservationTimeService reservationTimeService = new ReservationTimeService(
                new TestReservationTimeRepository(),
                new MemoryReservationRepository()
        );
        reservationTimeService.save(LocalTime.parse("10:00"));

        assertThrows(IllegalArgumentException.class, () -> reservationTimeService.save(LocalTime.parse("10:00")));
    }

    @Test
    @DisplayName("특정 날짜와 테마에 이미 예약된 시간을 제외한 예약 가능 시간을 조회한다")
    void findAvailableTimes() {
        ReservationTimeRepository reservationTimeRepository = new TestReservationTimeRepository();
        MemoryReservationRepository reservationRepository = new MemoryReservationRepository();
        ReservationTimeService reservationTimeService = new ReservationTimeService(
                reservationTimeRepository,
                reservationRepository
        );

        Theme firstTheme = Theme.of(1L, "미술관의 밤", "추리 테마", "https://example.com/theme.png");
        Theme secondTheme = Theme.of(2L, "심해 연구소", "SF 테마", "https://example.com/theme2.png");
        ReservationTime ten = reservationTimeRepository.save(ReservationTime.createNew(LocalTime.parse("10:00")));
        ReservationTime eleven = reservationTimeRepository.save(ReservationTime.createNew(LocalTime.parse("11:00")));
        LocalDate date = LocalDate.parse("2026-08-06");

        reservationRepository.save(Reservation.createNew("쿠다", date, firstTheme, ten));
        reservationRepository.save(Reservation.createNew("포비", date, secondTheme, ten));

        List<ReservationTime> availableTimes = reservationTimeService.findAvailableTimes(date, firstTheme.getId());

        assertThat(availableTimes)
                .extracting(ReservationTime::getId)
                .containsExactly(eleven.getId());
    }

    @Test
    @DisplayName("예약이 존재하는 예약 시간은 삭제할 수 없다")
    void deleteById() {
        ReservationTimeRepository reservationTimeRepository = new TestReservationTimeRepository();
        MemoryReservationRepository reservationRepository = new MemoryReservationRepository();
        ReservationTimeService reservationTimeService = new ReservationTimeService(
                reservationTimeRepository,
                reservationRepository
        );

        ReservationTime savedTime = reservationTimeService.save(LocalTime.parse("10:00"));
        Theme theme = Theme.of(1L, "미술관의 밤", "추리 테마", "https://example.com/theme.png");
        reservationRepository.save(Reservation.createNew("쿠다", LocalDate.parse("2026-08-06"), theme, savedTime));

        assertThrows(IllegalArgumentException.class, () -> reservationTimeService.deleteById(savedTime.getId()));
    }

    @Test
    @DisplayName("ID로 예약 시간을 조회한다")
    void getById() {
        ReservationTimeService reservationTimeService = new ReservationTimeService(
                new TestReservationTimeRepository(),
                new MemoryReservationRepository()
        );
        ReservationTime saved = reservationTimeService.save(LocalTime.parse("10:00"));

        ReservationTime found = reservationTimeService.getById(saved.getId());

        assertThat(found).isEqualTo(saved);
    }

    @Test
    @DisplayName("존재하지 않는 ID로 예약 시간을 조회할 수 없다")
    void getByIdNotFound() {
        ReservationTimeService reservationTimeService = new ReservationTimeService(
                new TestReservationTimeRepository(),
                new MemoryReservationRepository()
        );

        assertThrows(IllegalArgumentException.class, () -> reservationTimeService.getById(1L));
    }

    private static class TestReservationTimeRepository implements ReservationTimeRepository {

        private final List<ReservationTime> reservationTimes = new ArrayList<>();
        private long nextId = 1L;

        @Override
        public List<ReservationTime> findAll() {
            return List.copyOf(reservationTimes);
        }

        @Override
        public Optional<ReservationTime> findById(final long timeId) {
            return reservationTimes.stream()
                    .filter(reservationTime -> reservationTime.getId() == timeId)
                    .findFirst();
        }

        @Override
        public void deleteById(final long timeId) {
            reservationTimes.removeIf(reservationTime -> reservationTime.getId() == timeId);
        }

        @Override
        public ReservationTime save(final ReservationTime reservationTime) {
            ReservationTime saved = reservationTime.withId(nextId++);
            reservationTimes.add(saved);
            return saved;
        }

        @Override
        public boolean existsByStartAt(final LocalTime startAt) {
            return reservationTimes.stream()
                    .anyMatch(reservationTime -> reservationTime.getStartAt().equals(startAt));
        }
    }
}
