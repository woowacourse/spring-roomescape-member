package roomescape.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import roomescape.dao.row.TimeRow;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Import(TimeJdbcDao.class)
@ActiveProfiles("test")
class TimeJdbcDaoTest {
    private static final int DELETED = 1;
    @Autowired
    private TimeDao timeDao;

    private TimeRow time1;
    private TimeRow time2;

    @BeforeEach
    void setUp() {
        time1 = new TimeRow(LocalTime.of(11, 0));
        time2 = new TimeRow(LocalTime.of(11, 24));
    }

    @Test
    void findById() {
        TimeRow saved = createTimeHandler(time1);
        assertThat(timeDao.findById(saved.id()))
                .isPresent()
                .get().isEqualTo(saved);
    }

    @Test
    void findAll() {
        List<TimeRow> saved = createTimesHandler(time1, time2);
        List<TimeRow> find = timeDao.findAll();

        assertThat(find).hasSize(saved.size())
                .containsAll(saved);
    }

    @Test
    void create() {
        assertThat(timeDao.create(time1)).isNotNull();
    }

    @Test
    void delete() {
        TimeRow saved = createTimeHandler(time1);
        assertThat(timeDao.delete(saved.id())).isEqualTo(DELETED);
    }

    @Test
    void existsByStartAt() {
        TimeRow saved = createTimeHandler(time1);
        TimeRow notExists = time2;

        assertThat(timeDao.existsByStartAt(saved.startAt())).isTrue();
        assertThat(timeDao.existsByStartAt(notExists.startAt())).isFalse();
    }

    private TimeRow createTimeHandler(TimeRow time) {
        return timeDao.create(time);
    }

    private List<TimeRow> createTimesHandler(TimeRow... times) {
        return Arrays.stream(times)
                .map(this::createTimeHandler)
                .toList();
    }
}
