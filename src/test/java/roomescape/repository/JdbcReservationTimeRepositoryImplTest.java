package roomescape.repository;


import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import roomescape.domain.ReservationTime;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@Sql(scripts = "/truncate.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
class JdbcReservationTimeRepositoryImplTest {

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    private final LocalTime startAt1 = LocalTime.parse("10:00");
    private final ReservationTime reservationTime1 = new ReservationTime(startAt1);

    private final LocalTime startAt2 = LocalTime.parse("11:00");
    private final ReservationTime reservationTime2 = new ReservationTime(startAt2);

    @DisplayName("예약 시간 정보를 DB에 저장한다.")
    @Test
    void save() {
        ReservationTime saved = reservationTimeRepository.save(reservationTime1);

        assertThat(saved).isEqualTo(new ReservationTime(saved.getId(), startAt1));
    }

    @DisplayName("id값을 통해 예약 시간 정보를 DB에서 조회한다.")
    @Test
    void findById() {
        ReservationTime saved = reservationTimeRepository.save(reservationTime1);

        assertThat(reservationTimeRepository.findById(saved.getId())).isEqualTo(saved);
    }

    @DisplayName("모든 예약 시간 정보를 DB에서 조회한다.")
    @Test
    void findAll() {
        ReservationTime saved1 = reservationTimeRepository.save(reservationTime1);
        ReservationTime saved2 = reservationTimeRepository.save(reservationTime2);

        assertThat(reservationTimeRepository.findAll()).containsExactly(saved1, saved2);
    }

    @DisplayName("id값을 통해 예약 시간 정보를 DB에서 삭제한다.")
    @Test
    void deleteById() {
        ReservationTime saved1 = reservationTimeRepository.save(reservationTime1);
        ReservationTime saved2 = reservationTimeRepository.save(reservationTime2);

        reservationTimeRepository.deleteById(saved1.getId());

        assertThat(reservationTimeRepository.findAll()).containsExactly(saved2);
    }
}
