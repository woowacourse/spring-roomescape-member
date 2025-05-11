package roomescape.domain.reservation;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
import org.springframework.web.util.UriComponentsBuilder;
import roomescape.domain.auth.entity.Name;
import roomescape.domain.auth.entity.Password;
import roomescape.domain.auth.entity.Roles;
import roomescape.domain.auth.entity.User;
import roomescape.domain.auth.repository.UserRepository;
import roomescape.domain.auth.service.JwtManager;
import roomescape.domain.auth.service.PasswordEncryptor;
import roomescape.domain.reservation.entity.Reservation;
import roomescape.domain.reservation.entity.ReservationTime;
import roomescape.domain.reservation.entity.Theme;
import roomescape.domain.reservation.repository.ReservationRepository;
import roomescape.domain.reservation.repository.ReservationTimeRepository;
import roomescape.domain.reservation.repository.ThemeRepository;
import roomescape.utils.JdbcTemplateUtils;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ReservationApiTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtManager jwtManager;

    @Autowired
    private PasswordEncryptor passwordEncryptor;

    private Password password;

    @LocalServerPort
    private int port;

    public static String formatDateTime(final LocalDate dateTime) {
        final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return dateTimeFormatter.format(dateTime);
    }

    @BeforeEach
    void init() {
        RestAssured.port = port;
        JdbcTemplateUtils.deleteAllTables(jdbcTemplate);
        password = Password.encrypt("1234", passwordEncryptor);
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
        final User user = User.withoutId(name, "admin@naver.com", password, Roles.USER);
        final User savedUser = userRepository.save(user);

        final Reservation reservation = Reservation.withoutId(savedUser, LocalDate.now(), savedReservationTime,
                savedTheme);

        reservationRepository.save(reservation);

        // then
        RestAssured.given()
                .log()
                .all()
                .when()
                .get("/reservations")
                .then()
                .log()
                .all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @DisplayName("예약 정보를 추가한다.")
    @Test
    void test4() {
        // given
        final ReservationTime reservationTime = ReservationTime.withoutId(LocalTime.now());
        final ReservationTime savedReservationTime = reservationTimeRepository.save(reservationTime);

        final Theme theme = Theme.withoutId("공포", "우테코 공포",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
        final Theme savedTheme = themeRepository.save(theme);
        final LocalDate now = LocalDate.now();

        final User user = userRepository.save(User.withoutId(new Name("브라운"), "dsa@naver.com", password, Roles.USER));
        final String token = jwtManager.createToken(user);

        final Map<String, Object> reservation = new HashMap<>();
        reservation.put("date", formatDateTime(now.plusDays(1)));
        reservation.put("timeId", savedReservationTime.getId());
        reservation.put("themeId", savedTheme.getId());

        // when & then
        RestAssured.given()
                .cookie("token", token)
                .log()
                .all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when()
                .post("/reservations")
                .then()
                .log()
                .all()
                .statusCode(201);
    }

    @DisplayName("존재하지 않는 예약 시간 ID 를 추가하면 예외를 반환한다.")
    @Test
    void test5() {

        final Theme theme = Theme.withoutId("공포", "우테코 공포",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
        final Theme savedTheme = themeRepository.save(theme);

        final LocalDate now = LocalDate.now();

        final Map<String, Object> reservation = new HashMap<>();
        reservation.put("date", formatDateTime(now.plusDays(1)));
        reservation.put("timeId", 1);
        reservation.put("themeId", savedTheme.getId());

        final User user = userRepository.save(User.withoutId(new Name("브라운"), "dsa@naver.com", password, Roles.USER));
        final String token = jwtManager.createToken(user);

        // when & then
        RestAssured.given()
                .cookie("token", token)
                .log()
                .all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when()
                .post("/reservations")
                .then()
                .log()
                .all()
                .statusCode(404);
    }

    @DisplayName("존재하지 않는 테마 ID 를 추가하면 예외를 반환한다.")
    @Test
    void notExistThemeId() {
        // given
        final ReservationTime reservationTime = ReservationTime.withoutId(LocalTime.now());
        final ReservationTime savedReservationTime = reservationTimeRepository.save(reservationTime);

        final LocalDate now = LocalDate.now();

        final Map<String, Object> reservation = new HashMap<>();
        reservation.put("date", formatDateTime(now.plusDays(1)));
        reservation.put("timeId", savedReservationTime.getId());
        reservation.put("themeId", 1L);

        final User user = userRepository.save(User.withoutId(new Name("브라운"), "dsa@naver.com", password, Roles.USER));
        final String token = jwtManager.createToken(user);

        // when & then
        RestAssured.given()
                .cookie("token", token)
                .log()
                .all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when()
                .post("/reservations")
                .then()
                .log()
                .all()
                .statusCode(404);
    }

    @DisplayName("예약을 삭제한다.")
    @Test
    void test6() {
        // given
        final LocalDate now = LocalDate.now();

        final ReservationTime reservationTime = ReservationTime.withoutId(LocalTime.now());
        final ReservationTime savedReservationTime = reservationTimeRepository.save(reservationTime);

        final Theme theme = Theme.withoutId("공포", "우테코 공포",
                "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg");
        final Theme savedTheme = themeRepository.save(theme);

        final Name name = new Name("브라운");
        final User user = User.withoutId(name, "admin@naver.com", password, Roles.USER);
        final User savedUser = userRepository.save(user);

        final Reservation reservation = Reservation.withoutId(savedUser, now, savedReservationTime, savedTheme);
        final Reservation saved = reservationRepository.save(reservation);
        final Long id = saved.getId();

        // when & then
        RestAssured.given()
                .log()
                .all()
                .when()
                .delete("/reservations/" + id)
                .then()
                .log()
                .all()
                .statusCode(204);
    }

    @DisplayName("존재하지 않는 예약을 삭제할 경우 NOT_FOUND 반환")
    @Test
    void test7() {
        RestAssured.given()
                .log()
                .all()
                .when()
                .delete("/reservations/4")
                .then()
                .log()
                .all()
                .statusCode(404);
    }

    @DisplayName("예약 가능한 시간을 반환한다")
    @Test
    void test9() {
        final Theme theme = themeRepository.save(Theme.withoutId("공포", "공포", "www.m.com"));
        final ReservationTime time1 = reservationTimeRepository.save(ReservationTime.withoutId(LocalTime.of(10, 0)));
        reservationTimeRepository.save(ReservationTime.withoutId(LocalTime.of(11, 0)));

        final LocalDate date = LocalDate.now();

        final Name name = new Name("브라운");
        final User user = User.withoutId(name, "admin@naver.com", password, Roles.USER);
        final User savedUser = userRepository.save(user);

        reservationRepository.save(Reservation.withoutId(savedUser, date, time1, theme));

        final String path = UriComponentsBuilder.fromUriString("/reservations/available")
                .queryParam("date", formatDateTime(date))
                .queryParam("themeId", theme.getId())
                .build()
                .toUriString();

        RestAssured.given()
                .log()
                .all()
                .when()
                .get(path)
                .then()
                .log()
                .all()
                .statusCode(200)
                .body("size()", is(2))
                .body("alreadyBooked", containsInAnyOrder(true, false));
    }

    @DisplayName("예약 정보를 themeId, memberId, dateFrom, dateTo로 필터링하여 조회한다.")
    @Test
    void filterReservationsByParams() {
        // given
        final ReservationTime time = reservationTimeRepository.save(ReservationTime.withoutId(LocalTime.of(10, 0)));
        final Theme theme1 = themeRepository.save(Theme.withoutId("테마1", "설명1", "img1"));
        final Theme theme2 = themeRepository.save(Theme.withoutId("테마2", "설명2", "img2"));
        final User user1 = userRepository.save(User.withoutId(new Name("유저1"), "user1@a.com", password, Roles.USER));
        final User user2 = userRepository.save(User.withoutId(new Name("유저2"), "user2@a.com", password, Roles.USER));
        final LocalDate today = LocalDate.now();

        // 예약 3개 생성
        reservationRepository.save(Reservation.withoutId(user1, today, time, theme1));
        reservationRepository.save(Reservation.withoutId(user2, today.plusDays(1), time, theme1));
        reservationRepository.save(Reservation.withoutId(user1, today.plusDays(2), time, theme2));

        // when & then
        // themeId로 필터
        RestAssured.given()
                .queryParam("themeId", theme1.getId())
                .when()
                .get("/reservations")
                .then()
                .statusCode(200)
                .body("size()", is(2));

        // memberId로 필터
        RestAssured.given()
                .queryParam("memberId", user2.getId())
                .when()
                .get("/reservations")
                .then()
                .statusCode(200)
                .body("size()", is(1));

        // 날짜 범위로 필터
        RestAssured.given()
                .queryParam("dateFrom", formatDateTime(today))
                .queryParam("dateTo", formatDateTime(today.plusDays(1)))
                .when()
                .get("/reservations")
                .then()
                .statusCode(200)
                .body("size()", is(2));

        // themeId + memberId + 날짜 범위 조합
        RestAssured.given()
                .queryParam("themeId", theme1.getId())
                .queryParam("memberId", user2.getId())
                .queryParam("dateFrom", formatDateTime(today))
                .queryParam("dateTo", formatDateTime(today.plusDays(2)))
                .when()
                .get("/reservations")
                .then()
                .statusCode(200)
                .body("size()", is(1));
    }
}
