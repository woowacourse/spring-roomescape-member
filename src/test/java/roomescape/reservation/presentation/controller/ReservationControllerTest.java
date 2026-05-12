package roomescape.reservation.presentation.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Import;
import roomescape.config.TestTimeConfig;
import roomescape.reservation.presentation.dto.ReservationChangeRequest;
import roomescape.reservation.presentation.dto.ReservationRequest;
import roomescape.theme.presentation.dto.ThemeRequest;
import roomescape.time.presentation.dto.ReservationTimeRequest;

@Import(TestTimeConfig.class)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
class ReservationControllerTest {

    @Autowired
    private Clock clock;

    @Test
    @DisplayName("예약 생성시 비어있는 body를 보내면 400 Bad Request가 발생한다.")
    void createReservation_MissingParameter_Throws400() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body("{}")
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("예약 후 날짜 변경 요청을 하면 정상 변경된다.")
    void createReservation_changTime_200() {
        ReservationTimeRequest timeRequest = new ReservationTimeRequest(
                LocalTime.now(clock)
        );
        long timeId = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(timeRequest)
                .when().post("/times")
                .then().log().all()
                .statusCode(201)
                .extract().jsonPath().getLong("id");
        ThemeRequest themeRequest = ThemeRequest.builder()
                .name("판타지")
                .durationTime(LocalTime.now(clock))
                .description("판타지래요")
                .thumbnailImageUrl("https://~~~")
                .build();
        long themeId = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(themeRequest)
                .when().post("/admin/themes")
                .then().log().all()
                .statusCode(201)
                .extract().jsonPath().getLong("id");

        ReservationRequest reservationRequest = new ReservationRequest(
                "포비",
                LocalDate.now(clock),
                timeId,
                themeId
        );
        long reservationId = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationRequest)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .extract().jsonPath().getLong("id");
        ReservationChangeRequest reservationChangeRequest = new ReservationChangeRequest(
                reservationRequest.name(),
                LocalDate.now(clock).plusDays(1),
                timeId,
                themeId
        );
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationChangeRequest)
                .when().patch("/reservations/"+ reservationId)
                .then().log().all()
                .statusCode(200);
    }
}
