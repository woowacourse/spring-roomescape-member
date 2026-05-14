package roomescape.api;

import io.restassured.RestAssured;
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
        params.put("date", "2026-05-05");
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
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .queryParam("username","김철수")
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200);


    }

    // 과거 날짜로는 예약이 실패한다.
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
                .statusCode(500);
    }

}
