package roomescape.controller;

import static org.hamcrest.CoreMatchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationControllerTest {

    @DisplayName("같은 날짜 및 시간 예약이 존재하면 404 Not Found를 던진다")
    @Test
    void reservationAdd() {
        //given
        Map<String, String> timeParams = Map.of(
                "startAt", "15:40"
        );

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(timeParams)
                .when().post("/times")
                .then().log().all()
                .statusCode(200)
                .body("id", is(1));

        Map<String, String> params = Map.of(
                "name", "브라운",
                "date", "2023-08-05",
                "timeId", "1"
        );

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(200);

        Map<String, String> duplicated = Map.of(
                "name", "네오",
                "date", "2023-08-05",
                "timeId", "1"
        );

        //when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(duplicated)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(404);
    }

    @DisplayName("존재하지 않는 예약을 삭제하려는 경우 404 Not Found를 던진다")
    @Test
    void reservationRemove() {
        //given
        long notExistId = 1;

        //when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().delete("/reservations/" + notExistId)
                .then().log().all()
                .statusCode(404);
    }
}
