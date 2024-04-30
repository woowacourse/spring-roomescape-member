package roomescape.acceptance;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

class ReservationAcceptanceTest extends BasicAcceptanceTest {
    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @DisplayName("예약 관리 페이지에 접속해서, 모든 예약을 조회한다")
    @TestFactory
    Stream<DynamicTest> reservationsTest() {
        return Stream.of(
                dynamicTest("예약 관리 페이지에 접속한다", this::accessReservationPage),
                dynamicTest("모든 예약을 조회한다 (총 0개)", () -> checkReservationCount(0))
        );
    }

    @DisplayName("예약 관리 페이지에 접속해서, 예약 시간과 예약을 추가하고 삭제한다")
    @TestFactory
    Stream<DynamicTest> reservationAddRemoveTest() {
        return Stream.of(
                dynamicTest("예약 시간 관리 페이지에 접속한다", this::accessReservationTimePage),
                dynamicTest("예약 시간을 추가한다", this::addReservationTime),
                dynamicTest("예약을 추가한다", this::addReservation),
                dynamicTest("모든 예약을 조회한다 (총 1개)", () -> checkReservationCount(1)),
                dynamicTest("추가한 예약을 삭제한다", this::deleteReservation),
                dynamicTest("모든 예약을 조회한다 (총 0개)", () -> checkReservationCount(0))
        );
    }

    private void accessReservationPage() {
        RestAssured.given().log().all()
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(200);
    }

    private void accessReservationTimePage() {
        RestAssured.given().log().all()
                .when().get("/admin/time")
                .then().log().all()
                .statusCode(200);
    }

    private void addReservation() {
        List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();
        long reservationTimeId = reservationTimes.get(0).getId();

        Map<?, ?> params = Map.ofEntries(
                Map.entry("name", "브라운"),
                Map.entry("date", LocalDate.now().plusDays(1)),
                Map.entry("timeId", reservationTimeId));

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);
    }

    private void addReservationTime() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(Map.of("startAt", "10:00"))
                .when().post("/times")
                .then().log().all()
                .statusCode(200);
    }

    private void checkReservationCount(int expectedCount) {
        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(expectedCount));
    }

    private void deleteReservation() {
        List<Reservation> reservations = reservationRepository.findAll();
        long reservationId = reservations.get(0).getId();

        RestAssured.given().log().all()
                .when().delete("/reservations/" + reservationId)
                .then().log().all()
                .statusCode(204);
    }
}
