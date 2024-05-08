package roomescape.endpoint;

import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.http.ContentType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import roomescape.controller.exception.CustomExceptionResponse;
import roomescape.dto.AvailableReservationTimeResponse;
import roomescape.dto.ReservationTimeRequest;
import roomescape.dto.ReservationTimeResponse;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertAll;
import static roomescape.endpoint.PreInsertedData.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(locations = "classpath:application-test.properties")
class ReservationTimeEndPointTest {

    @DisplayName("예약 시간 목록 조회")
    @Test
    void getReservationTimes_success() {
        TypeRef<List<ReservationTimeResponse>> reservationTimesFormat = new TypeRef<>() {
        };

        RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().as(reservationTimesFormat);
    }

    @DisplayName("예약 시간 추가")
    @Test
    void addReservationTime_success() {
        ReservationTimeRequest reservationTimeRequest = new ReservationTimeRequest(LocalTime.parse("12:00:00"));

        RestAssured.given().log().ifValidationFails()
                .contentType(ContentType.JSON)
                .body(reservationTimeRequest)
                .when().post("/times")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .header("location", containsString("/times/"))
                .extract().as(ReservationTimeResponse.class);
    }

    @DisplayName("존재하는 예약 시간 삭제")
    @Test
    void deleteReservationTime_forExist_success() {
        long existReservationTimeId = preInsertedReservationTime1.getId();

        RestAssured.given().log().all()
                .when().delete("/times/" + existReservationTimeId)
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("존재하지 않는 예약 시간 삭제 - 예외 발생")
    @Test
    void deleteReservationTime_forNonExist_fail() {
        long notExistTimeId = 0L;

        CustomExceptionResponse response = RestAssured.given().log().all()
                .when().delete("/times/" + notExistTimeId)
                .then().log().all()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .extract().as(CustomExceptionResponse.class);

        assertAll(
                () -> assertThat(response.title()).contains("리소스를 찾을 수 없습니다."),
                () -> assertThat(response.detail()).contains("아이디에 해당하는 예약 시간을 찾을 수 없습니다.")
        );
    }

    @DisplayName("예약이 있는 예약 시간 삭제 - 예외 발생")
    @Test
    void deleteReservationTime_whenReservationExist_fail() {
        long reservationTimeId = preInsertedReservationTime2.getId();

        CustomExceptionResponse response = RestAssured.given().log().all()
                .when().delete("/times/" + reservationTimeId)
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .extract().as(CustomExceptionResponse.class);

        assertAll(
                () -> assertThat(response.title()).contains("허용되지 않는 작업입니다."),
                () -> assertThat(response.detail()).contains("해당 시간에 예약이 존재하기 때문에 삭제할 수 없습니다.")
        );
    }

    @DisplayName("날짜와 테마에 해당하는 예약 시간 목록 조회")
    @Test
    void getAvailableTimes_success() {
        LocalDate date = preInsertedReservation.getDate();
        long themeId = preInsertedReservation.getTheme().getId();

        TypeRef<List<AvailableReservationTimeResponse>> availableTimesFormat = new TypeRef<>() {
        };
        List<AvailableReservationTimeResponse> availableReservationTimeResponses = RestAssured.given().log().all()
                .when().get("/times/available?date=" + date + "&themeId=" + themeId)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().as(availableTimesFormat);

        Assertions.assertThat(availableReservationTimeResponses).containsExactlyInAnyOrder(
                AvailableReservationTimeResponse.from(preInsertedReservationTime1, false),
                AvailableReservationTimeResponse.from(preInsertedReservationTime2, true)
        );
    }
}
