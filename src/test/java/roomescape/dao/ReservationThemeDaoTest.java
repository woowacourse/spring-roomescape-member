package roomescape.dao;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestConstructor;
import roomescape.domain.ReservationTheme;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@JdbcTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public class ReservationThemeDaoTest {

    private final JdbcTemplate jdbcTemplate;
    private final ReservationThemeDao reservationThemeDao;

    public ReservationThemeDaoTest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.reservationThemeDao = new ReservationThemeDao(jdbcTemplate);
    }

    @Test
    void findAllTest() {
        List<ReservationTheme> reservationThemes = reservationThemeDao.findAll();

        assertThat(reservationThemes.size()).isEqualTo(1);
    }

    @Test
    void findByIdTest() {
        ReservationTheme reservationTheme = reservationThemeDao.findById(1L).get();

        assertThat(reservationTheme.getId()).isEqualTo(1L);
    }

    @Test
    void findByWrongIdTest() {
        Optional<ReservationTheme> reservationTheme = reservationThemeDao.findById(9L);

        assertThat(reservationTheme).isEqualTo(Optional.empty());
    }

    @Test
    void insertTest() {
        Long index = jdbcTemplate.queryForObject("SELECT count(*) FROM theme", Long.class);
        Long id = reservationThemeDao.insert("test", "testDesc", "testThumbnail");

        assertThat(id).isEqualTo(index + 1);
    }

    @Test
    void wrongInsertTest() {
        assertThatThrownBy(() -> reservationThemeDao.insert(null, "testDesc", "testThumbnail"))
                .isInstanceOf(DataIntegrityViolationException.class);
        assertThatThrownBy(() -> reservationThemeDao.insert("test", null, "testThumbnail"))
                .isInstanceOf(DataIntegrityViolationException.class);
        assertThatThrownBy(() -> reservationThemeDao.insert("test", "testDesc", null))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void deleteByIdTest() {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO theme(name, description, thumbnail) VALUES (?, ?, ?)",
                    new String[]{"id"});
            ps.setString(1, "test");
            ps.setString(2, "testDesc");
            ps.setString(3, "testThumbnail");
            return ps;
        }, keyHolder);

        Long key = keyHolder.getKey().longValue();
        reservationThemeDao.deleteById(key);

        assertThat(reservationThemeDao.findAll().stream().map(ReservationTheme::getId).toList()).doesNotContain(key);
    }
}
