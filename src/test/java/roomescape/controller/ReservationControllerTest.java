package roomescape.controller;

import static org.hamcrest.Matchers.hasItems;
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
import roomescape.dto.ReservationUpdateRequest;
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

    @Test
    public void 예약_이름으로_조회_시_해당하는_예약이_없으면_빈_리스트를_반환한다() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().get("/reservations?username=없는이름")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }

    @Test
    public void 예약_이름_파라미터_없이_조회하면_400을_반환한다() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().get("/reservations")
                .then().log().all()
                .statusCode(400);
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
    public void 예약_생성_시_검증_실패_응답은_errors_필드에_필드별_오류를_포함한다() {
        ReservationRequest invalid = new ReservationRequest("", LocalDate.now().plusDays(1), null, 1L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(invalid)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body("code", is("INVALID_CONSTRAINT"))
                .body("errors.size()", is(2))
                .body("errors.field", hasItems("name", "timeId"))
                .body("errors.message", hasItems(
                        ErrorCode.RESERVATION_NAME_BLANK.getMessage(),
                        ErrorCode.RESERVATION_TIME_NULL.getMessage()
                ));
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
    public void 사용자_예약_취소_시_경로_변수가_숫자가_아니면_400을_반환한다() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .delete("/reservations/abc?username=토리")
                .then().log().all()
                .statusCode(400)
                .body("code", is("INVALID_PARAMETER_TYPE"));
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
                .body("message", is(ErrorCode.RESERVATION_NOT_OWNER.getMessage()));
    }

    @Test
    public void 이미_지난_예약_취소_시_422를_반환한다() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .delete("admin/reservations/4")
                .then().log().all()
                .statusCode(422)
                .body("message", is(ErrorCode.RESERVATION_PAST_UPDATE.getMessage()));
    }

    @Test
    public void 예약_변경_시_정상적으로_변경되면_200을_반환한다() {
        ReservationRequest registerRequest = new ReservationRequest("포비", LocalDate.now().plusDays(1L), 1L, 1L);
        int id = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(registerRequest)
                .when().post("/reservations")
                .then().statusCode(201)
                .extract().path("id");

        ReservationUpdateRequest updateRequest = new ReservationUpdateRequest(LocalDate.now().plusDays(2L), 2L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(updateRequest)
                .when().patch("/reservations/" + id + "?username=포비")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    public void 예약_변경_시_존재하지_않는_예약이면_404를_반환한다() {
        ReservationUpdateRequest updateRequest = new ReservationUpdateRequest(LocalDate.now().plusDays(1L), 1L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(updateRequest)
                .when().patch("/reservations/-1?username=토리")
                .then().log().all()
                .statusCode(404)
                .body("message", is(ErrorCode.RESERVATION_NOT_FOUND.getMessage()));
    }

    @Test
    public void 예약_변경_시_이미_지난_예약이면_422를_반환한다() {
        ReservationUpdateRequest updateRequest = new ReservationUpdateRequest(LocalDate.now().plusDays(1L), 1L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(updateRequest)
                .when().patch("/reservations/1?username=윤기")
                .then().log().all()
                .statusCode(422)
                .body("message", is(ErrorCode.RESERVATION_PAST_UPDATE.getMessage()));
    }

    @Test
    public void 예약_변경_시_변경하려는_날짜_시간에_이미_예약이_존재하면_409를_반환한다() {
        ReservationRequest registerRequest = new ReservationRequest("포비", LocalDate.now().plusDays(1L), 1L, 1L);
        int id = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(registerRequest)
                .when().post("/reservations")
                .then().statusCode(201)
                .extract().path("id");

        ReservationRequest anotherRequest = new ReservationRequest("토리", LocalDate.now().plusDays(2L), 2L, 1L);
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(anotherRequest)
                .when().post("/reservations")
                .then().statusCode(201);

        ReservationUpdateRequest updateRequest = new ReservationUpdateRequest(LocalDate.now().plusDays(2L), 2L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(updateRequest)
                .when().patch("/reservations/" + id + "?username=포비")
                .then().log().all()
                .statusCode(409)
                .body("message", is(ErrorCode.RESERVATION_TIME_ALREADY_BOOKED.getMessage()));
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
