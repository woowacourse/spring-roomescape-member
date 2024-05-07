package roomescape.reservation.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.fixture.Fixture;
import roomescape.reservation.fake.FakeReservationRepository;
import roomescape.reservation.model.Reservation;

class FakeReservationRepositoryTest {

    private static FakeReservationRepository fakeReservationRepository;

    @BeforeAll
    static void beforeAll() {
        fakeReservationRepository = new FakeReservationRepository();
    }

    @BeforeEach
    void setUp() {
        fakeReservationRepository.clear();
    }


    @Test
    @DisplayName("Reservation를 저장한 후 그 값을 반환한다.")
    void save() {
        Reservation newReservation = new Reservation(
                null,
                "포비",
                LocalDate.of(2224, 4, 23),
                Fixture.RESERVATION_TIME_1,
                Fixture.THEME_1);

        assertThat(fakeReservationRepository.save(newReservation))
                .isEqualTo(new Reservation(
                        1L,
                        "포비",
                        LocalDate.of(2224, 4, 23),
                        Fixture.RESERVATION_TIME_1,
                        Fixture.THEME_1));
    }

    @Test
    @DisplayName("Reservation 테이블의 있는 모든 데이터를 조회한다.")
    void findAll() {
        fakeReservationRepository.save(Fixture.RESERVATION_1);
        fakeReservationRepository.save(Fixture.RESERVATION_2);

        assertThat(fakeReservationRepository.findAll())
                .containsExactly(
                        Fixture.RESERVATION_1,
                        Fixture.RESERVATION_2);
    }

    @Test
    @DisplayName("Reservation 테이블의 주어진 id와 동일한 데이터를 조회한다.")
    void findById() {
        fakeReservationRepository.save(Fixture.RESERVATION_1);

        assertThat(fakeReservationRepository.findById(1L))
                .isEqualTo(Optional.of(Fixture.RESERVATION_1));
    }

    @Test
    @DisplayName("Reservation 테이블에 주어진 id와 없는 경우 빈 옵셔널을 반환한다.")
    void findById_Return_EmptyOptional() {
        assertThat(fakeReservationRepository.findById(20L))
                .isNotPresent();
    }

    @Test
    @DisplayName("Reservation 테이블의 주어진 time_id와 동일한 데이터를 조회한다.")
    void findAllByTimeId() {
        fakeReservationRepository.save(Fixture.RESERVATION_1); // RESERVATION_TIME_1
        fakeReservationRepository.save(Fixture.RESERVATION_2); // RESERVATION_TIME_1
        fakeReservationRepository.save(Fixture.RESERVATION_3); // RESERVATION_TIME_1

        assertThat(fakeReservationRepository.findAllByTimeId(1L))
                .containsExactly(
                        Fixture.RESERVATION_1,
                        Fixture.RESERVATION_2,
                        Fixture.RESERVATION_3);
    }

    @Test
    @DisplayName("Reservation 테이블에 주어진 time_id 가 없는 경우 빈 리스트를 반환한다.")
    void findAllByTimeId_Return_EmptyCollection() {
        assertThat(fakeReservationRepository.findAllByTimeId(99999L)).isEmpty();
    }

    @Test
    @DisplayName("주어진 theme_id와 동일한 예약들을 조회한다.")
    void findAllByThemeId() {
        fakeReservationRepository.save(Fixture.RESERVATION_1); // theme_id: 1
        fakeReservationRepository.save(Fixture.RESERVATION_2); // theme_id: 2

        assertThat(fakeReservationRepository.findAllByThemeId(1L))
                .containsExactly(Fixture.RESERVATION_1);
    }

    @Test
    @DisplayName("Reservation 테이블에 주어진 theme_id 가 없는 경우 빈 리스트를 반환한다.")
    void findAllByThemeId_Return_EmptyCollection() {
        assertThat(fakeReservationRepository.findAllByThemeId(99999L))
                .isEmpty();
    }

    @Test
    @DisplayName("주어진 date, theme_id와 동일한 예약들을 조회한다.")
    void findAllByDateAndThemeId() {
        fakeReservationRepository.save(Fixture.RESERVATION_1);
        LocalDate date = Fixture.RESERVATION_1.getDate();
        Long themeId = Fixture.RESERVATION_1.getTheme().getId();

        assertThat(fakeReservationRepository.findAllByDateAndThemeId(date, themeId))
                .containsExactly(Fixture.RESERVATION_1);
    }

    @Test
    @DisplayName("주어진 date, theme_id 이 일치하는 예약이 없는 경우 빈 리스트를 반환한다.")
    void findAllByDateAndThemeId_Return_EmptyCollection() {
        assertThat(fakeReservationRepository.findAllByDateAndThemeId(LocalDate.of(1, 1, 1), 99999L))
                .isEmpty();
    }

    @Test
    @DisplayName("날짜와 시간 컬럼의 값이 동일할 경우 참을 반환한다.")
    void existsByDateAndTime_whenSameName() {
        fakeReservationRepository.save(Fixture.RESERVATION_1);
        LocalDate date = Fixture.RESERVATION_1.getDate();
        Long timeId = Fixture.RESERVATION_1.getReservationTime().getId();
        Long themeId = Fixture.RESERVATION_1.getTheme().getId();

        assertTrue(fakeReservationRepository.existsByDateAndTimeAndTheme(date, timeId, themeId));
    }

    @Test
    @DisplayName("날짜 또는 시간 중 하나라도 다를 경우 거짓을 반환한다.")
    void existsByDateAndTime_isFalse() {
        assertFalse(fakeReservationRepository.existsByDateAndTimeAndTheme(
                Fixture.RESERVATION_1.getDate(),
                1L,
                3L));
    }

    @Test
    @DisplayName("Reservation 테이블에 주어진 id와 동일한 데이터를 삭제한다.")
    void deleteById() {
        fakeReservationRepository.deleteById(2L);
        assertThat(fakeReservationRepository.findById(2L))
                .isNotPresent();
    }

}
