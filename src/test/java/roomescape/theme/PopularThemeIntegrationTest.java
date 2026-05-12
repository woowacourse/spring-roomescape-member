package roomescape.theme;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import roomescape.theme.dto.response.ThemeDetailDto;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "spring.datasource.url=jdbc:h2:mem:populardb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE"
)
@Sql("/popular-theme-data.sql")
class PopularThemeIntegrationTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("인기 테마를 예약 수 기준 내림차순으로 조회한다.")
    void getPopularThemes() {
        List<ThemeDetailDto> themes = RestAssured.given().log().all()
                .when().get("/themes/popular?top=10")
                .then().log().all()
                .statusCode(200)
                .extract()
                .jsonPath().getList(".", ThemeDetailDto.class);

        assertThat(themes).hasSize(3);
        assertThat(themes.get(0).name()).isEqualTo("공포");
        assertThat(themes.get(1).name()).isEqualTo("판타지");
        assertThat(themes.get(2).name()).isEqualTo("미스터리");
    }
}
