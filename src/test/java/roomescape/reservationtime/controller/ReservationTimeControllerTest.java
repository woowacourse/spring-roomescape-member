package roomescape.reservationtime.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.Map;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationTimeControllerTest {

    private static final Map<String, String> RESERVATION_BODY = Map.of(
            "startAt", "13:00"
    );

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @DisplayName("예약 시간 추가 요청시, id를 포함한 예약 시간과 CREATED를 응답한다")
    @Test
    void addReservationTimeTest() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(RESERVATION_BODY)
                .when().post("/times")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .body("id", Matchers.equalTo(4))
                .body("startAt", Matchers.equalTo("13:00:00"));
    }

    @DisplayName("예약 시간 조회 요청시, 존재하는 모든 예약 시간과 OK를 응답한다")
    @Test
    void findAllReservationTimeTest() {
        RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("size()", Matchers.is(3))
                .body("[0].startAt", Matchers.equalTo("10:00:00"))
                .body("[1].startAt", Matchers.equalTo("11:00:00"))
                .body("[2].startAt", Matchers.equalTo("12:00:00"));
    }


    @DisplayName("예약 시간 삭제 요청시, 주어진 아이디에 해당하는 예약 시간이 없다면 NOT FOUND를 응답한다.")
    @Test
    void removeReservationTimeTest_WhenReservationTimeDoesNotExisted() {
        RestAssured.given().log().all()
                .when().delete("/times/1000")
                .then().log().all()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @DisplayName("예약 시간 삭제 요청시, 주어진 아이디에 해당하는 예약 시간이 있다면 삭제하고 NO CONTENT를 응답한다.")
    @Test
    void removeReservationTimeTest() {
        RestAssured.given().log().all()
                .when().delete("/times/3")
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
