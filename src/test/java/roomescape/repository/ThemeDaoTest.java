package roomescape.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.domain.Theme;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ThemeDaoTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    private ThemeDao themeDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private RowMapper<Theme> themeRowMapper = (resultSet, rowNum) -> new Theme(
            resultSet.getLong("id"),
            resultSet.getString("name"),
            resultSet.getString("description"),
            resultSet.getString("thumbnail")
    );

    @BeforeEach
    void setPort() {
        RestAssured.port = port;
    }

    @DisplayName("모든 테마를 조회한다.")
    @Test
    void findAll() {
        // given
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "이름1", "설명", "썸네일");

        // when
        List<Theme> allThemes = themeDao.findAll();

        // then
        assertAll(
                () -> assertThat(allThemes.get(0).getId()).isEqualTo(1),
                () -> assertThat(allThemes.get(0).getName()).isEqualTo("이름1"),
                () -> assertThat(allThemes.get(0).getDescription()).isEqualTo("설명"),
                () -> assertThat(allThemes.get(0).getThumbnail()).isEqualTo("썸네일")
        );
    }

    @DisplayName("아이디에 해당하는 테마를 조회한다.")
    @Test
    void findById() {
        // given
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "이름1", "설명", "썸네일");

        // when
        Theme theme = themeDao.findById(1);

        // then
        assertAll(
                () -> assertThat(theme.getId()).isEqualTo(1),
                () -> assertThat(theme.getName()).isEqualTo("이름1"),
                () -> assertThat(theme.getDescription()).isEqualTo("설명"),
                () -> assertThat(theme.getThumbnail()).isEqualTo("썸네일")
        );
    }

    @DisplayName("해당 ID를 가진 테마가 존재하지 않는다면 IllegalArgumentException 예외를 발생시킨다.")
    @Test
    void findThemeById_invalidID_IllegalArgumentException() {
        assertThatThrownBy(() -> themeDao.findById(0L)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("일주일간의 인기순 10개 테마를 반환한다.")
    @Test
    void findThemesByDescOrder() {
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "이름1", "설명", "썸네일");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "이름2", "설명", "썸네일");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "이름3", "설명", "썸네일");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "이름4", "설명", "썸네일");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "이름5", "설명", "썸네일");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "이름6", "설명", "썸네일");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "이름7", "설명", "썸네일");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "이름8", "설명", "썸네일");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "이름9", "설명", "썸네일");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "이름10", "설명", "썸네일");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "이름11", "설명", "썸네일");
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "10:00");
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)", "테니",
                "2024-04-30", 1, 1);
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)", "테니",
                "2024-04-29", 1, 1);
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)", "테니",
                "2024-04-30", 1, 2);
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)", "테니",
                "2024-04-29", 1, 2);
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)", "테니",
                "2024-04-30", 1, 3);
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)", "테니",
                "2024-04-29", 1, 3);
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)", "테니",
                "2024-04-30", 1, 4);
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)", "테니",
                "2024-04-29", 1, 4);
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)", "테니",
                "2024-04-30", 1, 5);
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)", "테니",
                "2024-04-29", 1, 5);
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)", "테니",
                "2024-04-30", 1, 6);
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)", "테니",
                "2024-04-29", 1, 6);
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)", "테니",
                "2024-04-30", 1, 7);
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)", "테니",
                "2024-04-29", 1, 7);
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)", "테니",
                "2024-04-30", 1, 8);
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)", "테니",
                "2024-04-29", 1, 8);
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)", "테니",
                "2024-04-30", 1, 9);
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)", "테니",
                "2024-04-29", 1, 9);
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)", "테니",
                "2024-04-30", 1, 10);
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)", "테니",
                "2024-04-29", 1, 10);
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)", "테니",
                "2024-04-30", 1, 11);

        List<Theme> themesByDescOrder = themeDao.findThemesByDescOrder();
        assertThat(themesByDescOrder).doesNotContain(new Theme(11L, "이름11", "설명", "썸네일"));
    } // TODO: 수정

    @DisplayName("테마를 저장하고 반환한다.")
    @Test
    void save() {
        // given &&  when
        long id = themeDao.save(new Theme(null, "이름", "설명", "썸네일"));
        List<Theme> themes = jdbcTemplate.query("SELECT * FROM theme", themeRowMapper);

        // then
        assertAll(
                () -> assertThat(id).isEqualTo(1),
                () -> assertThat(themes.get(0).getName()).isEqualTo("이름"),
                () -> assertThat(themes.get(0).getDescription()).isEqualTo("설명"),
                () -> assertThat(themes.get(0).getThumbnail()).isEqualTo("썸네일")
        );
    }

    @DisplayName("이름에 해당하는 테마가 있는지 확인한다.")
    @Test
    void existByName() {
        // given
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "이름1", "설명", "썸네일");

        // when
        boolean existByName = themeDao.existByName("이름1");

        // then
        assertThat(existByName).isTrue();
    }

    @DisplayName("해당 id의 테마를 삭제한다.")
    @Test
    void deleteById() {
        // given
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "이름1", "설명", "썸네일");

        // when
        boolean existByName = themeDao.existByName("이름1");

        // then
        assertThat(existByName).isTrue();
    }
}
