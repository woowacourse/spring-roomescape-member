package roomescape.acceptance;


import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import roomescape.web.dto.ReservationTimeListResponse;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

public class ReservationTimeAcceptanceTest extends BasicAcceptanceTest {
    @DisplayName("동일한 예약 시간을 두번 추가하면, 예외가 발생한다")
    @TestFactory
    Stream<DynamicTest> duplicateReservationTest() {
        return Stream.of(
                dynamicTest("예약 시간을 추가한다 (10:00)", () -> postReservation("10:00", 201)),
                dynamicTest("동일한 예약 시간을 추가한다 (10:00)", () -> postReservation("10:00", 400))
        );
    }

    @DisplayName("3개의 예약 시간을 추가한다")
    @TestFactory
    Stream<DynamicTest> reservationPostTest() {
        return Stream.of(
                dynamicTest("예약 시간을 추가한다 (10:00)", () -> postReservation("10:00", 201)),
                dynamicTest("예약 시간을 추가한다 (11:00)", () -> postReservation("11:00", 201)),
                dynamicTest("예약 시간을 추가한다 (12:00)", () -> postReservation("12:00", 201)),
                dynamicTest("모든 예약 시간을 조회한다 (총 3개)", () -> getReservations(200, 3))
        );
    }

    @DisplayName("예약 시간을 추가하고 삭제한다")
    @TestFactory
    Stream<DynamicTest> reservationPostAndDeleteTest() {
        AtomicLong reservationId = new AtomicLong();

        return Stream.of(
                dynamicTest("예약 시간을 추가한다 (10:00)", () -> {
                    long id = postReservation("10:00", 201);
                    reservationId.set(id);
                }),
                dynamicTest("예약 시간을 삭제한다 (10:00)", () -> deleteReservation(reservationId.longValue(), 204)),
                dynamicTest("예약 시간을 추가한다 (10:00)", () -> postReservation("10:00", 201)),
                dynamicTest("모든 예약 시간을 조회한다 (총 1개)", () -> getReservations(200, 1))
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

    private void getReservations(int expectedHttpCode, int expectedReservationsSize) {
        Response response = RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(expectedHttpCode)
                .extract().response();

        ReservationTimeListResponse reservationTimeListResponses = response.as(ReservationTimeListResponse.class);

        assertThat(reservationTimeListResponses.getReservationTimes()).hasSize(expectedReservationsSize);
    }

    private void deleteReservation(long reservationId, int expectedHttpCode) {
        RestAssured.given().log().all()
                .when().delete("/times/" + reservationId)
                .then().log().all()
                .statusCode(expectedHttpCode);
    }
}
