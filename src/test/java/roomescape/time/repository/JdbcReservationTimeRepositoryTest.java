package roomescape.time.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.time.entity.ReservationTimeEntity;

import java.time.LocalTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
class JdbcReservationTimeRepositoryTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private ReservationTimeRepository timeRepository;

    @BeforeEach
    void setup() {
        timeRepository = new JdbcReservationTimeRepository(jdbcTemplate);
    }

    @DisplayName("생성 테스트")
    @Test
    void createTest() {
        // given
        ReservationTimeEntity time = new ReservationTimeEntity(1L, LocalTime.of(10, 0));

        // when
        timeRepository.save(time);

        // then
        assertThat(timeRepository.findAll()).hasSize(1);
    }

    @DisplayName("삭제 테스트")
    @Test
    void deleteTest() {
        // given
        jdbcTemplate.update("INSERT INTO reservation_time (id, start_at) VALUES (?, ?)", 1, "10:00");

        // when
        timeRepository.deleteById(1L);

        // then
        assertThat(timeRepository.findAll()).hasSize(0);
    }

    @DisplayName("id로 조회 테스트")
    @Test
    void findByIdTest() {
        // given
        jdbcTemplate.update("INSERT INTO reservation_time (id, start_at) VALUES (?, ?)", 1, "10:00");

        // when
        Optional<ReservationTimeEntity> entity = timeRepository.findById(1L);

        // then
        assertThat(entity).isNotEmpty();
        assertThat(entity.get().getId()).isEqualTo(1L);
    }
}
