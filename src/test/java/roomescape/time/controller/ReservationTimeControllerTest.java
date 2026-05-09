package roomescape.time.controller;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;

import static org.hamcrest.Matchers.is;
import static roomescape.date.fixture.ReservationDateApiFixture.createReservationDate;
import static roomescape.reservation.fixture.ReservationApiFixture.createReservation;
import static roomescape.theme.fixture.ThemeApiFixture.createTheme;
import static roomescape.time.fixture.ReservationTimeApiFixture.createReservationTime;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql(scripts = "classpath:truncate.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ReservationTimeControllerTest {

    private static String startAt1 = "10:00:00";
    private static String startAt2 = "11:00:00";
    private static String themeName = "테마1";
    private static String name = "브라운";

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("사용자는 특정 날짜와 테마의 예약 가능한 시간을 조회한다.")
    void readAvailableTimes() {
        Integer dateId = createReservationDate(LocalDate.now().plusDays(1).toString());
        Integer timeId = createReservationTime(startAt1);
        Integer themeId = createTheme(themeName);

        RestAssured.given().log().all()
                .queryParam("dateId", dateId)
                .queryParam("themeId", themeId)
                .when().get("/member/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1))
                .body("[0].id", is(timeId))
                .body("[0].startAt", is(startAt1));
    }

    @Test
    @DisplayName("이미 예약된 시간은 예약 가능한 시간 목록에서 제외된다.")
    void readAvailableTimesExcludeReservedTime() {
        Integer dateId = createReservationDate(LocalDate.now().plusDays(1).toString());
        Integer timeId = createReservationTime(startAt1);
        Integer availableTimeId = createReservationTime(startAt2);
        Integer themeId = createTheme(themeName);
        createReservation(name, dateId, timeId, themeId);

        RestAssured.given().log().all()
                .queryParam("dateId", dateId)
                .queryParam("themeId", themeId)
                .when().get("/member/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1))
                .body("[0].id", is(availableTimeId))
                .body("[0].startAt", is(startAt2));
    }

    @Test
    @DisplayName("예약 시간이 없으면 빈 목록을 반환한다.")
    void readAvailableTimesEmpty() {
        Integer dateId = createReservationDate(LocalDate.now().plusDays(1).toString());
        Integer themeId = createTheme(themeName);

        RestAssured.given().log().all()
                .queryParam("dateId", dateId)
                .queryParam("themeId", themeId)
                .when().get("/member/times")
                .then().log().all()
                .statusCode(200);
    }

}
