package roomescape.core.controller;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import roomescape.core.dto.reservationtime.ReservationTimeRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(properties = {"spring.config.location = classpath:application-test.yml"})
class ReservationTimeControllerTest {
    private static final String TOMORROW_DATE = LocalDate.now().plusDays(1).format(DateTimeFormatter.ISO_DATE);

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "10:89"})
    @DisplayName("시간 생성 시, startAt 값의 형식이 올바르지 않으면 예외가 발생한다.")
    void validateTimeCreateWithEmpty(final String startAt) {
        ReservationTimeRequest request = new ReservationTimeRequest(startAt);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/times")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("전체 시간 목록을 조회한다.")
    void findAll() {
        RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(3));
    }

    @Test
    @DisplayName("날짜와 테마 정보가 주어지면 예약 가능한 시간 목록을 조회한다.")
    void findBookable() {
        RestAssured.given().log().all()
                .when().get("/times?date=" + TOMORROW_DATE + "&theme=1")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(3));
    }

    @Test
    @DisplayName("시간 삭제 시, 해당 시간을 참조하는 예약이 있으면 예외가 발생한다.")
    void validateTimeDelete() {
        RestAssured.given().log().all()
                .when().delete("/times/1")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("시간 삭제 시, 해당 시간을 참조하는 예약이 없으면 삭제된다.")
    void deleteTime() {
        ReservationTimeRequest request = new ReservationTimeRequest("10:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/times")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .when().delete("/times/4")
                .then().log().all()
                .statusCode(204);
    }

    @Test
    @DisplayName("시간 생성 시, startAt 값이 중복되면 예외가 발생한다.")
    void validateTimeDuplicated() {
        ReservationTimeRequest request = new ReservationTimeRequest("10:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/times")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/times")
                .then().log().all()
                .statusCode(400);
    }
}
