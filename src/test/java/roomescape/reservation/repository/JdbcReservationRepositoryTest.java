package roomescape.reservation.repository;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import roomescape.fixture.Fixture;
import roomescape.reservation.model.Reservation;

@ActiveProfiles("test")
@SpringBootTest
@Sql(scripts = {"/delete-data.sql", "/init-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class JdbcReservationRepositoryTest {

    @Autowired
    private JdbcReservationRepository jdbcReservationRepository;

    @Test
    @DisplayName("Reservation를 저장한 후 그 값을 반환한다.")
    void save() {
        Reservation newReservation = new Reservation(
                null,
                "포비",
                LocalDate.of(2224, 4, 23),
                Fixture.RESERVATION_TIME_1,
                Fixture.THEME_1);

        assertThat(jdbcReservationRepository.save(newReservation))
                .isEqualTo(new Reservation(
                        4L,
                        "포비",
                        LocalDate.of(2224, 4, 23),
                        Fixture.RESERVATION_TIME_1,
                        Fixture.THEME_1));
    }

    @Test
    @DisplayName("Reservation 테이블의 있는 모든 데이터를 조회한다.")
    void findAll() {
        assertThat(jdbcReservationRepository.findAll())
                .containsExactly(
                        Fixture.RESERVATION_1,
                        Fixture.RESERVATION_2,
                        Fixture.RESERVATION_3);
    }

    @Test
    @DisplayName("Reservation 테이블의 주어진 id와 동일한 데이터를 조회한다.")
    void findById() {
        assertThat(jdbcReservationRepository.findById(1L))
                .isEqualTo(Optional.of(Fixture.RESERVATION_1));
    }

    @Test
    @DisplayName("Reservation 테이블에 주어진 id와 없는 경우 빈 옵셔널을 반환한다.")
    void findById_Return_EmptyOptional() {
        assertThat(jdbcReservationRepository.findById(20L))
                .isNotPresent();
    }

    @Test
    @DisplayName("Reservation 테이블의 주어진 time_id와 동일한 데이터를 조회한다.")
    void findAllByTimeId() {
        assertThat(jdbcReservationRepository.findAllByTimeId(1L))
                .containsExactly(
                        Fixture.RESERVATION_1,
                        Fixture.RESERVATION_2,
                        Fixture.RESERVATION_3);
    }

    @Test
    @DisplayName("Reservation 테이블에 주어진 time_id와 없는 경우 빈 리스트를 반환한다.")
    void findAllByTimeId_Return_EmptyCollection() {
        assertThat(jdbcReservationRepository.findAllByTimeId(20L)).isEmpty();
    }

    @Test
    @DisplayName("Reservation 테이블의 주어진 theme_id와 동일한 데이터를 조회한다.")
    void findAllByThemeId() {
        assertThat(jdbcReservationRepository.findAllByThemeId(1L))
                .containsExactly(Fixture.RESERVATION_1);
    }

    @Test
    @DisplayName("Reservation 테이블에 주어진 theme_id와 없는 경우 빈 리스트를 반환한다.")
    void findAllByThemeId_Return_EmptyCollection() {
        assertThat(jdbcReservationRepository.findAllByThemeId(99999L))
                .isEmpty();
    }

    @Test
    @DisplayName("Reservation 테이블의 주어진 date, theme_id와 동일한 데이터를 조회한다.")
    void findAllByDateAndThemeId() {
        Reservation reservation = Fixture.RESERVATION_1;

        assertThat(jdbcReservationRepository.findAllByDateAndThemeId(reservation.getDate(), reservation.getTheme().getId()))
                .containsExactly(reservation);
    }

    @Test
    @DisplayName("Reservation 테이블에 주어진 date, theme_id와 없는 경우 빈 리스트를 반환한다.")
    void findAllByDateAndThemeId_Return_EmptyCollection() {
        assertThat(jdbcReservationRepository.findAllByDateAndThemeId(LocalDate.of(1, 1, 1), 99999L))
                .isEmpty();
    }

    @Test
    @DisplayName("날짜와 시간 컬럼의 값이 동일할 경우 참을 반환한다.")
    void existsByDateAndTime_whenSameName() {
        assertTrue(jdbcReservationRepository.existsByDateAndTimeAndTheme(
                Fixture.RESERVATION_1.getDate(),
                Fixture.RESERVATION_1.getReservationTime().getId(),
                Fixture.RESERVATION_1.getTheme().getId()));
    }

    @Test
    @DisplayName("날짜 또는 시간 중 하나라도 다를 경우 거짓을 반환한다.")
    void existsByDateAndTime_isFalse() {
        assertFalse(jdbcReservationRepository.existsByDateAndTimeAndTheme(
                Fixture.RESERVATION_1.getDate(),
                1L,
                3L));
    }

    @Test
    @DisplayName("Reservation 테이블에 주어진 id와 동일한 데이터를 삭제한다.")
    void deleteById() {
        jdbcReservationRepository.deleteById(2L);
        assertThat(jdbcReservationRepository.findById(2L))
                .isNotPresent();
    }
}
