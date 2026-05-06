package roomescape.time.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import roomescape.time.domain.ReservationTime;

@JdbcTest
class ReservationTimeRepositoryTest {
    private JdbcReservationTimeRepository jdbcReservationTimeRepository;
    private Long timeId;

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() {
        jdbcReservationTimeRepository = new JdbcReservationTimeRepository(jdbcTemplate);

        timeId = jdbcReservationTimeRepository.save(ReservationTime.create(
                LocalTime.of(15, 40)));
        jdbcReservationTimeRepository.save(ReservationTime.create(
                LocalTime.of(16, 0)));
    }

    @Test
    @DisplayName("예약 시간을 추가한다.")
    void save() {
        //given & when
        jdbcReservationTimeRepository.save(
                ReservationTime.create(LocalTime.of(12, 0)));

        //then
        assertThat(jdbcReservationTimeRepository.findAll().size()).isEqualTo(3);
    }

    @Test
    @DisplayName("모든 예약 시간 정보를 조회한다.")
    void findAll() {
        assertThat(jdbcReservationTimeRepository.findAll().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("예약을 삭제한다.")
    void delete() {
        //given & when
        jdbcReservationTimeRepository.delete(timeId);

        //then
        assertThat(jdbcReservationTimeRepository.findAll().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("예약 시작 시간 값으로 예약 시간이 존재하는지 확인한다.")
    void existsByStartAt() {
        assertThat(jdbcReservationTimeRepository.existsByStartAt(LocalTime.of(15, 40))).isTrue();
        assertThat(jdbcReservationTimeRepository.existsByStartAt(LocalTime.of(12, 0))).isFalse();
    }
}
