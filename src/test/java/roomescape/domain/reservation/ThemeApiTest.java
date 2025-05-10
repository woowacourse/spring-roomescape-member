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
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.auth.entity.Name;
import roomescape.domain.auth.entity.Password;
import roomescape.domain.auth.entity.Roles;
import roomescape.domain.auth.entity.User;
import roomescape.domain.auth.repository.UserRepository;
import roomescape.domain.auth.service.PasswordEncryptor;
import roomescape.domain.reservation.entity.Reservation;
import roomescape.domain.reservation.entity.ReservationTime;
import roomescape.domain.reservation.entity.Theme;
import roomescape.domain.reservation.repository.ReservationRepository;
import roomescape.domain.reservation.repository.ReservationTimeRepository;
import roomescape.domain.reservation.repository.ThemeRepository;
import roomescape.utils.JdbcTemplateUtils;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ThemeApiTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncryptor passwordEncryptor;

    private Password password;

    @LocalServerPort
    private int port;

    private static LocalDate minusDay(final LocalDate date, final int days) {
        return date.minusDays(days);
    }

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        JdbcTemplateUtils.deleteAllTables(jdbcTemplate);
        password = Password.encrypt("1234", passwordEncryptor);
    }

    @DisplayName("테마를 추가한다.")
    @Test
    void test1() {
        final Map<String, String> params = new HashMap<>();
        params.put("name", "테마1");
        params.put("description", "테마1");
        params.put("thumbnail", "www.m.com");

        RestAssured.given()
                .log()
                .all()
                .contentType(ContentType.JSON)
                .body(params)
                .when()
                .post("/themes")
                .then()
                .log()
                .all()
                .statusCode(201);
    }

    @DisplayName("테마를 가져온다")
    @Test
    void test2() {
        // given
        final Theme theme = Theme.withoutId("테마1", "테마1", "www.m.com");
        themeRepository.save(theme);

        // when & then
        RestAssured.given()
                .log()
                .all()
                .when()
                .get("/themes")
                .then()
                .log()
                .all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @DisplayName("해당 테마를 삭제한다")
    @Test
    void test3() {
        // given
        final Theme theme = Theme.withoutId("테마1", "테마1", "www.m.com");
        final Theme saved = themeRepository.save(theme);
        final Long savedId = saved.getId();

        // when & then
        RestAssured.given()
                .log()
                .all()
                .when()
                .delete("/themes/" + savedId)
                .then()
                .log()
                .all()
                .statusCode(204);
    }

    @DisplayName("없는 테마를 삭제하면 NOT FOUND 반환")
    @Test
    void test4() {
        // given
        final int notFoundStatusCode = 404;

        // when & then
        RestAssured.given()
                .log()
                .all()
                .when()
                .delete("/themes/4")
                .then()
                .log()
                .all()
                .statusCode(notFoundStatusCode);
    }

    @DisplayName("사용 중인 테마가 있다면 삭제를 하면 409 CONFLICT를 반환한다.")
    @Test
    void test5() {
        // given
        final int conflictStatusCode = 409;
        final ReservationTime reservationTime = ReservationTime.withoutId(LocalTime.now());
        final ReservationTime savedTime = reservationTimeRepository.save(reservationTime);
        final Theme theme = Theme.withoutId("공포", "우테코 공포",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
        final Theme savedTheme = themeRepository.save(theme);
        final Long savedId = savedTheme.getId();
        final Name name = new Name("브라운");
        final User user = userRepository.save(User.withoutId(name, "admin@naver.com", password, Roles.USER));

        final Reservation reservation = Reservation.withoutId(user, LocalDate.now(), savedTime, savedTheme);
        reservationRepository.save(reservation);

        // when & then
        RestAssured.given()
                .log()
                .all()
                .when()
                .delete("/themes/" + savedId)
                .then()
                .log()
                .all()
                .statusCode(conflictStatusCode);
    }

    @DisplayName("가장 인기있는 테마를 가져온다.")
    @Test
    void test6() {
        // given
        final ReservationTime reservationTime = ReservationTime.withoutId(LocalTime.now());

        final ReservationTime savedTime = reservationTimeRepository.save(reservationTime);

        final Theme theme1 = Theme.withoutId("공포1", "우테코 공포", "www.m.com");
        final Theme theme2 = Theme.withoutId("공포2", "우테코 공포", "www.m.com");
        final Theme theme3 = Theme.withoutId("공포3", "우테코 공포", "www.m.com");
        final Theme savedTheme1 = themeRepository.save(theme1);
        final Theme savedTheme2 = themeRepository.save(theme2);
        final Theme savedTheme3 = themeRepository.save(theme3);

        final Name name = new Name("브라운");
        final User user = userRepository.save(User.withoutId(name, "admin@naver.com", password, Roles.USER));

        reservationRepository.save(Reservation.withoutId(user, minusDay(LocalDate.now(), 2), savedTime, savedTheme3));
        reservationRepository.save(Reservation.withoutId(user, minusDay(LocalDate.now(), 3), savedTime, savedTheme3));
        reservationRepository.save(Reservation.withoutId(user, minusDay(LocalDate.now(), 1), savedTime, savedTheme3));
        reservationRepository.save(Reservation.withoutId(user, minusDay(LocalDate.now(), 4), savedTime, savedTheme1));
        reservationRepository.save(Reservation.withoutId(user, minusDay(LocalDate.now(), 5), savedTime, savedTheme1));
        reservationRepository.save(Reservation.withoutId(user, minusDay(LocalDate.now(), 6), savedTime, savedTheme2));

        // when & then
        RestAssured.given()
                .log()
                .all()
                .when()
                .get("/themes/popular")
                .then()
                .log()
                .all()
                .statusCode(200)
                .body("size()", is(3))
                .body("name", contains("공포3", "공포1", "공포2"));
    }

    @DisplayName("가장 인기있는 테마를 최대 10개만 가져온다.")
    @Test
    void testPopularThemesLimit10() {
        // given
        final ReservationTime reservationTime = ReservationTime.withoutId(LocalTime.now());
        final ReservationTime savedTime = reservationTimeRepository.save(reservationTime);
        final Name name = new Name("브라운");
        final User user = userRepository.save(User.withoutId(name, "admin@naver.com", password, Roles.USER));

        // 11개의 테마 생성 및 각각 예약
        for (int i = 1; i <= 11; i++) {
            final Theme theme = Theme.withoutId("테마" + i, "설명" + i, "www.m.com/" + i);
            final Theme savedTheme = themeRepository.save(theme);
            reservationRepository.save(
                    Reservation.withoutId(user, minusDay(LocalDate.now(), 1), savedTime, savedTheme));
        }

        // when & then
        RestAssured.given()
                .log()
                .all()
                .when()
                .get("/themes/popular")
                .then()
                .log()
                .all()
                .statusCode(200)
                .body("size()", is(10));
    }
}
