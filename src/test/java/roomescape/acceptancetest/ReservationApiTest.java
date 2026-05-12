package roomescape.acceptancetest;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import roomescape.acceptancetest.fixture.AcceptanceTestFixture;

@RoomecapeAcceptanceTest
class ReservationApiTest {

    @Autowired
    private AcceptanceTestFixture acceptanceTestFixture;

    @Test
    void 예약_조회() {
        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }

    @Test
    void 예약_추가_및_삭제() {
        acceptanceTestFixture.createTheme();
        acceptanceTestFixture.createReservationTime("15:40", 1L);

        Map<String, Object> reservation = acceptanceTestFixture.reservationRequest("브라운",
                acceptanceTestFixture.reservationDate(), 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .body("id", is(1));

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));

        RestAssured.given().log().all()
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(204);

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }

    @Test
    void DB_추가_삭제_API_전환() {
        acceptanceTestFixture.createTheme();
        acceptanceTestFixture.createReservationTime("10:00", 1L);

        Map<String, Object> reservation = acceptanceTestFixture.reservationRequest("브라운",
                acceptanceTestFixture.reservationDate(), 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(204);

    }

    @Test
    void 예약과_시간_연결() {
        acceptanceTestFixture.createTheme();
        acceptanceTestFixture.createReservationTime("10:00", 1L);

        Map<String, Object> reservation = acceptanceTestFixture.reservationRequest("브라운",
                acceptanceTestFixture.reservationDate(), 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

}
