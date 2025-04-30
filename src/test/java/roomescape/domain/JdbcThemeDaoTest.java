package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.reservation.domain.Theme;
import roomescape.reservation.domain.repository.JdbcThemeDao;
import roomescape.util.TestDataSourceFactory;

public class JdbcThemeDaoTest {

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
        String dropSql = "DROP TABLE IF EXISTS reservation, reservation_time, theme";
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
        assertThat(id).isEqualTo(4L);
    }

    @DisplayName("테마를 조회한다")
    @Test
    void find_all_test() {
        // when
        List<Theme> themes = jdbcThemeDao.findAll();
        // then
        assertThat(themes).hasSize(3);
    }

}
