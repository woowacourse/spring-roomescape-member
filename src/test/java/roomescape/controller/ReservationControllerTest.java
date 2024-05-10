package roomescape.controller;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import roomescape.dto.member.MemberLoginRequest;
import roomescape.dto.reservation.AdminReservationCreateRequest;
import roomescape.dto.reservation.MemberReservationCreateRequest;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql(value = "classpath:clean_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ReservationControllerTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @TestFactory
    @DisplayName("예약을 생성하고 조회하고 삭제한다.")
    Collection<DynamicTest> createAndReadAndDelete() {
        String date = LocalDate.now().plusDays(1).toString();

        jdbcTemplate.update(
                "INSERT INTO member (name, email, password) VALUES ('사용자1', 'user1@wooteco.com', 'user1')");
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES ('12:12')");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES ('방탈출1', '방탈출 1번', '썸네일1')");

        jdbcTemplate.update(
                "INSERT INTO member (name, email, password) VALUES ('사용자2', 'user2@wooteco.com', 'user2')");
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES ('12:22')");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES ('방탈출2', '방탈출 2번', '썸네일2')");

        return List.of(
                dynamicTest("유저가 예약을 생성한다.", () -> {
                    MemberReservationCreateRequest params =
                            MemberReservationCreateRequest.of(date, 1L, 1L);

                    String token = RestAssured.given()
                            .body(MemberLoginRequest.of("user1", "user1@wooteco.com"))
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .when().post("/login")
                            .getHeader("Set-Cookie");

                    RestAssured.given().log().all()
                            .header("Cookie", token)
                            .contentType(ContentType.JSON)
                            .body(params)
                            .when().post("/reservations")
                            .then().log().all()
                            .statusCode(201)
                            .header("Location", "/reservations/1")
                            .body("id", is(1))
                            .body("member.name", is("사용자1"));
                }),
                dynamicTest("관리자가 예약을 생성한다.", () -> {
                    AdminReservationCreateRequest params =
                            AdminReservationCreateRequest.of(date, 2L, 1L, 2L);

                    RestAssured.given().log().all()
                            .contentType(ContentType.JSON)
                            .body(params)
                            .when().post("/reservations/admin")
                            .then().log().all()
                            .statusCode(201)
                            .header("Location", "/reservations/2")
                            .body("id", is(2))
                            .body("member.name", is("사용자2"));
                }),
                dynamicTest("모든 예약을 조회한다.", () ->
                        RestAssured.given().log().all()
                                .when().get("/reservations")
                                .then().log().all()
                                .statusCode(200)
                                .body("size()", is(2))
                ),
                dynamicTest("특정 테마의 예약 가능한 시간을 조회한다.", () ->
                        RestAssured.given().log().all()
                                .params("date", date)
                                .params("themeId", 1)
                                .when().get("/reservations/available-time")
                                .then().log().all()
                                .statusCode(200)
                                .body("size()", is(2))
                                .body("[0].alreadyBooked", is(true))
                                .body("[1].alreadyBooked", is(false))
                ),
                dynamicTest("예약을 삭제한다.", () ->
                        RestAssured.given().log().all()
                                .when().delete("/reservations/1")
                                .then().log().all()
                                .statusCode(204)
                ),
                dynamicTest("존재하지 않는 예약을 삭제하려고 시도하면 Bad Request status를 응답한다.", () ->
                        RestAssured.given().log().all()
                                .when().delete("reservations/1")
                                .then().log().all()
                                .statusCode(400)
                ));
    }

    @Nested
    @DisplayName("유효하지 않은 값으로 예약 생성 시도 테스트")
    class InvalidRequest {

        @BeforeEach
        void setUp() {
            jdbcTemplate.update(
                    "INSERT INTO member (name, email, password) VALUES ('사용자1', 'user1@wooteco.com', 'user1')");
            jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES ('12:12')");
            jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES ('방탈출1', '방탈출 1번', '썸네일1')");
        }

        @Test
        @DisplayName("지난 날짜로 예약하면 Bad Request status를 응답한다.")
        void createByPastDate() {
            String pastDate = LocalDate.now().minusDays(1).toString();
            AdminReservationCreateRequest params = AdminReservationCreateRequest.of(pastDate, 1L, 1L, 1L);

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(params)
                    .when().post("/reservations/admin")
                    .then().log().all()
                    .statusCode(400);
        }

        @Test
        @DisplayName("지난 시간으로 예약하면 Bad Request status를 응답한다.")
        void createByPastTime() {
            String pastTime = LocalTime.now().minusHours(1).toString();
            jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES ('" + pastTime + "')");
            AdminReservationCreateRequest params =
                    AdminReservationCreateRequest.of(LocalDate.now().toString(), 1L, 2L, 1L);

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(params)
                    .when().post("/reservations/admin")
                    .then().log().all()
                    .statusCode(400);
        }

        @ParameterizedTest
        @MethodSource("nullRequests")
        @DisplayName("날짜, 유저 ID, 예약 시간 ID, 테마 ID 중 하나라도 공백이면 Bad Request status를 응답한다.")
        void createByNullValue(AdminReservationCreateRequest params) {
            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(params)
                    .when().post("/reservations/admin")
                    .then().log().all()
                    .statusCode(400);
        }

        private static Stream<AdminReservationCreateRequest> nullRequests() {
            return Stream.of(
                    AdminReservationCreateRequest.of(null, 1L, 1L, 1L),
                    AdminReservationCreateRequest.of(LocalDate.now().plusDays(1).toString(), null, 1L, 1L),
                    AdminReservationCreateRequest.of(LocalDate.now().plusDays(1).toString(), 1L, null, 1L),
                    AdminReservationCreateRequest.of(LocalDate.now().plusDays(1).toString(), 1L, 1L, null)
            );
        }

        @ParameterizedTest
        @MethodSource("notExistRequests")
        @DisplayName("유저 ID, 시간 ID, 테마 ID 중 하나라도 존재하지 않으면 Bad Request status를 응답한다.")
        void createByNotExistValue(AdminReservationCreateRequest params) {
            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(params)
                    .when().post("/reservations/admin")
                    .then().log().all()
                    .statusCode(400);
        }

        private static Stream<AdminReservationCreateRequest> notExistRequests() {
            String date = LocalDate.now().plusDays(1).toString();
            return Stream.of(
                    AdminReservationCreateRequest.of(date, -1L, 1L, 1L),
                    AdminReservationCreateRequest.of(date, 1L, -1L, 1L),
                    AdminReservationCreateRequest.of(date, 1L, 1L, -1L)
            );
        }
    }
}
