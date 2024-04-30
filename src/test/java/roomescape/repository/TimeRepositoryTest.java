package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import roomescape.domain.time.Time;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class TimeRepositoryTest {

    @Autowired
    private TimeRepository timeRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Time time = new Time(1L, LocalTime.of(17, 30));

    @Test
    @DisplayName("등록된 시간의 id를 통해 단건 조회할 수 있다.")
    void findTimeById() {
        timeRepository.save(time);

        Time foundTime = timeRepository.findById(1L);

        assertThat(foundTime.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("repository를 통해 조회한 시간 수는 DB를 통해 조회한 시간 수와 같다.")
    void readDbTimes() {
        timeRepository.save(time);

        List<Time> times = timeRepository.findAll();
        Integer count = jdbcTemplate.queryForObject("SELECT count(*) from reservation_time", Integer.class);

        assertThat(times.size()).isEqualTo(count);
    }

    @Test
    @DisplayName("하나의 시간만 등록한 경우, DB를 조회 했을 때 조회 결과 개수는 1개이다.")
    void postTimeIntoDb() {
        timeRepository.save(time);

        List<Time> times = timeRepository.findAll();

        assertThat(times.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("하나의 시간만 등록한 경우, 시간 삭제 뒤 DB를 조회 했을 때 조회 결과 개수는 0개이다.")
    void readTimesSizeFromDbAfterPostAndDelete() {
        timeRepository.save(time);

        timeRepository.delete(1L);
        List<Time> times = timeRepository.findAll();

        assertThat(times.size()).isEqualTo(0);
    }
}
