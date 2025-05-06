package roomescape.time.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static roomescape.testFixture.Fixture.RESERVATION_1;
import static roomescape.testFixture.Fixture.RESERVATION_TIME_1;

import java.sql.Time;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import roomescape.testFixture.JdbcHelper;
import roomescape.time.domain.ReservationTime;

@JdbcTest
@Import(JdbcTimeRepository.class)
@ActiveProfiles("test")
class JdbcTimeRepositoryTest {

    @Autowired
    private JdbcTimeRepository timeRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void cleanDatabase() {
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
        jdbcTemplate.execute("TRUNCATE TABLE reservation");
        jdbcTemplate.execute("ALTER TABLE reservation ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("TRUNCATE TABLE reservation_time");
        jdbcTemplate.execute("ALTER TABLE reservation_time ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("TRUNCATE TABLE theme");
        jdbcTemplate.execute("ALTER TABLE theme ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("TRUNCATE TABLE members");
        jdbcTemplate.execute("ALTER TABLE members ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
    }

    @DisplayName("save 후 생성된 id를 반환한다.")
    @Test
    void saveTest() {
        // given
        LocalTime startAt = LocalTime.of(10, 0);
        ReservationTime reservationTime = ReservationTime.of(1L, startAt);

        // when
        Long savedId = timeRepository.save(reservationTime);

        // then
        String sql = "SELECT * FROM reservation_time WHERE id = ?";
        Map<String, Object> result = jdbcTemplate.queryForMap(sql, savedId);

        assertAll(
                () -> assertThat(savedId).isNotNull(),
                () -> assertThat(result.get("start_at")).isEqualTo(Time.valueOf(startAt))
        );
    }

    @DisplayName("모든 시간을 조회할 수 있다.")
    @Test
    void findAllTest() {
        // given
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES ('10:00:00')");
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES ('11:00:00')");
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES ('12:00:00')");

        // when
        List<ReservationTime> times = timeRepository.findAll();

        // then
        assertThat(times).hasSize(3);
        assertThat(times)
                .extracting(ReservationTime::getStartAt)
                .containsExactly(
                        LocalTime.of(10, 0),
                        LocalTime.of(11, 0),
                        LocalTime.of(12, 0)
                );
    }

    @DisplayName("id로 예약을 삭제할 수 있다.")
    @Test
    void deleteByIdTest() {
        // given
        JdbcHelper.insertReservationTime(jdbcTemplate, RESERVATION_TIME_1);
        assertThat(getTimeCount()).isEqualTo(1);

        // when
        timeRepository.deleteById(RESERVATION_TIME_1.getId());

        // then
        assertThat(getTimeCount()).isEqualTo(0);
    }

    @DisplayName("다른 테이블에서 참조되고 있는 시간 삭제 시 예외 발생")
    @Test
    void error_when_delete_referencedTime() {
        // given
        JdbcHelper.prepareAndInsertReservation(jdbcTemplate, RESERVATION_1);

        // when & then
        assertThatThrownBy(() -> timeRepository.deleteById(RESERVATION_TIME_1.getId()))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("존재하지 않는 ID로 시간 삭제 시도시 예외가 발생하지 않는다.")
    @Test
    void deleteNonExistingTheme_notThrowException() {
        // given
        Long notExistingId = 999L;

        // when & then
        assertDoesNotThrow(() -> timeRepository.deleteById(notExistingId));
    }

    @DisplayName("ID로 시간을 조회할 수 있다")
    @Test
    void findById() {
        //given
        JdbcHelper.insertReservationTime(jdbcTemplate, RESERVATION_TIME_1);
        //when
        Long id = RESERVATION_1.getId();
        Optional<ReservationTime> result = timeRepository.findById(id);

        //then
        assertThat(result).isPresent();
        ReservationTime resultTime = result.get();
        assertAll(
                () -> assertThat(resultTime.getId()).isEqualTo(id),
                () -> assertThat(resultTime.getStartAt()).isEqualTo(RESERVATION_TIME_1.getStartAt())
        );
    }


    @DisplayName("ID로 조회했을 때 존재하지 않으면 빈 Optional을 반환한다.")
    @Test
    void findMemberById_emptyOptional() {
        // given
        Long nonExistingId = 999L;

        // when
        Optional<ReservationTime> result = timeRepository.findById(nonExistingId);

        // then
        assertThat(result).isEmpty();
    }

    private int getTimeCount() {
        return jdbcTemplate.queryForObject("SELECT COUNT (*) FROM reservation_time", Integer.class);
    }
}
