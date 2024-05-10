package roomescape.controller;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import roomescape.dto.reservationtime.ReservationTimeCreateRequest;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql(value = "classpath:clean_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ReservationTimeControllerTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @TestFactory
    @DisplayName("예약 시간을 생성하고 조회하고 삭제한다.")
    Collection<DynamicTest> createAndReadAndDelete() {
        ReservationTimeCreateRequest params = ReservationTimeCreateRequest.from("12:12");

        return List.of(
                dynamicTest("예약 시간을 생성한다.", () ->
                        RestAssured.given().log().all()
                                .contentType(ContentType.JSON)
                                .body(params)
                                .when().post("/times")
                                .then().log().all()
                                .statusCode(201)
                                .header("Location", is("/times/1"))
                ),
                dynamicTest("중복된 예약 시간을 생성하려 시도하면 Bad Request status를 응답한다.", () -> {
                    ReservationTimeCreateRequest duplicated = ReservationTimeCreateRequest.from("12:12");

                    RestAssured.given().log().all()
                            .contentType(ContentType.JSON)
                            .body(duplicated)
                            .when().post("/times")
                            .then().log().all()
                            .statusCode(400);
                }),
                dynamicTest("저장된 모든 예약 시간을 조회한다.", () -> {
                    jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES ('12:22')");

                    RestAssured.given().log().all()
                            .contentType(ContentType.JSON)
                            .when().get("/times")
                            .then().log().all()
                            .statusCode(200)
                            .body("size()", is(2));
                }),
                dynamicTest("예약 시간을 삭제한다.", () ->
                        RestAssured.given().log().all()
                                .when().delete("/times/1")
                                .then().log().all()
                                .statusCode(204)
                ),
                dynamicTest("존재하지 않는 예약 시간을 삭제하려고 시도하면 Bad Request status를 응답한다.", () ->
                        RestAssured.given().log().all()
                                .when().delete("/times/1")
                                .then().log().all()
                                .statusCode(400)
                ));
    }

    @Nested
    @DisplayName("유효하지 않은 값으로 예약 시간 생성 시도 테스트")
    class InvalidRequest {

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" ", "   "})
        @DisplayName("예약 시간이 공백이면 Bad Request status를 응답한다.")
        void createByNullOrEmpty(String startAt) {
            ReservationTimeCreateRequest params = ReservationTimeCreateRequest.from(startAt);

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(params)
                    .when().post("/times")
                    .then().log().all()
                    .statusCode(400);
        }

        @ParameterizedTest
        @ValueSource(strings = {"12-12", "1212"})
        @DisplayName("예약 시간 형식이 HH:mm이 아니면 Bad Request status를 응답한다.")
        void createByInvalidTimeFormat(String startAt) {
            ReservationTimeCreateRequest params = ReservationTimeCreateRequest.from(startAt);

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(params)
                    .when().post("/times")
                    .then().log().all()
                    .statusCode(400);
        }
    }
}
