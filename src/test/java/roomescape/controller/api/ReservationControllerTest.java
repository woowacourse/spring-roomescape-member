package roomescape.controller.api;

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
class ReservationControllerTest {

    private static final Map<String, String> RESERVATION_BODY = Map.of(
            "name", "검프",
            "date", "3000-03-17",
            "timeId", "1",
            "themeId", "1"
    );

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @DisplayName("예약 추가 요청시, id를 포함한 예약 내용과 CREATED를 응답한다")
    @Test
    void addReservationTest() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(RESERVATION_BODY)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .body("name", Matchers.equalTo("검프"))
                .body("date", Matchers.equalTo("3000-03-17"));
    }

    @DisplayName("예약 조회 요청시, 존재하는 모든 예약과 OK를 응답한다")
    @Test
    void findAllReservationTest() {
        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("size()", Matchers.is(3))
                .body("[0].name", Matchers.equalTo("test1"))
                .body("[1].name", Matchers.equalTo("test2"))
                .body("[2].name", Matchers.equalTo("test3"));
    }


    @DisplayName("예약 삭제 요청시, 주어진 아이디에 해당하는 예약이 없다면 NOT FOUND를 응답한다.")
    @Test
    void removeReservationTest_WhenReservationDoesNotExisted() {
        RestAssured.given().log().all()
                .when().delete("/reservations/1000")
                .then().log().all()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @DisplayName("예약 삭제 요청시, 주어진 아이디에 해당하는 예약이 있다면 삭제하고 NO CONTENT를 응답한다.")
    @Test
    void removeReservationTest() {
        RestAssured.given().log().all()
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
