package roomescape.repository;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.domain.Theme;
import roomescape.exception.IllegalThemeException;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ThemeDaoTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    private ThemeDao themeDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setPort() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("해당 ID를 가진 테마가 존재하지 않는다면 예외가 발생한다.")
    void findTimeById_AbsenceId_ExceptionThrown() {
        assertThatThrownBy(() -> themeDao.findById(0L))
                .isInstanceOf(IllegalThemeException.class);
    }

    @Test
    void findThemesByDescOrder() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDate sevenDaysBefore = LocalDate.now().minusDays(7);
        for (int i = 2; i <= 12; i++) {
            jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "이름" + i, "설명", "썸네일");
            jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)", "테니", yesterday, 1, i);
            jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)", "테니", sevenDaysBefore, 1, i);
        }
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "이름13", "설명", "썸네일");
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)", "테니", yesterday, 1, 13);

        List<Theme> themesByDescOrder = themeDao.findThemesOrderByReservationThemeCountDesc();
        assertThat(themesByDescOrder).doesNotContain(new Theme(13L, "이름13", "설명", "썸네일"));
    }
}
