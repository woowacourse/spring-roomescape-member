package roomescape.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.dto.TimeResponse;
import roomescape.service.ReservationTimeService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class TimeRestControllerTest {

    @Autowired
    private ReservationTimeService reservationTimeService;

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("모든 시간을 조회한다.")
    @Test
    void getAll() {
        // when
        List<TimeResponse> reservations = RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(200).extract()
                .jsonPath().getList(".", TimeResponse.class);

        // then
        assertThat(reservations).hasSize(10);
    }

    @DisplayName("시간을 생성한다.")
    @Test
    void create() {
        // given
        Map<String, String> params = Map.of("startAt", "20:00");

        // when
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(201);

        List<TimeResponse> allTimes = reservationTimeService.findAll();

        // then
        assertAll(
                () -> assertThat(allTimes).hasSize(11),
                () -> assertThat(allTimes).contains(new TimeResponse(11L, LocalTime.of(20,0)))
        );
    }

    @DisplayName("중복된 시간을 생성하려고 하면 BAD_REQUEST를 반환한다.")
    @Test
    void create_duplicate_badRequest() {
        // given
        Map<String, String> params = Map.of("startAt", "9:00");

        // when && then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }

    @DisplayName("해당 id의 시간을 삭제한다.")
    @Test
    void delete() {
        // when
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().delete("/times/10")
                .then().log().all()
                .statusCode(HttpStatus.SC_NO_CONTENT);

        List<TimeResponse> allTimes = reservationTimeService.findAll();

        // then
        assertAll(
                () -> assertThat(allTimes).hasSize(9),
                () -> assertThat(allTimes).doesNotContain(new TimeResponse(11L, LocalTime.of(19,0)))
        );
    }

    @DisplayName("예약이 존재하는 시간을 삭제하려고 하면 BAD_REQUEST를 반환한다.")
    @Test
    void deleteById_existReservation_badRequest() {
        // when
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().delete("/times/1")
                .then().log().all()
                .statusCode(HttpStatus.SC_BAD_REQUEST);
    }
}
