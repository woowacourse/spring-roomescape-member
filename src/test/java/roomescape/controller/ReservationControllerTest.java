package roomescape.controller;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationControllerTest {

    @Test
    void 예약_추가() {
        long timeId = createTime("10:00");
        long themeId = createTheme("방탈출1", "다함께 탈출해요 방탈출.", "https://asdfsdf.sdfs");

        Map<String, Object> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", "2023-08-05");
        params.put("timeId", timeId);
        params.put("themeId", themeId);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .body("id", notNullValue())
                .extract()
                .jsonPath()
                .getLong("id");
    }

    @Test
    void 같은_날짜_및_시간이더라도_테마가_다르면_예약_가능하다() {
        long timeId = createTime("10:00");
        long themeId1 = createTheme("방탈출1", "다함께 탈출해요 방탈출.", "https://asdfsdf.sdfs");
        long themeId2 = createTheme("방탈출2", "다함께 탈출해요 방탈출2.", "https://asdfsdf.sdfssdafdasf");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(Map.of(
                        "name", "로지",
                        "date", "2026-05-05",
                        "timeId", timeId,
                        "themeId", themeId1
                ))
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .body("name", is("로지"));

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(Map.of(
                        "name", "러키",
                        "date", "2026-05-05",
                        "timeId", timeId,
                        "themeId", themeId2
                ))
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .body("name", is("러키"));
    }

    @Test
    void 예약_조회() {
        long timeId = createTime("10:00");
        long themeId = createTheme("방탈출1", "다함께 탈출해요 방탈출.", "https://asdfsdf.sdfs");

        createReservation("브라운", "2023-08-05", timeId, themeId);

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("name", hasItem("브라운"));
    }

    @Test
    void 예약_삭제() {
        long timeId = createTime("10:00");
        long themeId = createTheme("방탈출11", "다함께 탈출해요 방탈출.", "https://asdfsdf.sdfs");

        long reservationId = createReservation("브라운", "2023-08-05", timeId, themeId);

        RestAssured.given().log().all()
                .when().delete("/reservations/" + reservationId)
                .then().log().all()
                .statusCode(204);
    }

    private long createTime(String startAt) {
        Map<String, String> params = new HashMap<>();
        params.put("startAt", startAt);

        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/times")
                .then().log().all()
                .statusCode(201)
                .extract()
                .jsonPath()
                .getLong("id");
    }

    private long createTheme(String name, String description, String thumbnail) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("description", description);
        params.put("thumbnail", thumbnail);

        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/themes")
                .then().log().all()
                .statusCode(201)
                .extract()
                .jsonPath()
                .getLong("id");
    }

    private long createReservation(String name, String date, long timeId, long themeId) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("date", date);
        params.put("timeId", timeId);
        params.put("themeId", themeId);

        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .body("id", notNullValue())
                .extract()
                .jsonPath()
                .getLong("id");
    }
}
