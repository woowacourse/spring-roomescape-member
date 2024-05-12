package roomescape.endpoint;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.notNullValue;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import roomescape.dto.ThemeRequest;
import roomescape.dto.ThemeResponse;
import roomescape.repository.JdbcThemeRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(properties = {"spring.config.location = classpath:application-test.yml"})
public class ThemeEndPointTest {

    @Autowired
    JdbcThemeRepository themeRepository;

    @DisplayName("테마 목록을 조회하면 상태 코드 200과 테마 목록을 응답으로 반환한다.")
    @Test
    void getThemes() {
        List<ThemeResponse> responses = RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath()
                .getList(".", ThemeResponse.class);

        List<ThemeResponse> expected = List.of(
                new ThemeResponse(1L, "이름1", "설명1", "썸네일1"),
                new ThemeResponse(2L, "이름2", "설명2", "썸네일2")
        );

        assertThat(responses)
                .isEqualTo(expected);
    }

    @DisplayName("테마를 추가하면 상태 코드 201와 추가된 객체를 반환한다.")
    @Test
    void addTheme() {
        ThemeRequest request = new ThemeRequest("이름", "설명", "썸네일");
        ThemeResponse expected = new ThemeResponse(3L, "이름", "설명", "썸네일");

        ThemeResponse response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/themes")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(ThemeResponse.class);

        assertThat(response)
                .isEqualTo(expected);
    }

    @DisplayName("테마를 삭제하면 상태 코드 204를 반환한다.")
    @Test
    void deleteTheme() {
        RestAssured.given().log().all()
                .when().delete("/themes/1")
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .body(notNullValue());
    }

    @DisplayName("예약이 존재하는 테마를 삭제하면 상태 코드 400을 반환한다.")
    @Test
    void deleteThemeFailed() {
        RestAssured.given().log().all()
                .when().delete("/themes/2")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("테마 순위를 조회하면 상태코드 200과 테마의 순위를 반환한다.")
    @Test
    void getTopThemes() {
        List<ThemeResponse> responses = RestAssured.given().log().all()
                .when().get("/themes/rankings")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath()
                .getList(".", ThemeResponse.class);

        List<ThemeResponse> expected = List.of(
                new ThemeResponse(2L, "이름2", "설명2", "썸네일2")
        );

        assertThat(responses)
                .isEqualTo(expected);
    }
}
