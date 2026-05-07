package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.TimeSlot;

@JdbcTest
class TimeSlotDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private TimeSlotDao timeDao;

    @BeforeEach
    void setUp() {
        timeDao = new TimeSlotDao(jdbcTemplate);
        executeSchema();
    }

    private void executeSchema() {
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
        jdbcTemplate.execute(
                "CREATE TABLE IF NOT EXISTS time_slot (id BIGINT AUTO_INCREMENT PRIMARY KEY, start_at TIME)");
        jdbcTemplate.execute("TRUNCATE TABLE time_slot");
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");
    }

    @Test
    @DisplayName("예약 시간을 저장하고 영속화된 객체를 반환한다.")
    void save() {
        TimeSlot timeSlot = TimeSlot.transientOf(LocalTime.of(10, 0));
        TimeSlot savedTimeSlot = timeDao.save(timeSlot);
        assertThat(savedTimeSlot.id()).isPositive();
    }

    @Test
    @DisplayName("식별자로 예약 시간 객체를 조회한다.")
    void findById() {
        TimeSlot savedTimeSlot = timeDao.save(TimeSlot.transientOf(LocalTime.of(10, 0)));
        TimeSlot foundTimeSlot = timeDao.findById(savedTimeSlot.id());
        assertThat(foundTimeSlot.startAt()).isEqualTo(LocalTime.of(10, 0));
    }

    @Test
    @DisplayName("모든 예약 시간 객체 목록을 조회한다.")
    void findAll() {
        timeDao.save(TimeSlot.transientOf(LocalTime.of(10, 0)));
        List<TimeSlot> timeSlots = timeDao.findAll();
        assertThat(timeSlots).hasSize(1);
    }

    @Test
    @DisplayName("식별자로 예약 시간을 삭제한다.")
    void deleteById() {
        TimeSlot savedTimeSlot = timeDao.save(TimeSlot.transientOf(LocalTime.of(10, 0)));
        timeDao.deleteById(savedTimeSlot.id());
        assertThat(timeDao.findAll()).isEmpty();
    }
}
