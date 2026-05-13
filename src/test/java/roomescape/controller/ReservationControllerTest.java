package roomescape.controller;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.dto.ReservationRequest;
import roomescape.exception.ErrorCode;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationControllerTest {

    @Test
    public void 전체_예약_조회_API() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().get("admin/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(14));
    }

    @Test
    public void 예약_삭제_API() {
        ReservationRequest reservationRequest = new ReservationRequest("포비", LocalDate.now().plusDays(1L), 1L, 1L);
        int id = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(reservationRequest)
                .when().post("/reservations")
                .then().statusCode(201)
                .extract().path("id");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().delete("admin/reservations/" + id)
                .then().log().all()
                .statusCode(204);
    }

    @Test
    public void 예약_생성_API() {
        ReservationRequest reservationRequest = new ReservationRequest("포비", LocalDate.now().plusDays(1L), 2L, 2L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationRequest)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .body("size()", is(5));
    }

    @Test
    public void 사용자_이름_예약_조회_API() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().get("/reservations?username=토리")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }


    @ParameterizedTest
    @MethodSource("emptyReservationRequest")
    public void 예약_생성_시_필드가_빈값이면_400을_반환한다(ReservationRequest reservationRequest) {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationRequest)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }


    @Test
    public void 예약_생성_시_존재하지_않는_예약_시간으로_예약하는_경우_404를_반환한다() {
        ReservationRequest reservationRequest = new ReservationRequest("토리임", LocalDate.now().plusDays(1L), 20L, 1L);
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationRequest)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(404)
                .body("message", is(ErrorCode.TIME_NOT_FOUND.getMessage()));
    }

    @Test
    public void 예약_생성_시_존재하지_않는_테마로_예약하는_경우_404를_반환한다() {
        ReservationRequest reservationRequest = new ReservationRequest("토리임", LocalDate.now().plusDays(1L), 1L, -1L);
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationRequest)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(404)
                .body("message", is(ErrorCode.THEME_NOT_FOUND.getMessage()));
    }

    @Test
    public void 예약_생성_시_이미_지난_날짜_시간으로_예약하는_경우_422를_반환한다() {
        ReservationRequest reservationRequest = new ReservationRequest("토리임", LocalDate.now().minusDays(1L), 1L, 1L);
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationRequest)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(422)
                .body("message", is(ErrorCode.RESERVATION_PAST_DATE.getMessage()));
    }

    @Test
    public void 사용자_예약_취소_시_존재하지_않는_예약이면_404를_반환한다() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .delete("/reservations/-1?username=토리")
                .then().log().all()
                .statusCode(404)
                .body("message", is(ErrorCode.RESERVATION_NOT_FOUND.getMessage()));
    }

    @Test
    public void 사용자_예약_취소_시_다른_사람의_예약이면_422를_반환한다() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .delete("/reservations/1?username=페이커")
                .then().log().all()
                .statusCode(422)
                .body("message", is(ErrorCode.RESERVATION_NOT_USER_CANCEL.getMessage()));
    }

    @Test
    public void 이미_지난_예약_취소_시_422를_반환한다() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .delete("admin/reservations/4")
                .then().log().all()
                .statusCode(422)
                .body("message", is(ErrorCode.RESERVATION_PAST_CANCEL.getMessage()));
    }

    private static Stream<ReservationRequest> emptyReservationRequest() {
        return Stream.of(
                new ReservationRequest("", LocalDate.of(2026, 5, 11), 1L, 1L),
                new ReservationRequest("안녕하세요토리입니다테스트작성을위해서늘려쓰고있는닉네임입니다", LocalDate.of(2026, 5, 11), 1L, 1L),
                new ReservationRequest("토리", null, 1L, 1L),
                new ReservationRequest("토리", LocalDate.of(2026, 5, 11), null, 1L),
                new ReservationRequest("토리", LocalDate.of(2026, 5, 11), 1L, null)
        );
    }
}
