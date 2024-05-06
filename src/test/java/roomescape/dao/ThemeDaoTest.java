package roomescape.dao;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.exception.RoomEscapeException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
    @DisplayName("해당 ID를 가진 테마가 존재하지 않는다면 예외가 발생한다.")
    void findTimeById_AbsenceId_ExceptionThrown() {
        assertThatThrownBy(() -> themeDao.findById(0L))
                .isInstanceOf(RoomEscapeException.class);
    }
}
