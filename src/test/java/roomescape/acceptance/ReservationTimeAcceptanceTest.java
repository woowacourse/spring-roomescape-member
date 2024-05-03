package roomescape.acceptance;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.test.context.jdbc.Sql;
import roomescape.controller.response.AvailableReservationTimeResponse;

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
                dynamicTest("모든 예약 시간을 조회한다 (총 3개)", () -> getReservationTimes(200, 3))
        );
    }

    @TestFactory
    @DisplayName("예약 시간을 추가하고 삭제한다")
    Stream<DynamicTest> reservationPostAndDeleteTest() {
        AtomicLong reservationId = new AtomicLong();

        return Stream.of(
                dynamicTest("예약 시간을 추가한다 (10:00)", () -> {
                    long id = postReservation("10:00", 201);
                    reservationId.set(id);
                }),
                dynamicTest("예약 시간을 삭제한다 (10:00)", () -> deleteReservation(reservationId.longValue(), 204)),
                dynamicTest("예약 시간을 추가한다 (10:00)", () -> postReservation("10:00", 201)),
                dynamicTest("모든 예약 시간을 조회한다 (총 1개)", () -> getReservationTimes(200, 1))
        );
    }

    @TestFactory
    @Sql("/init-for-available-time.sql")
    @DisplayName("예약이 가능한 시간을 구분하여 반환한다.")
    Stream<DynamicTest> res() {
        return Stream.of(
                dynamicTest("모든 예약된 시간을 조회한다 (총 3개)", () -> getAvailableTimes(200))
        );
    }

    private long postReservation(String time, int expectedHttpCode) {
        Map<?, ?> requestBody = Map.of("startAt", time);

        Response response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when().post("/times")
                .then().log().all()
                .statusCode(expectedHttpCode)
                .extract().response();

        if (expectedHttpCode == 201) {
            return response.jsonPath().getLong("id");
        }

        return 0L;
    }

    private void getReservationTimes(int expectedHttpCode, int expectedReservationsSize) {
        Response response = RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(expectedHttpCode)
                .extract().response();

        List<?> reservationTimeResponses = response.as(List.class);

        assertThat(reservationTimeResponses).hasSize(expectedReservationsSize);
    }

    private void deleteReservation(long reservationId, int expectedHttpCode) {
        RestAssured.given().log().all()
                .when().delete("/times/" + reservationId)
                .then().log().all()
                .statusCode(expectedHttpCode);
    }

    private void getAvailableTimes(int expectedHttpCode) {
        Response response = RestAssured.given().log().all()
                .when().get("/times/available?date=2099-04-29&themeId=1")
                .then().log().all()
                .statusCode(expectedHttpCode)
                .extract().response();

        List<AvailableReservationTimeResponse> reservationTimeResponses = response.jsonPath().getList(".", AvailableReservationTimeResponse.class);

        List<AvailableReservationTimeResponse> list = reservationTimeResponses.stream()
                .filter(AvailableReservationTimeResponse::alreadyBooked)
                .toList();

        assertThat(list).hasSize(3);
    }
}
