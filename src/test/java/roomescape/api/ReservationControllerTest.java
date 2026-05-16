package roomescape.api;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import roomescape.reservation.controller.ReservationController;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationControllerTest {
    @Autowired
    private ReservationController reservationController;

    @DisplayName("사용자 예약 추가")
    @Test
    void userReservationCreateApi() {
        Map<String,Object> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", LocalDate.now().plusDays(1).toString());
        params.put("timeId", 1);
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);
    }

    @DisplayName("사용자 예약 삭제")
    @Test
    void userReservationDelete(){
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .pathParam("id",1)
                .when().delete("/reservations/{id}")
                .then().log().all()
                .statusCode(204);
    }

    @DisplayName("사용자 예약 조회")
    @Test
    void userReservationRetrieve(){
        SessionFilter sessionFilter = loginAs("김철수");

        RestAssured.given().log().all()
                .filter(sessionFilter)
                .contentType(ContentType.JSON)
                .when().get("/reservations/me")
                .then().log().all()
                .statusCode(200);


    }

    @DisplayName("예약은 오늘 이후 날짜로만 가능하다.")
    @Test
    void reservationDateMustBeFuture() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", LocalDate.now().minusDays(1).toString());
        params.put("timeId", 1);
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(422);
    }

    @DisplayName("동일 테마, 동일 날짜, 동일 타임 중복된 예약은 실패한다.")
    @Test
    void duplicateReservation() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", LocalDate.now().plusDays(1).toString());
        params.put("timeId", 1);
        params.put("themeId", 1);
        createReservation(params);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(409);
    }

    @DisplayName("동일 테마, 동일 날짜, 다른 타임 예약은 성공한다.")
    @Test
    void duplicateThemeAndDate() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", LocalDate.now().plusDays(1).toString());
        params.put("timeId", 1);
        params.put("themeId", 1);
        createReservation(params);

        params.put("timeId", 2);
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);
    }

    @DisplayName("동일 테마, 다른 날짜, 다른 타임 예약은 성공한다.")
    @Test
    void duplicateTheme() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", LocalDate.now().plusDays(1).toString());
        params.put("timeId", 1);
        params.put("themeId", 1);
        createReservation(params);

        params.put("timeId", 2);
        params.put("date", LocalDate.now().plusDays(2).toString());
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);
    }

    private void createReservation(Map<String, Object> params) {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);
    }

    private SessionFilter loginAs(String username) {
        SessionFilter sessionFilter = new SessionFilter();
        Map<String, Object> params = new HashMap<>();
        params.put("username", username);

        RestAssured.given().log().all()
                .filter(sessionFilter)
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/login")
                .then().log().all()
                .statusCode(200);

        return sessionFilter;
    }

}
