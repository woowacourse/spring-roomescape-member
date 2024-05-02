package roomescape.acceptance;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

public class ReservationTimeAcceptanceTest extends BasicAcceptanceTest {
    @TestFactory
    @DisplayName("동일한 예약 시간을 두번 추가하면, 예외가 발생한다")
    Stream<DynamicTest> duplicateReservationTest() {
        return Stream.of(
                dynamicTest("예약 시간을 추가한다 (10:00)", () -> postReservation("10:00", 201)),
                dynamicTest("동일한 예약 시간을 추가한다 (10:00)", () -> postReservation("10:00", 400))
        );
    }

    @TestFactory
    @DisplayName("3개의 예약 시간을 추가한다")
    Stream<DynamicTest> reservationPostTest() {
        return Stream.of(
                dynamicTest("예약 시간을 추가한다 (10:00)", () -> postReservation("10:00", 201)),
                dynamicTest("예약 시간을 추가한다 (11:00)", () -> postReservation("11:00", 201)),
                dynamicTest("예약 시간을 추가한다 (12:00)", () -> postReservation("12:00", 201)),
                dynamicTest("모든 예약 시간을 조회한다 (총 3개)", () -> getReservations(200, 3))
        );
    }

    private void postReservation(String time, int expectedHttpCode) {
        Map<?, ?> requestBody = Map.of("startAt", time);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when().post("/times")
                .then().log().all()
                .statusCode(expectedHttpCode);
    }

    private void getReservations(int expectedHttpCode, int expectedReservationsSize) {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(expectedHttpCode)
                .extract();

        List<?> reservationTimeResponses = response.as(List.class);

        assertThat(reservationTimeResponses).hasSize(expectedReservationsSize);
    }
}
