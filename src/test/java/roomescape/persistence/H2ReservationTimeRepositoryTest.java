package roomescape.persistence;

import java.time.LocalTime;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.business.ReservationTime;

@JdbcTest
@Import(H2ReservationTimeRepository.class)
class H2ReservationTimeRepositoryTest {

    @Autowired
    private H2ReservationTimeRepository h2ReservationTimeRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @DisplayName("모든 시간을 조회한다")
    @Test
    void findAll() {
        // given
        jdbcTemplate.update("insert into reservation_time (start_at) values (?)", LocalTime.now());
        jdbcTemplate.update("insert into reservation_time (start_at) values (?)", LocalTime.now().plusMinutes(1));

        // when
        List<ReservationTime> reservationTimes = h2ReservationTimeRepository.findAll();

        // then
        Assertions.assertThat(reservationTimes).hasSize(2);
    }

    @DisplayName("시간 아이디로 하나의 시간을 조회한다")
    @Test
    void findById() {
        // given
        LocalTime now = LocalTime.now();
        Long timeId = h2ReservationTimeRepository.add(new ReservationTime(now));

        // when
        ReservationTime reservationTime = h2ReservationTimeRepository.findById(timeId);

        // then
        Assertions.assertThat(reservationTime.getId()).isEqualTo(timeId);
        Assertions.assertThat(reservationTime.getStartAt()).isEqualTo(now);
    }

    @DisplayName("시간을 추가한다")
    @Test
    void add() {
        // given
        ReservationTime reservationTime = new ReservationTime(LocalTime.now());

        // when
        Long timeId = h2ReservationTimeRepository.add(reservationTime);

        // then
        Assertions.assertThat(timeId).isEqualTo(h2ReservationTimeRepository.findById(timeId).getId());
    }

    @DisplayName("시간을 삭제한다")
    @Test
    void delete() {
        // given
        jdbcTemplate.update("insert into reservation_time (start_at) values (?)", LocalTime.now());

        // when
        h2ReservationTimeRepository.delete(1L);

        // then
        Assertions.assertThat(h2ReservationTimeRepository.findAll()).isEmpty();
    }
}