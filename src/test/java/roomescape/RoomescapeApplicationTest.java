package roomescape;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static roomescape.test.fixture.DateFixture.NEXT_DAY;
import static roomescape.test.fixture.DateFixture.YESTERDAY;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.utility.JwtTokenProvider;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class RoomescapeApplicationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Value("${security.jwt.token.secret-key}")
    String tokenSecretKey;

    @BeforeEach
    void setUp() {
        jdbcTemplate.update("DELETE FROM reservation");
        jdbcTemplate.update("DELETE FROM reservation_time");
        jdbcTemplate.update("DELETE FROM theme");
        jdbcTemplate.execute("ALTER TABLE reservation ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE reservation_time ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE theme ALTER COLUMN id RESTART WITH 1");
    }

    @DisplayName("저장된 모든 예약을 응답한다")
    @Test
    void canResponseAllReservations() {
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", LocalTime.of(10, 0));
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", LocalTime.of(11, 0));
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)",
                "테마1", "설명1", "썸네일1");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)",
                "테마2", "설명2", "썸네일2");
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                "예약1", LocalDate.now(), 1, 1);
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                "예약2", LocalDate.now(), 2, 2);

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(2));
    }

    @DisplayName("예약을 추가할 수 있다")
    @Test
    void canCreateReservation() {
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)",
                "이름1", "설명1", "썸네일1");
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", LocalTime.now());

        Map<String, Object> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", NEXT_DAY.toString());
        params.put("timeId", 1);
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .header("location", "/reservations/1");
    }

    @DisplayName("유효하지 않은 입력값으로 예약 추가가 불가능하다")
    @Test
    void cannotCreateReservationsWhenInvalidRequest() {
        jdbcTemplate.update("INSERT INTO reservation_time (id, start_at) VALUES (?, ?)",
                1, LocalTime.of(10, 0));

        Map<String, Object> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", "");
        params.put("timeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("과거의 날짜와 시간으로는 예약이 불가능하다")
    @Test
    void cannotCreateReservationsWhenPastRequest() {
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", LocalTime.of(10, 0));

        Map<String, Object> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", YESTERDAY.toString());
        params.put("timeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("중복 예약을 추가할 수 없다")
    @Test
    void cannotCreateReservationsWhenDuplicatedTime() {
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", LocalTime.of(10, 0));
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "테마", "설명", "썸네일");
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                "예약", NEXT_DAY.toString(), 1, 1);

        Map<String, Object> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", NEXT_DAY.toString());
        params.put("timeId", 1);
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("Id를 통해 예약을 삭제할 수 있다")
    @Test
    void canDeleteReservationById() {
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "테마", "설명", "썸네일");
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", LocalTime.of(10, 0));
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                "예약", NEXT_DAY.toString(), 1, 1);

        RestAssured.given().log().all()
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(204);
    }

    @DisplayName("예약 가능한 시간을 추가할 수 있다")
    @Test
    void canCreateReservationTime() {
        Map<String, String> params = new HashMap<>();
        params.put("startAt", "10:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(201)
                .header("location", "/times/1");
    }

    @DisplayName("이미 존재하는 예약 가능 시간은 추가할 수 없다")
    @Test
    void cannotCreateReservationTimeWhenExist() {
        LocalTime time = LocalTime.now();
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", time.toString());

        Map<String, String> params = new HashMap<>();
        params.put("startAt", time.toString());

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("유요하지 않은 요청을 예약 가능 시간을 추가할 수 없다")
    @Test
    void cannotCreateReservationTimeWhenInvalidRequest() {
        Map<String, String> params = new HashMap<>();
        params.put("startAt", "");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("예약 가능 시간을 조회할 수 있다")
    @Test
    void canResponseAllReservationTimes() {
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", LocalTime.now().toString());
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", LocalTime.now().toString());

        RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(2));
    }

    @DisplayName("예약 여부와 함께 예약 가능 시간을 조회할 수 있다")
    @Test
    void canResponseAvaliableReservationTime() {
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", LocalTime.now().toString());
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)", "테마", "설명", "썸네일");
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                "예약", NEXT_DAY.toString(), 1, 1);

        RestAssured.given()
                .param("date", NEXT_DAY.toString())
                .param("themeId", 1)
                .log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("get(0).isBooked", is(true));

        jdbcTemplate.update("DELETE FROM reservation");

        RestAssured.given()
                .param("date", NEXT_DAY.toString())
                .param("themeId", 1)
                .log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("get(0).isBooked", is(false));
    }

    @DisplayName("예약 가능한 시간을 삭제할 수 있다")
    @Test
    void canDeleteReservationTime() {
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", LocalTime.now().toString());

        RestAssured.given().log().all()
                .when().delete("/times/1")
                .then().log().all()
                .statusCode(204);
    }

    @DisplayName("이미 해당 시간에 대해 예약 데이터가 존재한다면 삭제가 불가능하다")
    @Test
    void cannotDeleteReservationTimeWhenExistReservation() {
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", LocalTime.now().toString());
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)",
                "테마", "설명", "썸네일");
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                "예약", NEXT_DAY, 1, 1);

        RestAssured.given().log().all()
                .when().delete("/times/1")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("테마를 추가할 수 있다")
    @Test
    void canCreateTheme() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "이름");
        params.put("description", "설명");
        params.put("thumbnail", "썸네일");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201)
                .header("location", "/theme/1");
    }

    @DisplayName("유효하지 않은 요청으로는 테마를 추가할 수 없다")
    @Test
    void cannotCreateThemeWhenInvalidRequest() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "이름");
        params.put("description", "");
        params.put("thumbnail", "썸네일");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/themes")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("모든 테마를 조회할 수 있다")
    @Test
    void canResponseAllTheme() {
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)",
                "테마1", "설명", "썸네일");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)",
                "테마2", "설명", "썸네일");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)",
                "테마3", "설명", "썸네일");

        RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(3));
    }

    @DisplayName("인기 테마를 조회할 수 있다")
    @Test
    void canResponseTopThemes() {
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)",
                "인기테마", "설명1", "썸네일1");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)",
                "평범테마", "설명2", "썸네일2");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)",
                "인기없는테마", "설명3", "썸네일3");
        for (int i = 0; i < 10; i++) {
            jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)",
                    "테마" + i, "설명", "썸네일");
        }

        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", LocalTime.of(10, 0).toString());
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", LocalTime.of(11, 0).toString());
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", LocalTime.of(12, 0).toString());

        jdbcTemplate.update("insert into reservation (name, date, time_id, theme_id) values (?, ?, ?, ?)",
                "예약", YESTERDAY.toString(), 1, 1);
        jdbcTemplate.update("insert into reservation (name, date, time_id, theme_id) values (?, ?, ?, ?)",
                "예약", YESTERDAY.toString(), 2, 1);
        jdbcTemplate.update("insert into reservation (name, date, time_id, theme_id) values (?, ?, ?, ?)",
                "예약", YESTERDAY.toString(), 3, 1);
        jdbcTemplate.update("insert into reservation (name, date, time_id, theme_id) values (?, ?, ?, ?)",
                "예약", YESTERDAY.toString(), 1, 2);
        jdbcTemplate.update("insert into reservation (name, date, time_id, theme_id) values (?, ?, ?, ?)",
                "예약", YESTERDAY.toString(), 2, 2);
        jdbcTemplate.update("insert into reservation (name, date, time_id, theme_id) values (?, ?, ?, ?)",
                "예약", YESTERDAY.toString(), 1, 3);

        RestAssured.given().log().all()
                .when().get("/themes/top")
                .then().log().all()
                .statusCode(200)
                .body("get(0).name", is("인기테마"))
                .body("get(1).name", is("평범테마"))
                .body("get(2).name", is("인기없는테마"))
                .body("size()", is(10));
    }

    @DisplayName("ID를 통해 테마를 삭제할 수 있다")
    @Test
    void canDeleteThemeById() {
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)",
                "테마", "설명", "썸네일");

        RestAssured.given().log().all()
                .when().delete("/themes/1")
                .then().log().all()
                .statusCode(204);
    }

    @DisplayName("이미 테마에 대한 예약이 존재한다면 해당 테마의 삭제가 불가능하다")
    @Test
    void cannotDeleteThemeByIdWhenReservationExist() {
        jdbcTemplate.update("INSERT INTO reservation_time (start_at) VALUES (?)", LocalTime.now().toString());
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES (?, ?, ?)",
                "테마", "설명", "썸네일");
        jdbcTemplate.update("INSERT INTO reservation (name, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                "예약", NEXT_DAY.toString(), 1, 1);

        RestAssured.given().log().all()
                .when().delete("/themes/1")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("로그인할 수 있다")
    @Test
    void canLogin() {
        jdbcTemplate.update("INSERT INTO member (name, email, password) VALUES (?,?,?)",
                "회원", "test@test.com", "ecxewqe!23");

        Map<String, String> params = new HashMap<>();
        params.put("email", "test@test.com");
        params.put("password", "ecxewqe!23");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/login")
                .then().log().all()
                .statusCode(200)
                .cookie("access", notNullValue());
    }

    @DisplayName("이메일에 대한 회원이 존재하지 않을 경우 로그인할 수 없다")
    @Test
    void cannotLoginBecauseOfInvalidEmail() {
        Map<String, String> params = new HashMap<>();
        params.put("email", "test@test.com");
        params.put("password", "ecxewqe!23");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/login")
                .then().log().all()
                .statusCode(401);
    }

    @DisplayName("비밀번호가 맞지 않을 경우 로그인할 수 없다")
    @Test
    void cannotLoginBecauseOfWrongPassword() {
        jdbcTemplate.update("INSERT INTO member (name, email, password) VALUES (?,?,?)",
                "회원", "test@test.com", "ecxewqe!23");

        Map<String, String> params = new HashMap<>();
        params.put("email", "test@test.com");
        params.put("password", "wrong_password_123!");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/login")
                .then().log().all()
                .statusCode(401);
    }

    @DisplayName("로그인 여부를 확인할 수 있다")
    @Test
    void canCheckLogin() {
        String accessToken = jwtTokenProvider.makeAccessToken(1L, "회원");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("access", accessToken)
                .when().get("/login/check")
                .then().log().all()
                .statusCode(200)
                .body("name", is("회원"));
    }

    @DisplayName("토큰이 올바르지 않은 경우 예외가 발생한다")
    @Test
    void cannotCheckLoginBecauseOfInvalidToken() {
        String accessToken = jwtTokenProvider.makeAccessToken(1L, "회원");
        String invalidToken = accessToken + "invalid";

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("access", invalidToken)
                .when().get("/login/check")
                .then().log().all()
                .statusCode(401);
    }

    @DisplayName("토큰이 만료된 경우 예외가 발생한다")
    @Test
    void cannotCheckLoginBecauseOfTokenExpiration() {
        Date validity = new Date(new Date().getTime() - 1000);
        String expirationToken = Jwts.builder()
                .setSubject(String.valueOf(1L))
                .claim("name", "회원")
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, tokenSecretKey)
                .compact();

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("access", expirationToken)
                .when().get("/login/check")
                .then().log().all()
                .statusCode(401);
    }

    @DisplayName("로그아웃을 할 수 있다")
    @Test
    void canLogout() {
        String accessToken = jwtTokenProvider.makeAccessToken(1L, "회원");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("access", accessToken)
                .when().post("/logout")
                .then().log().all()
                .statusCode(200);
    }
}
