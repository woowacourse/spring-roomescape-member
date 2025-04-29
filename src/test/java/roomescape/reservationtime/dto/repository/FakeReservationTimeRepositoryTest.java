package roomescape.reservationtime.dto.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.repository.FakeReservationTimeRepository;

class FakeReservationTimeRepositoryTest {

    private FakeReservationTimeRepository repository;

    @BeforeEach
    void setUp() {
        repository = new FakeReservationTimeRepository();
    }

    @Test
    void getAll_shouldReturnAllSavedReservationTimes() {
        ReservationTime t1 = ReservationTime.of(1L, LocalTime.of(10, 0));
        ReservationTime t2 = ReservationTime.of(1L, LocalTime.of(14, 30));

        repository.put(t1);
        repository.put(t2);

        List<ReservationTime> all = repository.getAll();
        assertThat(all).hasSize(2);
        assertThat(all).extracting(ReservationTime::getStartAt)
                .containsExactlyInAnyOrder(t1.getStartAt(), t2.getStartAt());
    }

    @Test
    void findById_shouldReturnCorrectTime() {
        ReservationTime time = ReservationTime.of(1L, LocalTime.of(11, 0));
        repository.put(time);

        Optional<ReservationTime> found = repository.findById(1L);
        assertThat(found).isPresent();
        assertThat(found.get().getStartAt()).isEqualTo(LocalTime.of(11, 0));
    }

    @Test
    void deleteById_shouldRemoveEntry() {
        ReservationTime time = ReservationTime.of(1L, LocalTime.of(12, 0));
        repository.put(time);

        repository.deleteById(1L);

        Optional<ReservationTime> found = repository.findById(1L);
        assertThat(found).isNotPresent();
    }

    @Test
    void checkExistsByStartAt_shouldReturnTrueIfExists() {
        LocalTime time = LocalTime.of(13, 0);
        repository.put(ReservationTime.of(1L, time));

        boolean exists = repository.checkExistsByStartAt(time);
        assertThat(exists).isTrue();
    }

    @Test
    void checkExistsByStartAt_shouldReturnFalseIfNotExists() {
        boolean exists = repository.checkExistsByStartAt(LocalTime.of(15, 0));
        assertThat(exists).isFalse();
    }
}