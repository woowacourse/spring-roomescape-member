package roomescape.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Stream;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;
import roomescape.dto.ReservationTimeRequest;
import roomescape.dto.ReservationTimeResponse;

class ReservationTimeControllerTest extends BaseControllerTest {

    @TestFactory
    @DisplayName("예약 시간을 생성, 조회, 삭제한다.")
    Stream<DynamicTest> reservationTimeControllerTests() {
        return Stream.of(
                DynamicTest.dynamicTest("예약 시간을 생성한다.", this::addReservationTime),
                DynamicTest.dynamicTest("예약 시간을 모두 조회한다.", this::getAllReservationTimes),
                DynamicTest.dynamicTest("예약 시간을 삭제한다.", this::deleteReservationTimeById)
        );
    }

    void addReservationTime() {
        ReservationTimeRequest request = new ReservationTimeRequest(LocalTime.of(10, 30));

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/times")
                .then().log().all()
                .extract();

        ReservationTimeResponse reservationTimeResponse = response.as(ReservationTimeResponse.class);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
            softly.assertThat(response.header("Location")).isEqualTo("/times/1");
            softly.assertThat(reservationTimeResponse).isEqualTo(new ReservationTimeResponse(1L, LocalTime.of(10, 30)));
        });
    }

    void getAllReservationTimes() {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .extract();

        List<ReservationTimeResponse> reservationTimeResponses = response.jsonPath()
                .getList(".", ReservationTimeResponse.class);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            softly.assertThat(reservationTimeResponses).hasSize(1);
            softly.assertThat(reservationTimeResponses)
                    .containsExactly(new ReservationTimeResponse(1L, LocalTime.of(10, 30)));
        });
    }

    void deleteReservationTimeById() {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().delete("/times/1")
                .then().log().all()
                .extract();

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        });
    }
}
