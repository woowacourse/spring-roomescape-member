package roomescape.reservationtime.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.fixture.Fixture;
import roomescape.reservationtime.fake.FakeReservationTimeRepository;
import roomescape.reservationtime.model.ReservationTime;

public class FakeReservationTimeRepositoryTest {

    private static FakeReservationTimeRepository fakeReservationTimeRepository;

    @BeforeAll
    static void beforeAll() {
        fakeReservationTimeRepository = new FakeReservationTimeRepository();
    }

    @BeforeEach
    void setUp() {
        fakeReservationTimeRepository.clear();
    }

    @Test
    @DisplayName("ReservationTime 저장한 후 그 값을 반환한다.")
    void save() {
        ReservationTime newReservationTime = new ReservationTime(null, LocalTime.of(12, 12));

        ReservationTime reservationTime = fakeReservationTimeRepository.save(newReservationTime);

        assertThat(reservationTime.getTime())
                .isEqualTo(LocalTime.of(12, 12));
    }

    @Test
    @DisplayName("ReservationTime 테이블의 있는 모든 데이터를 조회한다.")
    void findAll() {
        fakeReservationTimeRepository.save(Fixture.RESERVATION_TIME_1);
        fakeReservationTimeRepository.save(Fixture.RESERVATION_TIME_2);

        assertThat(fakeReservationTimeRepository.findAll())
                .containsExactly(
                        Fixture.RESERVATION_TIME_1,
                        Fixture.RESERVATION_TIME_2);
    }

    @Test
    @DisplayName("ReservationTime 테이블의 주어진 id와 동일한 데이터를 조회한다.")
    void findById() {
        fakeReservationTimeRepository.save(Fixture.RESERVATION_TIME_1); // id: 1

        assertThat(fakeReservationTimeRepository.findById(1L))
                .isEqualTo(Optional.of(Fixture.RESERVATION_TIME_1));
    }

    @Test
    @DisplayName("ReservationTime 테이블에 주어진 id와 없는 경우 빈 옵셔널을 반환한다.")
    void findById_Return_EmptyOptional() {
        assertThat(fakeReservationTimeRepository.findById(99999L))
                .isEqualTo(Optional.empty());
    }

    @Test
    @DisplayName("ReservationTime 테이블에 주어진 id와 동일한 데이터를 삭제한다.")
    void deleteById() {
        fakeReservationTimeRepository.save(Fixture.RESERVATION_TIME_1); // id: 1

        fakeReservationTimeRepository.deleteById(1L);

        assertThat(fakeReservationTimeRepository.findById(1L))
                .isNotPresent();
    }
}
