package roomescape.reservationtime.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import roomescape.fixture.Fixture;
import roomescape.reservationtime.model.ReservationTime;

@ActiveProfiles("test")
@SpringBootTest
@Sql(scripts = {"/delete-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
statements = {
        "INSERT INTO reservation_time (start_at) VALUES ('10:00')",
        "INSERT INTO reservation_time (start_at) VALUES ('12:00')",
})
class JdbcReservationTimeRepositoryTest {

    @Autowired
    private JdbcReservationTimeRepository reservationTimeRepository;

    @Test
    @DisplayName("ReservationTime 저장한 후 저장한 row의 id값을 반환한다.")
    void save() {
        ReservationTime newReservationTime = new ReservationTime(null, LocalTime.of(14, 00));

        assertThat(reservationTimeRepository.save(newReservationTime)).isEqualTo(new ReservationTime(3L, LocalTime.of(10, 00)));
    }

    @Test
    @DisplayName("ReservationTime 테이블의 있는 모든 데이터를 조회한다.")
    void findAll() {
        assertThat(reservationTimeRepository.findAll())
                .containsExactly(
                        Fixture.RESERVATION_TIME_1,
                        Fixture.RESERVATION_TIME_2);
    }

    @Test
    @DisplayName("ReservationTime 테이블의 주어진 id와 동일한 데이터를 조회한다.")
    void findById() {
        assertThat(reservationTimeRepository.findById(1L))
                .isEqualTo(Optional.of(Fixture.RESERVATION_TIME_1));
    }

    @Test
    @DisplayName("ReservationTime 테이블에 주어진 id와 없는 경우 빈 옵셔널을 반환한다.")
    void findById_Return_EmptyOptional() {
        assertThat(reservationTimeRepository.findById(99999L))
                .isEqualTo(Optional.empty());
    }

    @Test
    @DisplayName("ReservationTime 테이블에 주어진 id와 동일한 데이터를 삭제한다.")
    void deleteById() {
        reservationTimeRepository.deleteById(1L);

        assertThat(reservationTimeRepository.findById(1L))
                .isNotPresent();
    }
}
