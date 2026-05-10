package roomescape.reservation.infra;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservation.domain.Schedule;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Transactional
@ActiveProfiles("test")
@Import(JdbcScheduleRepository.class)
class JdbcScheduleRepositoryTest {

    @Autowired
    private JdbcScheduleRepository repository;

    @Test
    void 스케줄_저장_레포지토리_테스트() {
        Schedule schedule = new Schedule(null, LocalDate.of(2026, 5, 7), 1L, 2L);

        Schedule savedSchedule = repository.save(schedule);

        assertThat(savedSchedule.getId()).isNotNull();
        assertThat(savedSchedule.getDate()).isEqualTo(LocalDate.of(2026, 5, 7));
        assertThat(savedSchedule.getTimeId()).isEqualTo(1L);
        assertThat(savedSchedule.getThemeId()).isEqualTo(2L);
    }

    @Test
    void 스케줄_조회_레포지토리_테스트() {
        Optional<Schedule> schedule = repository.findById(1L);

        assertThat(schedule).isPresent();
        assertThat(schedule.get().getDate()).isEqualTo(LocalDate.of(2026, 5, 5));
        assertThat(schedule.get().getTimeId()).isEqualTo(1L);
        assertThat(schedule.get().getThemeId()).isEqualTo(1L);
    }

    @Test
    void 스케줄_전체_조회_레포지토리_테스트() {
        List<Schedule> schedules = repository.findAll();

        assertThat(schedules).hasSize(5);
    }

    @Test
    void 스케줄_삭제_레포지토리_테스트() {
        Schedule savedSchedule = repository.save(new Schedule(null, LocalDate.of(2026, 5, 8), 3L, 4L));

        int deletedRows = repository.deleteById(savedSchedule.getId());

        assertThat(deletedRows).isEqualTo(1);
        assertThat(repository.findById(savedSchedule.getId())).isEmpty();
    }
}
