package roomescape.domain.reservation;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.reservation.entity.Reservation;
import roomescape.domain.reservation.entity.ReservationTime;
import roomescape.domain.reservation.entity.Theme;
import roomescape.domain.reservation.repository.ReservationRepository;
import roomescape.domain.reservation.repository.ReservationTimeRepository;
import roomescape.domain.reservation.repository.ThemeRepository;
import roomescape.domain.reservation.utils.JdbcTemplateUtils;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
class ThemeApiTest {

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ThemeRepository themeRepository;

    private static LocalDate minusDay(LocalDate date, int days) {
        return date.minusDays(days);
    }

    @BeforeEach
    void setUp() {
        JdbcTemplateUtils.deleteAllTables(jdbcTemplate);
    }

    @DisplayName("테마를 추가한다.")
    @Test
    void test1() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "테마1");
        params.put("description", "테마1");
        params.put("thumbnail", "www.m.com");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201);
    }

    @DisplayName("테마를 가져온다")
    @Test
    void test2() {
        // given
        Theme theme = Theme.withoutId("테마1", "테마1", "www.m.com");

        themeRepository.save(theme);

        // when & then
        RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @DisplayName("해당 테마를 삭제한다")
    @Test
    void test3() {
        // given
        Theme theme = Theme.withoutId("테마1", "테마1", "www.m.com");

        Theme saved = themeRepository.save(theme);

        Long savedId = saved.getId();

        // when & then
        RestAssured.given().log().all()
                .when().delete("/themes/" + savedId.intValue())
                .then().log().all()
                .statusCode(204);
    }

    @DisplayName("없는 테마를 삭제하면 NOT FOUND 반환")
    @Test
    void test4() {
        // given
        int notFoundStatusCode = 404;

        // when & then
        RestAssured.given().log().all()
                .when().delete("/themes/4")
                .then().log().all()
                .statusCode(notFoundStatusCode);
    }

    @DisplayName("사용 중인 테마가 있다면 삭제를 하면 409 CONFLICT를 반환한다.")
    @Test
    void test5() {
        // given
        int conflictStatusCode = 409;
        ReservationTime reservationTime = ReservationTime.withoutId(LocalTime.now());

        ReservationTime savedTime = reservationTimeRepository.save(reservationTime);

        Theme theme = Theme.withoutId("공포", "우테코 공포",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
        Theme savedTheme = themeRepository.save(theme);
        Long savedId = savedTheme.getId();

        Reservation reservation = Reservation.withoutId("꾹", LocalDate.now(), savedTime, savedTheme);
        reservationRepository.save(reservation);

        // when & then
        RestAssured.given().log().all()
                .when().delete("/themes/" + savedId.intValue())
                .then().log().all()
                .statusCode(conflictStatusCode);
    }

    @DisplayName("가장 인기있는 테마를 가져온다.")
    @Test
    void test6() {
        // given
        ReservationTime reservationTime = ReservationTime.withoutId(LocalTime.now());

        ReservationTime savedTime = reservationTimeRepository.save(reservationTime);

        Theme theme1 = Theme.withoutId("공포1", "우테코 공포", "www.m.com");
        Theme theme2 = Theme.withoutId("공포2", "우테코 공포", "www.m.com");
        Theme theme3 = Theme.withoutId("공포3", "우테코 공포", "www.m.com");
        Theme savedTheme1 = themeRepository.save(theme1);
        Theme savedTheme2 = themeRepository.save(theme2);
        Theme savedTheme3 = themeRepository.save(theme3);

        reservationRepository.save(Reservation.withoutId("꾹", minusDay(LocalDate.now(), 1), savedTime, savedTheme3));
        reservationRepository.save(Reservation.withoutId("꾹", minusDay(LocalDate.now(), 2), savedTime, savedTheme3));
        reservationRepository.save(Reservation.withoutId("꾹", minusDay(LocalDate.now(), 3), savedTime, savedTheme3));
        reservationRepository.save(Reservation.withoutId("꾹", minusDay(LocalDate.now(), 4), savedTime, savedTheme1));
        reservationRepository.save(Reservation.withoutId("꾹", minusDay(LocalDate.now(), 5), savedTime, savedTheme1));
        reservationRepository.save(Reservation.withoutId("꾹", minusDay(LocalDate.now(), 6), savedTime, savedTheme2));

        // when & then
        RestAssured.given().log().all()
                .when().get("/themes/popular")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(3))
                .body("name", contains("공포3", "공포1", "공포2"));
    }
}
