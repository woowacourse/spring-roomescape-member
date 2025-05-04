package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import roomescape.domain.ReservationTime;
import roomescape.repository.dao.ReservationTimeH2Dao;

@JdbcTest
@ActiveProfiles("test")
@Import({ReservationTimeDbRepository.class, ReservationTimeH2Dao.class})
class ReservationTimeDbRepositoryTest {

    @Autowired
    private ReservationTimeDbRepository reservationTimeDbRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("""
                SET REFERENTIAL_INTEGRITY FALSE;
                TRUNCATE TABLE reservation;
                ALTER TABLE reservation ALTER COLUMN id RESTART WITH 1;
                TRUNCATE TABLE reservation_time;
                ALTER TABLE reservation_time ALTER COLUMN id RESTART WITH 1;
                TRUNCATE TABLE theme;
                ALTER TABLE theme ALTER COLUMN id RESTART WITH 1;
                SET REFERENTIAL_INTEGRITY TRUE;
                """);
    }

    @DisplayName("존재하지 않는 id를 조회하면 예외를 발생시킨다")
    @Test
    void getByIdException() {
        // given
        jdbcTemplate.update("INSERT INTO reservation_time(start_at) VALUES (?)", "10:00");

        // when & then
        assertThatThrownBy(() -> reservationTimeDbRepository.getById(2L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("존재하는 id를 조회하면 조회된 예약 객체를 반환한다")
    @Test
    void getById() {
        // given
        LocalTime startAt = LocalTime.parse("10:00");
        jdbcTemplate.update("INSERT INTO reservation_time(start_at) VALUES (?)", startAt);

        // when
        ReservationTime reservationTime = reservationTimeDbRepository.getById(1L);

        // then
        assertThat(reservationTime.startAt()).isEqualTo(startAt);
    }
}
