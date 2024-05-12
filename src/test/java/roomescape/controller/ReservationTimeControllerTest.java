package roomescape.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.domain.ReservationTime;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationTimeControllerTest {

    @LocalServerPort
    int serverPort;

    @BeforeEach
    public void beforeEach() {
        RestAssured.port = serverPort;
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @DisplayName("DB에서 예약 시간 목록 조회 API 작동을 확인한다")
    void checkAllReservations() {
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", "15:40");

        List<ReservationTime> reservationTimes = RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(200).extract()
                .jsonPath().getList(".", ReservationTime.class);

        Integer count = jdbcTemplate.queryForObject("SELECT count(*) from reservation_time", Integer.class);

        assertThat(reservationTimes.size()).isEqualTo(count);
    }

    @TestFactory
    @DisplayName("DB에서 예약 시간 추가와 삭제의 작동을 확인한다")
    Stream<DynamicTest> reservationCreateAndDelete() {
        Map<String, String> params = Map.of(
                "startAt", "10:00"
        );

        return Stream.of(
                dynamicTest("예약 시간을 추가한다", () -> {
                    RestAssured.given().log().all()
                            .contentType(ContentType.JSON)
                            .body(params)
                            .when().post("/times")
                            .then().log().all()
                            .statusCode(201);
                }),

                dynamicTest("예약 시간이 정상적으로 추가되었는지 확인한다", () -> {
                    Integer count = jdbcTemplate.queryForObject("SELECT count(*) from reservation_time", Integer.class);
                    assertThat(count).isEqualTo(1);
                }),

                dynamicTest("id가 1인 예약을 삭제한다", () -> {
                    RestAssured.given().log().all()
                            .when().delete("/times/1")
                            .then().log().all()
                            .statusCode(204);
                }),

                dynamicTest("예약이 정상적으로 삭제되었는지 확인한다", () -> {
                    Integer countAfterDelete = jdbcTemplate.queryForObject("SELECT count(*) from reservation_time",
                            Integer.class);
                    assertThat(countAfterDelete).isEqualTo(0);
                })
        );
    }

    @TestFactory
    @DisplayName("중복된 예약 시간 추가가 불가능한지 확인한다.")
    Stream<DynamicTest> checkDuplicatedReservationTime() {
        Map<String, String> params1 = Map.of(
                "startAt", "10:00"
        );

        Map<String, String> params2 = Map.of(
                "startAt", "10:00"
        );

        return Stream.of(
                dynamicTest("예약 시간을 추가한다", () -> {
                    RestAssured.given().log().all()
                            .contentType(ContentType.JSON)
                            .body(params1)
                            .when().post("/times")
                            .then().log().all()
                            .statusCode(201);
                }),

                dynamicTest("중복된 예약 시간을 추가한다", () -> {
                    RestAssured.given().log().all()
                            .contentType(ContentType.JSON)
                            .body(params2)
                            .when().post("/times")
                            .then().log().all()
                            .statusCode(400);
                })
        );
    }

    @Test
    @DisplayName("존재하지 않는 예약 시간의 삭제가 불가능한지 확인한다")
    void checkNotExistReservationTimeDelete() {
        RestAssured.given().log().all()
                .when().delete("times/1")
                .then().log().all()
                .statusCode(400);
    }
}
