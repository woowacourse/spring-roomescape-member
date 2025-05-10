package roomescape.reservation.acceptance;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.reservation.entity.ReservationTime;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationTimeAcceptanceTest {

    @Test
    @Disabled
    @DisplayName("시간을 조회하는 API를 요청한다.")
    void getReservationTimes() {
        // given
        var params = Map.of(
                "startAt", "10:00"
        );

        RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times");

        // when
        List<ReservationTime> reservationTimes = RestAssured
                .given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(200).extract()
                .jsonPath().getList(".", ReservationTime.class);

        // then
        ReservationTime expected = new ReservationTime(
                1L,
                LocalTime.of(10, 0)
        );

        assertThat(reservationTimes.getFirst()).isEqualTo(expected);
        assertThat(reservationTimes.size()).isEqualTo(1);
    }

    @Test
    @Disabled
    @DisplayName("시간을 생성하는 API를 요청한다.")
    void createReservationTime() {
        // given
        var params = Map.of(
                "startAt", "10:00"
        );

        // when
        RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times");

        // then
        List<ReservationTime> reservationTimes = RestAssured
                .given()
                .when().get("/times")
                .then().extract()
                .jsonPath().getList(".", ReservationTime.class);

        ReservationTime expected = new ReservationTime(
                1L,
                LocalTime.of(10, 0)
        );

        assertThat(reservationTimes.getFirst()).isEqualTo(expected);
        assertThat(reservationTimes.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("시간을 삭제하는 API를 요청한다.")
    void deleteReservationTime() {
        // given
        var params = Map.of(
                "startAt", "10:00"
        );

        RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times");

        // when
        RestAssured
                .given().log().all()
                .when().delete("/times/1");

        // then
        List<ReservationTime> reservationTimes = RestAssured
                .given()
                .when().get("/times")
                .then().extract()
                .jsonPath().getList(".", ReservationTime.class);

        assertThat(reservationTimes.size()).isEqualTo(0);
    }


    @Test
    @DisplayName("이미 예약이 존재하는 시간은 삭제할 수 없다.")
    void deleteTimeExistReservation() {
        // given
        Map<String, String> timeParams = new HashMap<>();
        timeParams.put("startAt", "10:00");

        Map<String, String> themeParams = Map.of(
                "name", "theme",
                "description", "hi",
                "thumbnail", "hello"
        );

        Map<String, Object> reservationParams = Map.of(
                "name", "test",
                "date", LocalDate.now().plusDays(1).toString(),
                "timeId", 1,
                "themeId", 1
        );

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(timeParams)
                .when().post("/times");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(themeParams)
                .when().post("/themes");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationParams)
                .when().post("/reservations");

        // when & then
        RestAssured.given().log().all()
                .when().delete("/times/1")
                .then().log().all()
                .statusCode(400);
    }
}
