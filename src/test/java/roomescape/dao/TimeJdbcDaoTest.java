package roomescape.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import roomescape.domain.Time;

@JdbcTest
@Import(TimeJdbcDao.class)
@ActiveProfiles("test")
class TimeJdbcDaoTest {
    private static final int DELETED = 1;
    @Autowired
    private TimeDao timeDao;

    private Time time1;
    private Time time2;

    @BeforeEach
    void setUp() {
        time1 = new Time(LocalTime.of(11, 0));
        time2 = new Time(LocalTime.of(11, 24));
    }

    @Test
    void findById() {
        Time saved = insertTimeHandler(time1);
        assertThat(timeDao.findById(saved.getId()))
                .isPresent()
                .get().isEqualTo(saved);
    }

    @Test
    void findAll() {
        List<Time> saved = insertTimesHandler(time1, time2);
        List<Time> find = timeDao.findAll();

        assertThat(find).hasSize(saved.size())
                .containsAll(saved);
    }

    @Test
    void insert() {
        assertThat(timeDao.insert(time1)).isNotNull();
    }

    @Test
    void delete() {
        Time saved = insertTimeHandler(time1);
        assertThat(timeDao.delete(saved.getId())).isEqualTo(DELETED);
    }

    private Time insertTimeHandler(Time time) {
        return timeDao.insert(time);
    }

    private List<Time> insertTimesHandler(Time... times) {
        return Arrays.stream(times)
                .map(this::insertTimeHandler)
                .toList();
    }
}
