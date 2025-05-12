package roomescape.reservation.controller;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import org.junit.jupiter.api.Test;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql("/sql/test-data.sql")
public class ReservationControllerTest {

//    @Test
//    void 예약_추가_테스트() {
//        Map<String, Object> reservation = new HashMap<>();
//        reservation.put("date", "2026-08-05");
//        reservation.put("memberId", 1);
//        reservation.put("timeId", 1);
//        reservation.put("themeId", 1);
//
//        Long id = RestAssured.given().log().all()
//                .contentType(ContentType.JSON)
//                .body(reservation)
//                .when().post("/reservations")
//                .then().log().all()
//                .statusCode(201)
//                .extract().as(Long.class);
//
//        assertThat(id).isEqualTo(6L);
//    }

    @Test
    void 예약_조회_테스트() {
        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(5));
    }

    @Test
    void 에약_삭제_테스트() {
        RestAssured.given().log().all()
                .when().delete("/reservations/3")
                .then().log().all()
                .statusCode(204);
    }
}
