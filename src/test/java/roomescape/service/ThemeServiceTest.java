package roomescape.service;

import io.restassured.RestAssured;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.Theme;
import roomescape.dto.request.ThemeRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ThemeServiceTest {

    @LocalServerPort
    int port;

    @Autowired
    ThemeService themeService;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("테마를 저장할 수 있다.")
    void save() {
        Theme theme = themeService.save(new ThemeRequest("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"));

        assertThat(theme).isNotNull();
    }

    @Test
    @DisplayName("전체 테마를 조회할 수 있다.")
    void findAll() {
        themeService.save(new ThemeRequest("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"));

        List<Theme> theme = themeService.findAll();

        assertThat(theme).hasSize(1);
    }

    @Test
    @DisplayName("테마를 삭제할 수 있다.")
    void delete() {
        themeService.save(new ThemeRequest("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"));

        themeService.delete(1L);
        List<Theme> theme = themeService.findAll();

        assertThat(theme).hasSize(0);
    }

    @DisplayName("많이 예약한 순으로 10개를 정렬한다.")
    @Disabled
    @Sql("/testdata.sql")
    @Test
    void popularThemeTest() {
        List<Theme> topRanking = themeService.findTopRanking();
        Assertions.assertAll(
                () -> assertThat(topRanking.get(0).getName()).isEqualTo("테마 2"),
                () -> assertThat(topRanking.get(1).getName()).isEqualTo("테마 1"),
                () -> assertThat(topRanking.get(2).getName()).isEqualTo("테마 3"),
                () -> assertThat(topRanking.get(3).getName()).isEqualTo("테마 4"),
                () -> assertThat(topRanking.get(4).getName()).isEqualTo("테마 5"),
                () -> assertThat(topRanking.get(5).getName()).isEqualTo("테마 6"),
                () -> assertThat(topRanking.get(6).getName()).isEqualTo("테마 7"),
                () -> assertThat(topRanking.get(7).getName()).isEqualTo("테마 8"),
                () -> assertThat(topRanking.get(8).getName()).isEqualTo("테마 9"),
                () -> assertThat(topRanking.get(9).getName()).isEqualTo("테마 10")
        );
    }
}
