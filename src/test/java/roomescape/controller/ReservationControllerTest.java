package roomescape.controller;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
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
public class ReservationControllerTest {

    @Test
    void 예약_추가() {
        // given
        int timeId = createTime("10:00");
        int themeId = createTheme("방탈출1", "다함께 탈출해요 방탈출", "https://asdfsdf.sdfs");

        // when & then
        createReservation("브라운", LocalDate.now().plusDays(1).toString(), timeId, themeId)
                .statusCode(201)
                .body("id", notNullValue())
                .header("Location", "/reservations/1");
    }

    @Test
    void 같은_날짜_및_시간이더라도_테마가_다르면_예약_가능하다() {
        // given
        int timeId = createTime("10:00");
        int themeId1 = createTheme("방탈출1", "다함께 탈출해요 방탈출", "https://asdfsdf.sdfs");
        int themeId2 = createTheme("방탈출2", "다함께 탈출해요 방탈출2", "https://asdfsdf.sdfssdafdasf");

        // when & then
        createReservation("로지", LocalDate.now().plusDays(1).toString(), timeId, themeId1).statusCode(201)
                .body("name", is("로지"));
        createReservation("러키", LocalDate.now().plusDays(1).toString(), timeId, themeId2).statusCode(201)
                .body("name", is("러키"));
    }

    @Test
    void 중복_예약을_하면_409를_반환한다() {
        // given
        int timeId = createTime("10:00");
        int themeId = createTheme("방탈출1", "설명", "https://asdfsdf.sdfs");
        String date = LocalDate.now().plusDays(1).toString();
        createReservation("브라운", date, timeId, themeId).statusCode(201);

        // when & then
        createReservation("로지", date, timeId, themeId)
                .statusCode(409);
    }

    @Test
    void 지나간_날짜로_예약하면_422를_반환한다() {
        // given
        int timeId = createTime("10:00");
        int themeId = createTheme("방탈출1", "설명", "https://asdfsdf.sdfs");

        // when & then
        createReservation("브라운", "2026-04-01", timeId, themeId)
                .statusCode(422);
    }

    @Test
    void 예약_조회() {
        // given
        int timeId = createTime("10:00");
        int themeId = createTheme("방탈출1", "다함께 탈출해요 방탈출", "https://asdfsdf.sdfs");
        createReservation("브라운", LocalDate.now().plusDays(1).toString(), timeId, themeId).statusCode(201);

        // when & then
        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("name", hasItem("브라운"));
    }

    @Test
    void 예약자_이름으로_예약_조회() {
        // given
        int timeId = createTime("10:00");
        int themeId = createTheme("방탈출1", "다함께 탈출해요 방탈출", "https://asdfsdf.sdfs");
        createReservation("브라운", LocalDate.now().plusDays(1).toString(), timeId, themeId).statusCode(201);

        // when & then
        RestAssured.given().log().all()
                .when().get("/reservations?name=브라운")
                .then().log().all()
                .statusCode(200)
                .body("name", hasItem("브라운"));
    }

    @Test
    void 예약_삭제() {
        // given
        int timeId = createTime("10:00");
        int themeId = createTheme("방탈출11", "다함께 탈출해요 방탈출", "https://asdfsdf.sdfs");
        int reservationId = createReservation("브라운", LocalDate.now().plusDays(1).toString(), timeId, themeId)
                .statusCode(201)
                .extract().path("id");

        // when & then
        RestAssured.given().log().all()
                .when().delete("/reservations/" + reservationId)
                .then().log().all()
                .statusCode(204);
    }

    private int createTime(String startAt) {
        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(Map.of("startAt", startAt))
                .when().post("/admin/times")
                .then().statusCode(201)
                .extract().jsonPath().getInt("id");
    }

    private int createTheme(String name, String description, String thumbnail) {
        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(Map.of("name", name, "description", description, "thumbnail", thumbnail))
                .when().post("/admin/themes")
                .then().statusCode(201)
                .extract().jsonPath().getInt("id");
    }

    private ValidatableResponse createReservation(String name, String date, int timeId, int themeId) {
        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(Map.of("name", name, "date", date, "timeId", timeId, "themeId", themeId))
                .when().post("/reservations")
                .then().log().all();
    }
}
