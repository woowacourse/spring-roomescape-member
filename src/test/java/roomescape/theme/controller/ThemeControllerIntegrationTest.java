package roomescape.theme.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import roomescape.theme.dto.ThemeResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql(value = {"/schema.sql", "/data.sql"}, executionPhase = BEFORE_TEST_METHOD)
class ThemeControllerIntegrationTest {

  @LocalServerPort
  int randomServerPort;

  @BeforeEach
  public void initReservation() {
    RestAssured.port = randomServerPort;
  }

  @DisplayName("전체 테마 정보를 조회한다.")
  @Test
  void getThemesTest() {
    RestAssured.given().log().all()
        .when().get("/themes")
        .then().log().all()
        .statusCode(200)
        .body("size()", is(15));
  }

  @DisplayName("테마 정보를 저장한다.")
  @Test
  void saveThemeTest() {
    final Map<String, String> params = new HashMap<>();
    params.put("name", "켈리의 두근두근");
    params.put("description", "켈리와 함께하는 두근두근 데이트");
    params.put("thumbnail", "켈리 사진");

    RestAssured.given().log().all()
        .contentType(ContentType.JSON)
        .body(params)
        .when().post("/themes")
        .then().log().all()
        .statusCode(201)
        .body("id", is(16));
  }

  @DisplayName("테마 정보를 삭제한다.")
  @Test
  void deleteThemeTest() {
    // 예약 시간 정보 삭제
    RestAssured.given().log().all()
        .when().delete("/themes/7")
        .then().log().all()
        .statusCode(204);

    // 예약 시간 정보 조회
    final List<ThemeResponse> themes = RestAssured.given().log().all()
        .when().get("/themes")
        .then().log().all()
        .statusCode(200).extract()
        .jsonPath().getList(".", ThemeResponse.class);

    assertThat(themes.size()).isEqualTo(14);
  }
}
