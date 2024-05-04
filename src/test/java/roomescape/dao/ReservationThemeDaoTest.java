package roomescape.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.ReservationTheme;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Import(ReservationThemeDao.class)
@Sql(scripts = {"/test_schema.sql", "/test_data.sql"})
public class ReservationThemeDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ReservationThemeDao reservationThemeDao;

    @DisplayName("모든 테마를 조회한다.")
    @Test
    void findAllTest() {
        List<ReservationTheme> reservationThemes = reservationThemeDao.findAll();

        assertThat(reservationThemes.size()).isEqualTo(1);
    }

    @DisplayName("테마 ID를 이용하여 테마를 조회한다.")
    @Test
    void findByIdTest() {
        ReservationTheme reservationTheme = reservationThemeDao.findById(1L).get();

        assertThat(reservationTheme.getId()).isEqualTo(1L);
    }

    @DisplayName("ID가 존재하지 않으면 빈 시간을 반환한다.")
    @Test
    void findByWrongIdTest() {
        Optional<ReservationTheme> reservationTheme = reservationThemeDao.findById(9L);

        assertThat(reservationTheme).isEqualTo(Optional.empty());
    }

    @DisplayName("테마를 추가한다.")
    @Test
    void insertTest() {
        Long index = jdbcTemplate.queryForObject("SELECT count(*) FROM theme", Long.class);
        Long id = reservationThemeDao.insert("test", "testDesc", "testThumbnail");

        assertThat(id).isEqualTo(index + 1);
    }

    @DisplayName("ID를 이용하여 테마를 삭제한다.")
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

    @DisplayName("최근 일주일간 가장 많이 예약된 테마를 조회한다.")
    @Test
    void findBestThemesBetweenDates() {
        List<ReservationTheme> bestTheme = reservationThemeDao.findBestThemesBetweenDates("2023-12-28", "2024-01-02", 10);

        assertThat(bestTheme.size()).isEqualTo(1);
    }
}
