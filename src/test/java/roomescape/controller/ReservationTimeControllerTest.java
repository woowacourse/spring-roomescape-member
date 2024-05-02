package roomescape.controller;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import roomescape.service.dto.ReservationRequest;
import roomescape.service.dto.ReservationTimeCreateRequest;
import roomescape.service.dto.ReservationTimeReadRequest;
import roomescape.service.dto.ThemeRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReservationTimeControllerTest {
    @LocalServerPort
    private int port;

    @BeforeEach
    void initPort() {
        RestAssured.port = port;
    }

    @AfterEach
    void initData() {
        RestAssured.get("/reservations")
                .then().extract().body().jsonPath().getList("id")
                .forEach(id -> RestAssured.delete("/reservations/" + id));

        RestAssured.get("/times")
                .then().extract().body().jsonPath().getList("id")
                .forEach(id -> RestAssured.delete("/times/" + id));

        RestAssured.get("/themes")
                .then().extract().body().jsonPath().getList("id")
                .forEach(id -> RestAssured.delete("/themes/" + id));
    }

    @DisplayName("시간 정보를 추가한다.")
    @Test
    void createReservationTime() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(new ReservationTimeCreateRequest("10:00"))
                .when().post("/times")
                .then().log().all().statusCode(201).body("id", is(greaterThan(0)));
    }

    @DisplayName("시간 추가 실패 테스트 - 중복 시간 오류")
    @Test
    void createDuplicateTime() {
        //given
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(new ReservationTimeCreateRequest("10:00"))
                .when().post("/times");

        //when&then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(new ReservationTimeCreateRequest("10:00"))
                .when().post("/times")
                .then().log().all().statusCode(400).body("message", is("이미 같은 시간이 존재합니다."));
    }

    @DisplayName("시간 추가 실패 테스트 - 시간 오류")
    @Test
    void createInvalidReservationTime() {
        //given
        String invalidTime = "";

        //when&then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(new ReservationTimeCreateRequest(invalidTime))
                .when().post("/times")
                .then().log().all()
                .assertThat().statusCode(400).body("message", is("올바르지 않은 시간입니다."));
    }

    @DisplayName("등록된 시간 내역을 조회한다.")
    @Test
    void findAllReservationTime() {
        //given
        RestAssured.given().contentType(ContentType.JSON).body(new ReservationTimeCreateRequest("10:00"))
                .when().post("/times");

        //when&then
        RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all().statusCode(200).body("size()", is(1));
    }

    @DisplayName("시간 정보를 id로 삭제한다.")
    @Test
    void deleteReservationTimeById() {
        //given
        var id = RestAssured.given().contentType(ContentType.JSON).body(new ReservationTimeCreateRequest("10:00"))
                .when().post("/times")
                .then().extract().response().jsonPath().get("id");

        //when&then
        RestAssured.given().log().all()
                .when().delete("/times/" + id)
                .then().log().all()
                .assertThat().statusCode(204);

        RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .assertThat().body("size()", is(0));
    }

    @DisplayName("시간 삭제 실패 테스트 - 이미 예약이 존재하는 시간 삭제 시도 오류")
    @Test
    void cannotDeleteReservationTime() {
        //given
        long timeId = (int) RestAssured.given().contentType(ContentType.JSON)
                .body(new ReservationTimeCreateRequest("10:00"))
                .when().post("/times")
                .then().extract().response().jsonPath().get("id");

        ThemeRequest themeRequest = new ThemeRequest("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
        long themeId = (int) RestAssured.given().contentType(ContentType.JSON).body(themeRequest)
                .when().post("/themes")
                .then().extract().response().jsonPath().get("id");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(new ReservationRequest("브라운", "2222-04-30", timeId, themeId))
                .when().post("/reservations");

        //when&then
        RestAssured.given().log().all()
                .when().delete("/times/" + timeId)
                .then().log().all()
                .assertThat().statusCode(400).body("message", is("해당 시간에 예약이 존재해서 삭제할 수 없습니다."));
        RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .assertThat().body("size()", is(1));
    }

    @DisplayName("예약 가능한 시간 조회 테스트")
    @Test
    void findAvailableTime() {
        //given
        long timeId = (int) RestAssured.given().contentType(ContentType.JSON)
                .body(new ReservationTimeCreateRequest("10:00"))
                .when().post("/times")
                .then().extract().response().jsonPath().get("id");

        ThemeRequest themeRequest = new ThemeRequest("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
        long themeId = (int) RestAssured.given().contentType(ContentType.JSON).body(themeRequest)
                .when().post("/themes")
                .then().extract().response().jsonPath().get("id");

        String date = "2222-04-30";
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(new ReservationRequest("브라운", date, timeId, themeId))
                .when().post("/reservations");

        //when&then
        ReservationTimeReadRequest reservationTimeReadRequest = new ReservationTimeReadRequest(date, themeId);
        RestAssured.given().contentType(ContentType.JSON)
                .body(reservationTimeReadRequest)
                .when().get("/times/available?date=" + date + "&themeId=" + themeId)
                .then().log().all().statusCode(200).body("size()", is(0));
    }
}
