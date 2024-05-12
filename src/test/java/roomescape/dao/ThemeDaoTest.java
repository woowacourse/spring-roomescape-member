package roomescape.dao;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.theme.theme.domain.Theme;
import roomescape.theme.theme.dao.ThemeDao;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ThemeDaoTest {

    @LocalServerPort
    private Integer port;

    @Autowired
    private ThemeDao themeDao;

    @BeforeEach
    void setPort() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("해당 ID를 가진 테마가 존재하지 않는다면 빈값을 반환한다.")
    void findTimeById_AbsenceId_ExceptionThrown() {
        Optional<Theme> optionalTheme = themeDao.findById(0L);

        assertThat(optionalTheme.isEmpty()).isTrue();
    }
}
