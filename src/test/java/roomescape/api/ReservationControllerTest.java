package roomescape.api;

import static org.hamcrest.Matchers.equalTo;

import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationControllerTest {

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
        SessionFilter sessionFilter = loginAs("김철수");

        RestAssured.given().log().all()
                .filter(sessionFilter)
                .contentType(ContentType.JSON)
                .pathParam("id",1)
                .when().delete("/reservations/{id}")
                .then().log().all()
                .statusCode(204);
    }

    @DisplayName("사용자는 다른 사람의 예약을 삭제할 수 없다.")
    @Test
    void userReservationDeleteByOtherUser() {
        SessionFilter sessionFilter = loginAs("이영희");

        RestAssured.given().log().all()
                .filter(sessionFilter)
                .contentType(ContentType.JSON)
                .pathParam("id", 1)
                .when().delete("/reservations/{id}")
                .then().log().all()
                .statusCode(404);
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

    @DisplayName("사용자는 본인 예약의 날짜와 시간을 변경할 수 있다.")
    @Test
    void userReservationDateTimeChange() {
        SessionFilter sessionFilter = loginAs("김철수");
        String changeDate = LocalDate.now().plusDays(8).toString();
        Map<String, Object> params = new HashMap<>();
        params.put("date", changeDate);
        params.put("timeId", 4);

        RestAssured.given().log().all()
                .filter(sessionFilter)
                .contentType(ContentType.JSON)
                .pathParam("id", 15)
                .body(params)
                .when().patch("/reservations/{id}")
                .then().log().all()
                .statusCode(200)
                .body("id", equalTo(15))
                .body("date", equalTo(changeDate))
                .body("time", equalTo("13:00"));
    }

    @DisplayName("사용자는 다른 사람 예약의 날짜와 시간을 변경할 수 없다.")
    @Test
    void userReservationDateTimeChangeByOtherUser() {
        SessionFilter sessionFilter = loginAs("이영희");
        Map<String, Object> params = new HashMap<>();
        params.put("date", LocalDate.now().plusDays(8).toString());
        params.put("timeId", 4);

        RestAssured.given().log().all()
                .filter(sessionFilter)
                .contentType(ContentType.JSON)
                .pathParam("id", 15)
                .body(params)
                .when().patch("/reservations/{id}")
                .then().log().all()
                .statusCode(404);
    }

    @DisplayName("이미 예약된 날짜와 시간으로 변경할 수 없다.")
    @Test
    void userReservationDateTimeChangeDuplicate() {
        SessionFilter sessionFilter = loginAs("김철수");
        Map<String, Object> params = new HashMap<>();
        params.put("date", LocalDate.now().plusDays(6).toString());
        params.put("timeId", 1);

        RestAssured.given().log().all()
                .filter(sessionFilter)
                .contentType(ContentType.JSON)
                .pathParam("id", 15)
                .body(params)
                .when().patch("/reservations/{id}")
                .then().log().all()
                .statusCode(409);
    }

    @DisplayName("날짜와 시간이 변경되지 않은 예약 변경 요청은 실패한다.")
    @Test
    void userReservationDateTimeChangeNotChanged() {
        SessionFilter sessionFilter = loginAs("김철수");
        Map<String, Object> params = new HashMap<>();
        params.put("date", LocalDate.now().plusDays(7).toString());
        params.put("timeId", 3);

        RestAssured.given().log().all()
                .filter(sessionFilter)
                .contentType(ContentType.JSON)
                .pathParam("id", 15)
                .body(params)
                .when().patch("/reservations/{id}")
                .then().log().all()
                .statusCode(422);
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
