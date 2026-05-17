package roomescape.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AdminControllerE2ETest {

    @DisplayName("예약 시간을 생성한다")
    @Test
    void 예약_시간_생성에_성공하면_201_Created를_응답한다() {
        Map<String, String> requestBody = Map.of(
                "startAt", "10:00"
        );

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when().post("/admin/times")
                .then().log().all()
                .statusCode(201)
                .header("Location", "/times/1");
    }

    @DisplayName("예약 시간 삭제에 성공하면 204 No Content를 응답한다")
    @Sql("/initialize_theme_and_time.sql")
    @Test
    void 예약_시간_삭제에_성공하면_204를_응답한다() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().delete("/admin/times/1")
                .then().log().all()
                .statusCode(204);
    }

    @DisplayName("잘못된 시간 형식으로 시간 생성을 요청하면 422 Unprocessable Entity를 응답한다")
    @Test
    void 잘못된_시간_형식으로_시간_생성을_요청하면_422를_응답한다() {
        Map<String, String> requestBodyWithIllegalTimeFormat = Map.of(
                "startAt", "10-00"
        );

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(requestBodyWithIllegalTimeFormat)
                .when().post("/admin/times")
                .then().log().all()
                .statusCode(422);
    }

    @DisplayName("예약이 존재하는 시간 삭제 요청 시 409 Conflict를 응답한다")
    @Sql("/data.sql")
    @Test
    void 삭제하려는_예약_시간에_대한_예약이_존재한다면_409를_응답한다() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().delete("/admin/times/1")
                .then().log().all()
                .statusCode(409);
    }

    @DisplayName("존재하지 않는 예약 시간 삭제 요청 시 422 Unprocessable Entity를 응답한다")
    @Test
    void 삭제하려는_예약_시간이_존재하지_않는다면_422를_응답한다() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().delete("/admin/times/1")
                .then().log().all()
                .statusCode(422);
    }

    @DisplayName("테마를 생성한다")
    @Test
    void 테마_생성에_성공하면_201_Created를_응답한다() {
        Map<String, String> requestBody = Map.of(
                "name", "귀신찾기",
                "description", "귀신을 찾는 테마입니다.",
                "imageUrl", "https://image.png"
        );

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when().post("/admin/themes")
                .then().log().all()
                .statusCode(201)
                .header(HttpHeaders.LOCATION, "/themes/1");
    }

    @DisplayName("테마 삭제에 성공하면 204 No Content를 응답한다")
    @Sql("/initialize_theme_and_time.sql")
    @Test
    void 테마_삭제에_성공하면_204를_응답한다() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().delete("/admin/themes/1")
                .then().log().all()
                .statusCode(204);
    }

    @DisplayName("존재하지 않는 테마 삭제 요청 시 422 Unprocessable Entity를 응답한다")
    @Test
    void 삭제하려는_테마가_존재하지_않는다면_422를_응답한다() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().delete("/admin/themes/1")
                .then().log().all()
                .statusCode(422);
    }
}
