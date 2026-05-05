package roomescape.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import roomescape.domain.Time;

@JdbcTest
@Import(TimeJdbcDao.class)
class TimeJdbcDaoTest {
    private static final int DELETED = 1;
    @Autowired
    private TimeDao timeDao;


    @ParameterizedTest
    @CsvSource("1,2,3")
    void findAll(int count) {
        for (int i = 0; i < count; i++) {
            createAndInsertTime();
        }
        List<Time> times = timeDao.findAll();

        assertThat(times).hasSize(count);
    }

    @Test
    void findById() {
        long none = 1;
        assertThat(timeDao.findById(none)).isNotPresent();

        Long savedId = createAndInsertTime();
        assertThat(timeDao.findById(savedId)).isPresent();
    }

    @Test
    void insert() {
        assertThat(timeDao.insert(new Time(LocalTime.of(10, 0)))).isNotNull();
    }

    @Test
    void delete() {
        Long id = createAndInsertTime();

        assertThat(timeDao.delete(id)).isEqualTo(DELETED);
    }

    private Long createAndInsertTime() {
        return timeDao.insert(new Time(LocalTime.of(10, 0)));
    }
}
