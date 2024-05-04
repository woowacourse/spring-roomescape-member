package roomescape.controller.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
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

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/reset.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ThemeControllerTest {

    @LocalServerPort
    int port;

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

    @DisplayName("테마 목록 조회")
    @Test
    void getReservationsWhenEmpty() {
        themeRepository.save(new Theme("이름1", "설명1", "썸네일1"));
        themeRepository.save(new Theme("이름2", "설명2", "썸네일2"));

        RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(2));
    }

    @DisplayName("테마 추가 및 삭제")
    @TestFactory
    Stream<DynamicTest> saveAndDeleteTheme() {
        return Stream.of(
                dynamicTest("테마를 추가한다", () -> {
                    final Map<String, Object> params = Map.of(
                            "name", "테마테마",
                            "description", "설명설명",
                            "thumbnail", "썸네일썸네일");

                    RestAssured.given().log().all()
                            .contentType(ContentType.JSON)
                            .body(params)
                            .when().post("/themes")
                            .then().log().all()
                            .statusCode(201)
                            .header("Location", "/themes/1");
                }),
                dynamicTest("테마를 삭제한다", () ->
                        RestAssured.given().log().all()
                                .when().delete("/themes/1")
                                .then().log().all()
                                .statusCode(204)
                )
        );
    }

    @DisplayName("인기 테마 조회")
    @Test
    void getPopularThemes() {
        final ReservationTime reservationTime = reservationTimeRepository.save(new ReservationTime(LocalTime.parse("09:00")));
        final Theme theme = themeRepository.save(new Theme("이름1", "설명1", "썸네일1"));
        final LocalDate localDate = LocalDate.now().minusDays(1);
        reservationRepository.save(new Reservation("이름1", localDate, reservationTime, theme));

        RestAssured.given().log().all()
                .when().get("/themes/popular?date=" + localDate)
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }
}
