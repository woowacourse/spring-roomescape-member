package roomescape.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.TimeSlot;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Sql(scripts = "/test-setup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class JdbcTimeSlotRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private JdbcTimeSlotRepository timeRepository;

    @BeforeEach
    void setUp() {
        timeRepository = new JdbcTimeSlotRepository(jdbcTemplate);
    }

    @Test
    @DisplayName("예약 시간을 저장하고 영속화된 객체를 반환한다.")
    void save() {
        TimeSlot timeSlot = TimeSlot.transientOf(LocalTime.of(10, 0));
        TimeSlot savedTimeSlot = timeRepository.save(timeSlot);
        assertThat(savedTimeSlot.id()).isPositive();
    }

    @Test
    @DisplayName("식별자로 예약 시간 객체를 조회한다.")
    void findById() {
        TimeSlot savedTimeSlot = timeRepository.save(TimeSlot.transientOf(LocalTime.of(10, 0)));
        Optional<TimeSlot> foundTimeSlot = timeRepository.findById(savedTimeSlot.id());
        assertThat(foundTimeSlot).isPresent();
        assertThat(foundTimeSlot.get().startAt()).isEqualTo(LocalTime.of(10, 0));
    }

    @Test
    @DisplayName("모든 예약 시간 객체 목록을 조회한다.")
    void findAll() {
        timeRepository.save(TimeSlot.transientOf(LocalTime.of(10, 0)));
        List<TimeSlot> timeSlots = timeRepository.findAll();
        assertThat(timeSlots).hasSize(1);
    }

    @Test
    @DisplayName("식별자로 예약 시간을 삭제한다.")
    void deleteById() {
        TimeSlot savedTimeSlot = timeRepository.save(TimeSlot.transientOf(LocalTime.of(10, 0)));
        timeRepository.deleteById(savedTimeSlot.id());
        assertThat(timeRepository.findAll()).isEmpty();
    }
}
