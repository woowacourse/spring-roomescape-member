package roomescape;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationTimeApiTest {


    @Test
    @DisplayName("에약 시간 생성 테스트")
    void createReservationTimeWithStatus201Test() {
        Map<String, String> params = new HashMap<>();
        params.put("startAt", "19:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(201);
    }

    @Test
    @DisplayName("예약 시간 조회 테스트")
    void searchReservationTimeWithStatus200Test() {
        RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("예약 가능한 시간 조회 테스트")
    void searchPossibleReservationTimeWithStatus200Test() {
        Map<String, String> params = new HashMap<>();
        params.put("date", LocalDate.now().toString());
        params.put("themeId", "1");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .queryParams(params)
                .when().get("/times/possible")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("예약 시간 삭제 테스트")
    void deleteReservationTimeWithStatus204Test() {
        RestAssured.given().log().all()
                .when().delete("/times/2")
                .then().log().all()
                .statusCode(204);
    }

}
