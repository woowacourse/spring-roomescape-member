package roomescape.theme.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.theme.domain.Theme;
import roomescape.util.TestDataSourceFactory;

class JdbcThemeDaoTest {

    private JdbcThemeDao jdbcThemeDao;

    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() {
        DataSource dataSource = TestDataSourceFactory.getEmbeddedDataSource();
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcThemeDao = new JdbcThemeDao(jdbcTemplate);
    }

    @AfterEach
    void dropTable() {
        String dropSql = "DROP TABLE IF EXISTS reservation, reservation_time, theme, member";
        jdbcTemplate.execute(dropSql);
    }

    @DisplayName("테마 데이터를 저장한다")
    @Test
    void save_test() {
        // given
        String name = "레벨1 탈출";
        String description = "우테코 레벨1를 탈출하는 내용입니다.";
        String thumbnail = "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg";
        Theme theme = new Theme(null, name, description, thumbnail);

        // when
        Long id = jdbcThemeDao.saveAndReturnId(theme);

        // then
        assertThat(id).isEqualTo(7L);
    }

    @DisplayName("테마를 조회한다")
    @Test
    void find_all_test() {
        // when
        List<Theme> themes = jdbcThemeDao.findAll();

        // then
        assertThat(themes).hasSize(6);
    }

    @DisplayName("테마를 삭제한다")
    @Test
    void delete_test() {
        // given
        Long deleteId = 6L;

        // when
        jdbcThemeDao.deleteById(deleteId);

        // then
        String sql = "SELECT COUNT(1) FROM theme";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        assertThat(count).isEqualTo(5);
    }

    @DisplayName("특정 ID의 테마를 조회한다")
    @Test
    void find_by_id_test() {
        // given
        Long id = 3L;

        // when & then
        Theme theme = jdbcThemeDao.findById(id).get();
        assertThat(theme.getId()).isEqualTo(3L);
        assertThat(theme.getName()).isEqualTo("레벨3 탈출");
        assertThat(theme.getDescription()).isEqualTo("우테코 레벨3를 탈출하는 내용입니다.");
        assertThat(theme.getThumbnail()).isEqualTo(
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
    }

    @DisplayName("인기 테마 목록을 조회한다")
    @Test
    void find_by_period_and_limit() {
        // given
        LocalDate start = LocalDate.of(2025, 4, 24);
        LocalDate end = LocalDate.of(2025, 4, 30);
        int limit = 10;

        // when
        List<Theme> themeRankings = jdbcThemeDao.findByPeriodAndLimit(start, end, limit);

        // then
        assertThat(themeRankings.get(0).getId()).isEqualTo(3L);
        assertThat(themeRankings.get(0).getName()).isEqualTo("레벨3 탈출");
        assertThat(themeRankings.get(0).getDescription()).isEqualTo("우테코 레벨3를 탈출하는 내용입니다.");
        assertThat(themeRankings.get(0).getThumbnail()).isEqualTo(
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
    }

}
