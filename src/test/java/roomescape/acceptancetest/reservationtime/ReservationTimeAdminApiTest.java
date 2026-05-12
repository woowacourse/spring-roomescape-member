package roomescape.acceptancetest.reservationtime;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import roomescape.acceptancetest.RoomecapeAcceptanceTest;
import roomescape.acceptancetest.fixture.AcceptanceTestFixture;

@RoomecapeAcceptanceTest
class ReservationTimeAdminApiTest {

    @Autowired
    private AcceptanceTestFixture acceptanceTestFixture;

    @Test
    @DisplayName("테마 기준 예약 조회")
    void findReservationTimes() {
        // given
        acceptanceTestFixture.createTheme("미술관의 밤", "aa", "aa");
        acceptanceTestFixture.createReservationTime("10:00", 1L);

        //when & then
        RestAssured.given().log().all()
                .when().get("/admin/themes/1/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Test
    @DisplayName("예약 시간 삭제")
    void deleteReservationTime() {
        // given
        acceptanceTestFixture.createTheme("미술관의 밤", "aa", "aa");
        acceptanceTestFixture.createReservationTime("10:00", 1L);

        //when & then
        RestAssured.given().log().all()
                .when().delete("/admin/themes/1/times/1")
                .then().log().all()
                .statusCode(204);
    }

    @Test
    @DisplayName("예약 시간 삭제 시 예약이 존재하는 경우 예외")
    void deleteReservationTime_ExistingReservations() {
        // given
        acceptanceTestFixture.createTheme("미술관의 밤", "aa", "aa");
        acceptanceTestFixture.createReservationTime("10:00", 1L);
        acceptanceTestFixture.createReservation("브라운", acceptanceTestFixture.reservationDate(), 1L);

        //when & then
        RestAssured.given().log().all()
                .when().delete("/admin/themes/times/1")
                .then().log().all()
                .statusCode(409);
    }

    @Test
    @DisplayName("예약 시간 생성")
    void createReservationTime() {
        // given
        acceptanceTestFixture.createTheme("미술관의 밤", "aa", "aa");
        Map<String, String> request = new HashMap<>();
        request.put("startAt", "10:00");

        //when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/admin/themes/1/times")
                .then().log().all()
                .statusCode(201)
                .body("id", is(1));
    }

    @Test
    @DisplayName("예약 시간 생성 시 중복된 시간이 존재하는 경우 예외")
    void createReservationTime_DuplicateTime() {
        // given
        acceptanceTestFixture.createTheme("미술관의 밤", "aa", "aa");
        Map<String, String> request = new HashMap<>();
        request.put("startAt", "10:00");

        // when
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/admin/themes/1/times")
                .then().log().all()
                .statusCode(201);

        // then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/admin/themes/1/times")
                .then().log().all()
                .statusCode(409);
    }

}
