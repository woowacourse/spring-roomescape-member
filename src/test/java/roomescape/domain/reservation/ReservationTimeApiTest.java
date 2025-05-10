package roomescape.domain.reservation;

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
public class ReservationTimeApiTest {

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

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        JdbcTemplateUtils.deleteAllTables(jdbcTemplate);
    }

    @DisplayName("예약 시간을 추가한다.")
    @Test
    void test1() {
        final Map<String, String> params = new HashMap<>();
        params.put("startAt", "10:00");

        RestAssured.given()
                .log()
                .all()
                .contentType(ContentType.JSON)
                .body(params)
                .when()
                .post("/times")
                .then()
                .log()
                .all()
                .statusCode(201);
    }

    @DisplayName("예약 시간 요청에 초가 있으면 Bad Request 반환")
    @Test
    void test2() {
        final Map<String, String> params = new HashMap<>();
        params.put("startAt", "10:00:20");

        RestAssured.given()
                .log()
                .all()
                .contentType(ContentType.JSON)
                .body(params)
                .when()
                .post("/times")
                .then()
                .log()
                .all()
                .statusCode(400);
    }

    @DisplayName("예약 시간을 가져온다")
    @Test
    void test3() {
        // given
        final ReservationTime reservationTime = ReservationTime.withoutId(LocalTime.now());

        reservationTimeRepository.save(reservationTime);

        // when & then
        RestAssured.given()
                .log()
                .all()
                .when()
                .get("/times")
                .then()
                .log()
                .all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @DisplayName("해당 예약 시간을 삭제한다")
    @Test
    void test4() {
        // given
        final ReservationTime reservationTime = ReservationTime.withoutId(LocalTime.now());

        final ReservationTime saved = reservationTimeRepository.save(reservationTime);

        final Long savedId = saved.getId();

        // when & then
        RestAssured.given()
                .log()
                .all()
                .when()
                .delete("/times/" + savedId)
                .then()
                .log()
                .all()
                .statusCode(204);
    }

    @DisplayName("없는 예약 시간을 삭제하면 NOT FOUND 반환")
    @Test
    void test5() {
        // given
        final int notFoundStatusCode = 404;

        // when & then
        RestAssured.given()
                .log()
                .all()
                .when()
                .delete("/times/4")
                .then()
                .log()
                .all()
                .statusCode(notFoundStatusCode);
    }

    @DisplayName("사용 중인 예약 시간이 있다면 삭제를 하면 409 CONFLICT를 반환한다.")
    @Test
    void test6() {
        // given
        final int conflictStatusCode = 409;
        final ReservationTime reservationTime = ReservationTime.withoutId(LocalTime.now());

        final ReservationTime savedTime = reservationTimeRepository.save(reservationTime);
        final Long savedId = savedTime.getId();

        final Theme theme = Theme.withoutId("공포", "우테코 공포",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
        final Theme savedTheme = themeRepository.save(theme);
        final Name name = new Name("꾹");
        final Password password = Password.encrypt("1234", passwordEncryptor);

        final User user = userRepository.save(User.withoutId(name, "admin@naver.com", password, Roles.USER));

        final Reservation reservation = Reservation.withoutId(user, LocalDate.now(), savedTime, savedTheme);
        reservationRepository.save(reservation);

        // when & then
        RestAssured.given()
                .log()
                .all()
                .when()
                .delete("/times/" + savedId)
                .then()
                .log()
                .all()
                .statusCode(conflictStatusCode);
    }
}
