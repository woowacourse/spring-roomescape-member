package roomescape.domain.reservation;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import roomescape.domain.reservation.entity.Name;
import roomescape.domain.reservation.entity.Reservation;
import roomescape.domain.reservation.entity.ReservationTime;
import roomescape.domain.reservation.entity.Theme;
import roomescape.domain.reservation.repository.ReservationDao;
import roomescape.domain.reservation.repository.ReservationTimeDao;
import roomescape.domain.reservation.repository.ThemeDao;
import roomescape.domain.reservation.utils.JdbcTemplateUtils;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ThemeApiTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ReservationTimeDao reservationTimeRepository;
    @Autowired
    private ReservationDao reservationDao;
    @Autowired
    private ThemeDao themeDao;

    private static LocalDate minusDay(LocalDate date, int days) {
        return date.minusDays(days);
    }

    @AfterEach
    void tearDown() {
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

        themeDao.save(theme);

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

        Theme saved = themeDao.save(theme);

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
        Theme savedTheme = themeDao.save(theme);
        Long savedId = savedTheme.getId();

        Reservation reservation = Reservation.withoutId(new Name("꾹"), LocalDate.now(), savedTime, savedTheme);
        reservationDao.save(reservation);

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
        Theme savedTheme1 = themeDao.save(theme1);
        Theme savedTheme2 = themeDao.save(theme2);
        Theme savedTheme3 = themeDao.save(theme3);

        Name name = new Name("꾹");
        reservationDao.save(Reservation.withoutId(name, minusDay(LocalDate.now(), 1), savedTime, savedTheme3));
        reservationDao.save(Reservation.withoutId(name, minusDay(LocalDate.now(), 2), savedTime, savedTheme3));
        reservationDao.save(Reservation.withoutId(name, minusDay(LocalDate.now(), 3), savedTime, savedTheme3));
        reservationDao.save(Reservation.withoutId(name, minusDay(LocalDate.now(), 4), savedTime, savedTheme1));
        reservationDao.save(Reservation.withoutId(name, minusDay(LocalDate.now(), 5), savedTime, savedTheme1));
        reservationDao.save(Reservation.withoutId(name, minusDay(LocalDate.now(), 6), savedTime, savedTheme2));

        // when & then
        RestAssured.given().log().all()
                .when().get("/themes/popular")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(3))
                .body("name", contains("공포3", "공포1", "공포2"));
    }
}
