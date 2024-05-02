package roomescape.acceptance;


import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.DynamicTest.dynamicTest;

public class ReservationTimeAcceptanceTest extends BasicAcceptanceTest {
    @TestFactory
    @DisplayName("동일한 예약 시간을 두번 추가하면, 예외가 발생한다")
    Stream<DynamicTest> reservationsTest() {
        return Stream.of(
                dynamicTest("예약 시간을 추가한다 (10:00)", () -> postReservation(201)),
                dynamicTest("동일한 예약 시간을 추가한다 (10:00)", () -> postReservation(400))
        );
    }

    private void postReservation(int expectedHttpCode) {
        Map<?, ?> requestBody = Map.of("startAt", "10:10");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when().post("/times")
                .then().log().all()
                .statusCode(expectedHttpCode);
    }
}
