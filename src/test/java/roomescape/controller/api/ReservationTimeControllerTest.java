package roomescape.controller.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import roomescape.model.Reservation;
import roomescape.model.ReservationTime;
import roomescape.model.Theme;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/reset.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ReservationTimeControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @BeforeEach
    void initPort() {
        RestAssured.port = port;
    }

    @DisplayName("예약 시간 목록 조회")
    @Test
    void getReservationTimesWhenEmpty() {
        reservationTimeRepository.save(new ReservationTime(LocalTime.parse("09:00")));
        reservationTimeRepository.save(new ReservationTime(LocalTime.parse("12:00")));

        RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(2));
    }

    @DisplayName("시간 목록 예약 여부 조회")
    @Test
    void getReservationTimesWithBooked() {
        final ReservationTime reservationTime = reservationTimeRepository.save(new ReservationTime(LocalTime.parse("09:00")));
        final Theme theme = themeRepository.save(new Theme("이름1", "설명1", "썸네일1"));
        final LocalDate localDate = LocalDate.now().plusMonths(2);
        reservationRepository.save(new Reservation("이름1", localDate, reservationTime, theme));

        RestAssured.given().log().all()
                .when().get("/times?date=" + localDate + "&themeId=1")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @DisplayName("예약 시간 추가 및 삭제")
    @TestFactory
    Stream<DynamicTest> saveAndDeleteReservationTime() {
        return Stream.of(
                dynamicTest("예약 시간을 추가한다", () -> {
                    final Map<String, String> params = Map.of("startAt", "10:10");

                    RestAssured.given().log().all()
                            .contentType(ContentType.JSON)
                            .body(params)
                            .when().post("/times")
                            .then().log().all()
                            .statusCode(201)
                            .header("Location", "/times/1");
                }),
                dynamicTest("예약 시간을 삭제한다", () ->
                        RestAssured.given().log().all()
                                .when().delete("/times/1")
                                .then().log().all()
                                .statusCode(204)
                )
        );
    }

    @DisplayName("유효하지 않은 시간 형식 입력 시 BadRequest 반환")
    @ParameterizedTest
    @ValueSource(strings = {"11:11:11", "25:10"})
    void invalidTimeFormat(final String time) {
        final Map<String, String> params = Map.of("startAt", time);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(400)
                .body(equalTo("잘못된 입력 형식입니다."));
    }
}
