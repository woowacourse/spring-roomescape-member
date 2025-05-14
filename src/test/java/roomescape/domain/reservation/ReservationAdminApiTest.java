package roomescape.domain.reservation;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.domain.auth.config.JwtProperties;
import roomescape.domain.auth.entity.Name;
import roomescape.domain.auth.entity.Password;
import roomescape.domain.auth.entity.Roles;
import roomescape.domain.auth.entity.User;
import roomescape.domain.auth.repository.UserRepository;
import roomescape.domain.auth.service.JwtManager;
import roomescape.domain.reservation.entity.Reservation;
import roomescape.domain.reservation.entity.ReservationTime;
import roomescape.domain.reservation.entity.Theme;
import roomescape.domain.reservation.repository.ReservationRepository;
import roomescape.domain.reservation.repository.ReservationTimeRepository;
import roomescape.domain.reservation.repository.ThemeRepository;
import roomescape.infrastructure.security.Sha256PasswordEncryptor;
import roomescape.utils.DateFormatter;
import roomescape.utils.JdbcTemplateUtils;
import roomescape.utils.PasswordFixture;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ReservationAdminApiTest {

    private static String adminToken;
    private static String userToken;
    private String cookieKey;

    @Autowired
    private JwtProperties jwtProperties;
    @Autowired
    private ReservationTimeRepository reservationTimeRepository;
    @Autowired
    private ThemeRepository themeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @LocalServerPort
    private int port;

    @BeforeAll
    static void setUp(@Autowired final JwtManager jwtManager) {
        final Password password = Password.encrypt("1234", new Sha256PasswordEncryptor());
        final User admin = new User(1L, new Name("꾹"), "tizm@naver.com", password, Roles.ADMIN);
        final User user = new User(1L, new Name("꾹"), "user@naver.com", password, Roles.USER);

        adminToken = jwtManager.createToken(admin);
        userToken = jwtManager.createToken(user);
    }

    @BeforeEach
    void init() {
        RestAssured.port = port;
        JdbcTemplateUtils.deleteAllTables(jdbcTemplate);
        cookieKey = jwtProperties.getCookieKey();
    }

    @DisplayName("어드민 페이지로 접근할 수 있다.")
    @Test
    void reservationTest1() {
        RestAssured.given()
                .cookie("token", adminToken)
                .log()
                .all()
                .when()
                .get("/admin")
                .then()
                .log()
                .all()
                .statusCode(200);
    }

    @DisplayName("권한이 없다면 403 상태 코드를 반환한다.")
    @Test
    void reservationTest2() {
        RestAssured.given()
                .log()
                .all()
                .when()
                .get("/admin")
                .then()
                .log()
                .all()
                .statusCode(403);

    }

    @DisplayName("어드민이 예약 관리 페이지에 접근한다.")
    @Test
    void adminReservationTest1() {
        RestAssured.given()
                .cookie("token", adminToken)
                .log()
                .all()
                .when()
                .get("/admin/reservation")
                .then()
                .log()
                .all()
                .statusCode(200);
    }

    @DisplayName("권한이 없다면 예약 관리 페이지에 접근할 수 없으며 403 에러 코드를 반환한다.")
    @Test
    void adminReservationTest2() {
        RestAssured.given()
                .log()
                .all()
                .when()
                .get("/admin/reservation")
                .then()
                .log()
                .all()
                .statusCode(403);
    }

    @DisplayName("일반 유저가 어드민 페이지에 접근하면 403 예외가 발생한다.")
    @Test
    void userAccessAdminPage_forbidden() {
        // given
        // when & then
        RestAssured.given()
                .cookie("token", userToken)
                .log()
                .all()
                .when()
                .get("/admin")
                .then()
                .log()
                .all()
                .statusCode(403);

        RestAssured.given()
                .cookie("token", userToken)
                .log()
                .all()
                .when()
                .get("/admin/reservation")
                .then()
                .log()
                .all()
                .statusCode(403);
    }

    @DisplayName("어드민이 예약을 생성할 수 있다.")
    @Test
    void addReservationByAdminTest1() {
        final User user = userRepository.save(
                User.withoutId(new Name("꾹"), "tsadsad@naver.com", PasswordFixture.generate(), Roles.USER));
        final ReservationTime reservationTime = reservationTimeRepository.save(
                ReservationTime.withoutId(java.time.LocalTime.of(10, 0)));
        final Theme theme = themeRepository.save(Theme.withoutId("공포", "공포 테마", "https://test.com/image.jpg"));

        final LocalDate now = LocalDate.now();

        final Map<String, Object> reservation = new HashMap<>();
        reservation.put("memberId", user.getId());
        reservation.put("date", DateFormatter.format(now.plusDays(1)));
        reservation.put("timeId", reservationTime.getId());
        reservation.put("themeId", theme.getId());

        // when & then
        RestAssured.given()
                .cookie("token", adminToken)
                .contentType("application/json")
                .body(reservation)
                .log()
                .all()
                .when()
                .post("/admin/reservations")
                .then()
                .log()
                .all()
                .statusCode(201);
    }

    @DisplayName("모든 예약 정보를 반환한다.")
    @Test
    void test3() {
        // given
        final LocalTime time = LocalTime.of(15, 0);
        final ReservationTime reservationTime = ReservationTime.withoutId(time);
        final ReservationTime savedReservationTime = reservationTimeRepository.save(reservationTime);
        final Theme theme = Theme.withoutId("공포", "우테코 공포",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
        final Theme savedTheme = themeRepository.save(theme);

        final Name name = new Name("브라운");
        final User user = User.withoutId(name, "admin@naver.com", Password.of("1234"), Roles.USER);
        final User savedUser = userRepository.save(user);

        final Reservation reservation = Reservation.withoutId(savedUser, LocalDate.now(), savedReservationTime,
                savedTheme);

        reservationRepository.save(reservation);

        // then
        RestAssured.given()
                .cookie(cookieKey, adminToken)
                .log()
                .all()
                .when()
                .get("/admin/reservations")
                .then()
                .log()
                .all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @DisplayName("예약 정보를 themeId, memberId, dateFrom, dateTo로 필터링하여 조회한다.")
    @Test
    void filterReservationsByParams() {
        // given
        final ReservationTime time = reservationTimeRepository.save(ReservationTime.withoutId(LocalTime.of(10, 0)));
        final Theme theme1 = themeRepository.save(Theme.withoutId("테마1", "설명1", "img1"));
        final Theme theme2 = themeRepository.save(Theme.withoutId("테마2", "설명2", "img2"));
        final User user1 = userRepository.save(
                User.withoutId(new Name("유저1"), "user1@a.com", Password.of("1234"), Roles.USER));
        final User user2 = userRepository.save(
                User.withoutId(new Name("유저2"), "user2@a.com", Password.of("1234"), Roles.USER));
        final LocalDate today = LocalDate.now();

        // 예약 3개 생성
        reservationRepository.save(Reservation.withoutId(user1, today, time, theme1));
        reservationRepository.save(Reservation.withoutId(user2, today.plusDays(1), time, theme1));
        reservationRepository.save(Reservation.withoutId(user1, today.plusDays(2), time, theme2));

        // when & then
        // themeId로 필터
        RestAssured.given()
                .queryParam("themeId", theme1.getId())
                .cookie(cookieKey, adminToken)
                .when()
                .get("/admin/reservations")
                .then()
                .statusCode(200)
                .body("size()", is(2));

        // memberId로 필터
        RestAssured.given()
                .cookie(cookieKey, adminToken)
                .queryParam("memberId", user2.getId())
                .when()
                .get("/admin/reservations")
                .then()
                .statusCode(200)
                .body("size()", is(1));

        // 날짜 범위로 필터
        RestAssured.given()
                .cookie(cookieKey, adminToken)
                .queryParam("dateFrom", DateFormatter.format(today))
                .queryParam("dateTo", DateFormatter.format(today.plusDays(1)))
                .when()
                .get("/admin/reservations")
                .then()
                .statusCode(200)
                .body("size()", is(2));

        // themeId + memberId + 날짜 범위 조합
        RestAssured.given()
                .cookie(cookieKey, adminToken)
                .queryParam("themeId", theme1.getId())
                .queryParam("memberId", user2.getId())
                .queryParam("dateFrom", DateFormatter.format(today))
                .queryParam("dateTo", DateFormatter.format(today.plusDays(2)))
                .when()
                .get("/admin/reservations")
                .then()
                .statusCode(200)
                .body("size()", is(1));
    }
}
