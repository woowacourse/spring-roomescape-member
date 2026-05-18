package roomescape.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import roomescape.dao.jdbc.TimeJdbcDao;
import roomescape.domain.Time;

@JdbcTest
@Import(TimeJdbcDao.class)
@ActiveProfiles("test")
class TimeJdbcDaoTest {

    @Autowired
    private TimeDao timeDao;

    @Test
    @DisplayName("시간을 수정하면 변경된 값이 저장된다")
    void update() {
        Time saved = timeDao.insert(new Time(LocalTime.of(10, 0)));
        Time toUpdate = new Time(saved.getId(), LocalTime.of(11, 0));

        timeDao.update(toUpdate);

        Time updated = timeDao.findById(saved.getId()).orElseThrow();
        assertThat(updated.getStartAt()).isEqualTo(LocalTime.of(11, 0));
    }
}
