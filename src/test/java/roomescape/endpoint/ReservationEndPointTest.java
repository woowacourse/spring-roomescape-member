package roomescape.endpoint;

import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import roomescape.controller.exception.CustomExceptionResponse;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.ReservationRequest;
import roomescape.dto.ReservationResponse;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertAll;
import static roomescape.endpoint.PreInsertedData.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(locations = "classpath:application-test.properties")
class ReservationEndPointTest {

    @DisplayName("예약 목록 조회")
    @Test
    void getReservations_success() {
        TypeRef<List<ReservationResponse>> reservationListFormat = new TypeRef<>() {
        };

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().as(reservationListFormat);
    }

    @DisplayName("예약 추가")
    @Test
    void addReservation_success() {
        long reservationTimeId = preInsertedReservationTime1.getId();
        long themeId = preInsertedTheme1.getId();
        ReservationRequest requestBody = new ReservationRequest(
                "이름",
                LocalDate.parse("2099-01-11"),
                reservationTimeId,
                themeId);

        RestAssured.given().log().ifValidationFails()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .header("location", containsString("/reservations/"))
                .extract().as(ReservationResponse.class);
    }

    @DisplayName("존재하는 예약 삭제")
    @Test
    void deleteReservation_forExist_success() {
        Long existReservationId = preInsertedReservation.getId();

        RestAssured.given().log().all()
                .when().delete("/reservations/" + existReservationId)
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("존재하지 않는 예약 삭제 - 예외 발생")
    @Test
    void deleteReservation_forNonExist_fail() {
        long notExistReservationId = 0L;

        CustomExceptionResponse response = RestAssured.given().log().all()
                .when().delete("/reservations/" + notExistReservationId)
                .then().log().all()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .extract().as(CustomExceptionResponse.class);

        assertAll(
                () -> assertThat(response.title()).contains("리소스를 찾을 수 없습니다."),
                () -> assertThat(response.detail()).contains("아이디에 해당하는 예약을 찾을 수 없습니다.")
        );
    }

    @DisplayName("과거 시간에 대한 예약 추가 - 예외 발생")
    @Test
    void addReservation_forPastTime_fail() {
        long reservationTimeId = preInsertedReservationTime1.getId();
        long themeId = preInsertedTheme1.getId();
        ReservationRequest reservationForPast = new ReservationRequest(
                "알파카",
                LocalDate.now().minusDays(1),
                reservationTimeId,
                themeId
        );

        CustomExceptionResponse response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationForPast)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract().as(CustomExceptionResponse.class);

        assertAll(
                () -> assertThat(response.title()).contains("허용되지 않는 작업입니다."),
                () -> assertThat(response.detail()).contains("지나간 시간에 대한 예약은 할 수 없습니다.")
        );
    }

    @DisplayName("이미 예약이 존재하는 날짜,시간,테마에 예약 추가 - 예외 발생")
    @Test
    void addReservation_alreadyExist_fail() {
        ReservationTime time = preInsertedReservation.getTime();
        Theme theme = preInsertedReservation.getTheme();
        ReservationRequest duplicatedRequest = new ReservationRequest(
                "이름",
                LocalDate.parse("2099-12-31"),
                time.getId(),
                theme.getId());

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(duplicatedRequest)
                .when().post("/reservations");
        CustomExceptionResponse response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(duplicatedRequest)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract().as(CustomExceptionResponse.class);

        assertAll(
                () -> assertThat(response.title()).contains("허용되지 않는 작업입니다."),
                () -> assertThat(response.detail()).contains("예약이 이미 존재합니다.")
        );
    }
}
