package roomescape.persistence.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.business.domain.PlayTime;
import roomescape.persistence.entity.PlayTimeEntity;

@JdbcTest
class JdbcPlayTimeDaoTest {

    private PlayTimeDao playTimeDao;

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    JdbcPlayTimeDaoTest(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @BeforeEach
    void setUp() {
        playTimeDao = new JdbcPlayTimeDao(jdbcTemplate);
        jdbcTemplate.execute("DROP TABLE IF EXISTS reservation_time CASCADE");
        jdbcTemplate.execute("""
                CREATE TABLE reservation_time
                (
                    id SERIAL,
                    start_at VARCHAR(255) NOT NULL,
                    PRIMARY KEY (id)
                );
                """);
    }

    @DisplayName("데이터베이스에 방탈출 시간을 저장한다.")
    @Test
    void save() {
        // given & when
        final Long id = playTimeDao.save(new PlayTime(LocalTime.of(10, 10)));
        final PlayTimeEntity actual = jdbcTemplate.queryForObject(
                "SELECT id, start_at FROM reservation_time WHERE id = ?",
                PlayTimeEntity.getDefaultRowMapper(), id
        );

        // then
        assertThat(actual).isEqualTo(new PlayTimeEntity(1L, "10:10"));
    }

    @DisplayName("데이터베이스에서 방탈출 시간을 찾는다.")
    @Test
    void find() {
        // given
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES ('10:10')");

        // when
        final Optional<PlayTime> actual = playTimeDao.find(1L);

        // then
        assertThat(actual).isPresent();
        assertThat(actual.get()).isEqualTo(PlayTime.createWithId(1L, LocalTime.of(10, 10)));
    }

    @DisplayName("해당하는 방탈출 시간이 없다면 Optional Empty를 반환한다.")
    @Test
    void findNotExistsTime() {
        // given & when
        final Optional<PlayTime> actual = playTimeDao.find(1L);

        // then
        assertThat(actual).isEmpty();
    }

    @DisplayName("데이터베이스에서 모든 방탈출 시간을 찾는다.")
    @Test
    void findAll() {
        // given
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES ('10:10')");
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES ('11:10')");

        // when
        final List<PlayTime> actual = playTimeDao.findAll();

        // then
        assertThat(actual).containsExactly(
                PlayTime.createWithId(1L, LocalTime.of(10, 10)),
                PlayTime.createWithId(2L, LocalTime.of(11, 10))
        );
    }

    @DisplayName("데이터베이스에서 방탈출 시간을 삭제한다.")
    @Test
    void remove() {
        // given
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES ('10:10')");

        // when
        final boolean flag = playTimeDao.remove(1L);
        final List<PlayTime> playTimes = playTimeDao.findAll();

        // then
        assertAll(
                () -> assertThat(flag).isTrue(),
                () -> assertThat(playTimes).isEmpty()
        );
    }

    @DisplayName("해당하는 방탈출 시간이 없다면 0을 반환한다.")
    @Test
    void removeNotExistsTime() {
        // given & when
        final boolean flag = playTimeDao.remove(1L);

        // then
        assertThat(flag).isFalse();
    }
}
