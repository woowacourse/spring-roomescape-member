package roomescape.controller.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.controller.dto.reservation.ReservationRequest;
import roomescape.controller.dto.reservationTime.ReservationTimeRequest;
import roomescape.controller.dto.theme.ThemeRequest;

import java.time.LocalDate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationApiTest {

    private int themeId = 0;
    private int timeId = 0;

    @BeforeEach
    void setUp() {

        //Theme 생성
        var themeRequest = new ThemeRequest(
                "스테이지",
                "인기 아이돌 실종 사건",
                "무엇보다 무섭다"
        );
        themeId = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(themeRequest)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201)
                .extract().path("id");

        //시간 생성
        ReservationTimeRequest reservationTimeRequest = new ReservationTimeRequest("19:00");
        timeId = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationTimeRequest)
                .when().post("/times")
                .then().log().all()
                .statusCode(201)
                .extract().path("id");

    }

    @Test
    @DisplayName("예약 조회 테스트")
    void searchReservationsWithStatus200Test() {
        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("예약 생성 테스트")
    void createReservationWithStatus201Test() {

        ReservationRequest request = new ReservationRequest(
                "브라운",
                LocalDate.now().plusDays(3),
                timeId,
                themeId
        );
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);
    }

    @Test
    @DisplayName("예약 삭제 테스트")
    void deleteReservationWith204Test() {
        ReservationRequest request = new ReservationRequest(
                "브라운",
                LocalDate.now().plusDays(3),
                timeId,
                themeId
        );
        int reservationId = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .extract().path("id");

        RestAssured.given().log().all()
                .when().delete("/reservations/" + reservationId)
                .then().log().all()
                .statusCode(204);
    }


}
