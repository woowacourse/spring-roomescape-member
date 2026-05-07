package roomescape.infrastructure;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import roomescape.entity.ReservationTime;

@JdbcTest
@Import(ReservationTimeJdbcTemplateRepository.class)
class ReservationTimeJdbcTemplateRepositoryTest {

    private final ReservationTimeJdbcTemplateRepository timeRepository;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    ReservationTimeJdbcTemplateRepositoryTest(ReservationTimeJdbcTemplateRepository timeRepository,
                                              JdbcTemplate jdbcTemplate) {
        this.timeRepository = timeRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Test
    @DisplayName("시간 저장을 잘 한다")
    @Sql(scripts = "/sql/cleanup.sql")
    void save_success() {
        // given
        ReservationTime testReservationTime = ReservationTime.createWithNullId(LocalTime.of(10, 0));

        // when
        ReservationTime result = timeRepository.save(testReservationTime);

        // then
        Assertions.assertNotNull(result.id());
    }

    @Test
    @DisplayName("id에 맞는 시간이 존재하면 id 로 잘 찾아온다.")
    @Sql(scripts = {
            "/sql/cleanup.sql",
            "/sql/infrastructure/time/find-by-id-fixtures.sql"
    })
    void findById_success() {
        // when
        Optional<ReservationTime> result = timeRepository.findById(2L);

        // then
        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(LocalTime.of(13, 0), result.get().startAt());
    }

    @Test
    @DisplayName("id에 맞는 시간이 존재하지 않으면, Optional empty를 반환한다.")
    @Sql(scripts = "/sql/cleanup.sql")
    void findById_success_but_return_empty_value() {
        // when
        Optional<ReservationTime> result = timeRepository.findById(999L);

        // then
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("저장되어 있는 모든 시간을 잘 가져온다")
    @Sql(scripts = {
            "/sql/cleanup.sql",
            "/sql/infrastructure/time/find-all-fixtures.sql"
    })
    void findAll_success() {
        // when
        List<ReservationTime> result = timeRepository.findAll();

        // then
        Assertions.assertEquals(3, result.size());
    }

    @Test
    @DisplayName("삭제를 id 기반으로 잘 한다")
    @Sql(scripts = {
            "/sql/cleanup.sql",
            "/sql/infrastructure/time/delete-fixtures.sql"
    })
    void deleteById_success() {
        // when
        timeRepository.deleteById(1L);

        // then
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM reservation_time WHERE id = 1", Integer.class);
        Assertions.assertEquals(0, count);
    }
}
