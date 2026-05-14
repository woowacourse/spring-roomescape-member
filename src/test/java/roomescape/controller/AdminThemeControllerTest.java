package roomescape.controller;

import static org.hamcrest.Matchers.notNullValue;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import java.time.LocalDate;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AdminThemeControllerTest {

    @Test
    void 테마를_추가한다() {
        // when & then
        createTheme("방탈출1", "다함께 탈출해요 방탈출", "https://asdfsdf.sdfs")
                .statusCode(201)
                .body("id", notNullValue())
                .header("Location", "/admin/themes/1");
    }

    @Test
    void 테마를_삭제한다() {
        // given
        int themeId = createTheme("방탈출11", "다함께 탈출해요 방탈출", "https://asdfsdf.sdfs")
                .statusCode(201)
                .extract().path("id");

        // when & then
        RestAssured.given().log().all()
                .when().delete("/admin/themes/" + themeId)
                .then().log().all()
                .statusCode(204);
    }

    @Test
    void 이미_존재하는_테마를_추가하면_409를_반환한다() {
        // given
        createTheme("방탈출1", "설명", "https://asdfsdf.sdfs").statusCode(201);

        // when & then
        createTheme("방탈출1", "설명2", "https://asdfsdf2.sdfs")
                .statusCode(409);
    }

    @Test
    void 존재하지_않는_테마를_삭제하면_404를_반환한다() {
        RestAssured.given().log().all()
                .when().delete("/admin/themes/999")
                .then().log().all()
                .statusCode(404);
    }

    @Test
    void 예약이_존재하는_테마를_삭제하면_422를_반환한다() {
        // given
        int themeId = createTheme("방탈출1", "설명", "https://asdfsdf.sdfs")
                .statusCode(201)
                .extract().path("id");

        int timeId = createTime("10:00");
        createReservation("브라운", LocalDate.now().plusDays(1).toString(), timeId, themeId);

        // when & then
        RestAssured.given().log().all()
                .when().delete("/admin/themes/" + themeId)
                .then().log().all()
                .statusCode(422);
    }

    private int createTime(String startAt) {
        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(Map.of("startAt", startAt))
                .when().post("/admin/times")
                .then().statusCode(201)
                .extract().jsonPath().getInt("id");
    }

    private void createReservation(String name, String date, int timeId, int themeId) {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(Map.of("name", name, "date", date, "timeId", timeId, "themeId", themeId))
                .when().post("/reservations")
                .then().statusCode(201);
    }

    private ValidatableResponse createTheme(String name, String description, String thumbnail) {
        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(Map.of("name", name, "description", description, "thumbnail", thumbnail))
                .when().post("/admin/themes")
                .then().log().all();
    }
}
