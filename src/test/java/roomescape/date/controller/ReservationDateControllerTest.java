package roomescape.date.controller;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static roomescape.date.fixture.ReservationDateApiFixture.createReservationDate;
import static roomescape.date.fixture.ReservationDateApiFixture.updateDateStatus;

import io.restassured.RestAssured;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql(scripts = "classpath:truncate.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ReservationDateControllerTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("사용자는 예약 가능한 날짜 목록을 조회한다.")
    void getReservationDates() {
        RestAssured.given().log().all()
                .when().get("/member/dates")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }

    @Test
    @DisplayName("사용자는 오늘 이후의 예약 날짜 목록을 조회한다.")
    void getReservationDatesAfterToday() {
        String tomorrow = LocalDate.now().plusDays(1).toString();
        Integer id = createReservationDate(tomorrow);
        updateDateStatus(id, true);

        RestAssured.given().log().all()
                .when().get("/member/dates")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Test
    @DisplayName("사용자는 오늘 이전의 예약 날짜는 조회할 수 없다.")
    @Sql(
            scripts = "classpath:past-reservation-date.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    void getReservationDatesExcludePastDates() {
        String tomorrow = LocalDate.now().plusDays(1).toString();
        Integer futureDateId = createReservationDate(tomorrow);
        updateDateStatus(futureDateId, true);

        RestAssured.given().log().all()
                .when().get("/member/dates")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1))
                .body("date", contains(tomorrow));
    }

}
