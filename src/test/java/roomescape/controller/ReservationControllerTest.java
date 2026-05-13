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
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().delete("admin/reservations/1")
                .then().log().all()
                .statusCode(204);
    }

    @Test
    public void 예약_생성_API() {
        ReservationRequest reservationRequest = new ReservationRequest("포비", LocalDate.of(2026, 5, 6), 2L, 2L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationRequest)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .body("size()", is(5));
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
