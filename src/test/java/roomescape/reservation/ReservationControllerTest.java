package roomescape.reservation;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Sql(scripts = {"/truncate.sql", "/test-data.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class ReservationControllerTest {
    @Test
    void 예약_생성() {
        Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", "브라운");
        reservation.put("date", "2026-05-05");
        reservation.put("timeId", 2);
        reservation.put("themeId", 2);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .body("id", is(5));
    }

    @Test
    void 예약_조회() {
        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(4)); // 테스트 더미데이터의 예약이 총 4개라 4개 반환
    }

    @Test
    void 예약_추가_및_삭제() {
        Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", "브라운");
        reservation.put("date", "2026-05-05");
        reservation.put("timeId", 4);
        reservation.put("themeId", 4);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .body("id", is(5));

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(5));

        RestAssured.given().log().all()
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(204);

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(4));
    }

    @Test
    void 내_이름으로_나의_특정_예약_삭제_및_나의_예약_목록_조회() {
        RestAssured.given().log().all()
                .queryParams("name", "a")
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));

        RestAssured.given().log().all()
                .pathParam("id", 1)
                .queryParams("name", "a")
                .when().delete("/reservations/{id}")
                .then().log().all()
                .statusCode(204);

        RestAssured.given().log().all()
                .queryParams("name", "a")
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }

//    @Test
//    void 내_이름이_아니면_나의_특정_예약_삭제_및_나의_예약_목록_조회_실패() {
//        RestAssured.given().log().all()
//                .queryParams("name", "a")
//                .when().get("/reservations")
//                .then().log().all()
//                .statusCode(200)
//                .body("size()", is(1));
//
//        RestAssured.given().log().all()
//                .pathParam("id", 1)
//                .queryParams("name", "a")
//                .when().delete("/reservations/{id}")
//                .then().log().all()
//                .statusCode(204);
//
//        RestAssured.given().log().all()
//                .queryParams("name", "a")
//                .when().get("/reservations")
//                .then().log().all()
//                .statusCode(200)
//                .body("size()", is(0));
//    }
}
