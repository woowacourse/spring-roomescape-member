package roomescape;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.common.Role;
import roomescape.common.auth.JwtProvider;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.ThemeDao;
import roomescape.controller.reservation.dto.ReservationResponseDto;
import roomescape.model.Member;
import roomescape.model.ReservationTime;
import roomescape.model.Theme;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MissionStepTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ReservationTimeDao reservationTimeDao;

    @Autowired
    private ThemeDao themeDao;

    @Autowired
    private JwtProvider jwtProvider;

    @BeforeEach
    void beforeEachTest() {
        reservationTimeDao.saveTime(new ReservationTime(LocalTime.of(10, 10)));
        themeDao.saveTheme(new Theme("adad", "무서워요", "image"));
    }

    @DisplayName("admin이 관리자 페이지 GET 요청 시 200 OK를 반환한다")
    @Test
    void 관리자_페이지_접근_성공() {
        String adminToken = jwtProvider.createToken(new Member(1L, "다로", "qwe", "1234",Role.ADMIN)); // 테스트용 JWT 생성
        RestAssured.given().log().all()
                .cookie("token", adminToken)
                .when().get("/admin")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("user가 관리자 페이지 GET 요청 시 403을 반환한다")
    @Test
    void 관리자_페이지_접근_실패() {
        String userToken = jwtProvider.createToken(
                new Member(2L, "포로", "asd", "1234", Role.USER)
        );
        RestAssured.given().log().all()
                .cookie("token", userToken)
                .when().get("/admin")
                .then().log().all()
                .statusCode(403);
    }

    @DisplayName("admin은 GET /admin/reservation 및 /reservations 요청 시 200 OK와 빈 목록 반환 확인")
    @Test
    void reservation_접근_성공() {
        String adminToken = jwtProvider.createToken(new Member(1L, "다로", "qwe", "1234",Role.ADMIN)); // 테스트용 JWT 생성
        RestAssured.given().log().all()
                .cookie("token", adminToken)
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(200);

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(3));
    }

    @DisplayName("user GET /admin/reservation 요청 시 403 실패")
    @Test
    void reservation_접근_실패() {
        String userToken = jwtProvider.createToken(new Member(2L, "다로", "mail", "pass",Role.USER)); // 테스트용 JWT 생성
        RestAssured.given().log().all()
                .cookie("token", userToken)
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(403);
    }


    @DisplayName("예약 생성 후 조회 및 삭제까지의 전체 흐름 테스트")
    @Test
    void 삼단계() {
        Map<String, String> params = new HashMap<>();
        params.put("date", String.valueOf(LocalDate.now().plusDays(1)));
        params.put("timeId", "1");
        params.put("themeId", "1");

        String adminToken = jwtProvider.createToken(new Member(1L, "다로", "qwe", "1234",Role.ADMIN)); // 테스트용 JWT 생성

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", adminToken)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .body("id", is(4));

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(4));

        RestAssured.given().log().all()
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(204);

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(3));
    }

    @DisplayName("DB 연결 및 RESERVATION 테이블 존재 여부 확인")
    @Test
    void 사단계() {
        try (Connection connection = jdbcTemplate.getDataSource().getConnection()) {
            assertThat(connection).isNotNull();
            assertThat(connection.getCatalog()).isEqualTo("DATABASE");
            assertThat(connection.getMetaData().getTables(null, null, "RESERVATION", null).next()).isTrue();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @DisplayName("조회한 예약 수와 데이터베이스 쿼리를 통해 조회한 예약 수가 같은지 비교하는 테스트")
    @Test
    void 오단계() {
        jdbcTemplate.update(
                "INSERT INTO reservation (member_id, date, time_id, theme_id) VALUES (?, ?, ?, ?)",
                "1", String.valueOf(LocalDate.now().plusDays(1)), "1", "1");

        List<ReservationResponseDto> reservations = RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200).extract()
                .jsonPath().getList(".", ReservationResponseDto.class);

        Integer count = jdbcTemplate.queryForObject("SELECT count(1) from reservation", Integer.class);

        assertThat(reservations.size()).isEqualTo(count);
    }

    @DisplayName("예약 추가 및 삭제 테스트")
    @Test
    void 육단계() {
        Map<String, String> params = new HashMap<>();
        params.put("memberId", "1");
        params.put("date", String.valueOf(LocalDate.now().plusDays(1)));
        params.put("timeId", "1");
        params.put("themeId", "1");

        JwtProvider jwtProvider = new JwtProvider();
        String token = jwtProvider.createToken(new Member(1L, "조로", "emai","1234", Role.ADMIN));

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        Integer count = jdbcTemplate.queryForObject("SELECT count(1) from reservation", Integer.class);
        assertThat(count).isEqualTo(4);

        RestAssured.given().log().all()
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(204);

        Integer countAfterDelete = jdbcTemplate.queryForObject("SELECT count(1) from reservation", Integer.class);
        assertThat(countAfterDelete).isEqualTo(3);
    }

    @DisplayName("상영시간을 등록하고 조회하면 목록에 포함되며 삭제도 가능하다")
    @Test
    void 칠단계() {
        Map<String, String> params = new HashMap<>();
        params.put("startAt", "10:55");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(5));

        RestAssured.given().log().all()
                .when().delete("/times/5")
                .then().log().all()
                .statusCode(204);
    }

    @DisplayName("예약 추가 이후 예약 개수 확인 테스트")
    @Test
    void 팔단계() {
        Map<String, Object> reservation = new HashMap<>();
        reservation.put("memberId", "1");
        reservation.put("date", String.valueOf(LocalDate.now().plusDays(1)));
        reservation.put("timeId", 1);
        reservation.put("themeId", 1);

        String adminToken = jwtProvider.createToken(new Member(1L, "다로", "qwe", "1234",Role.ADMIN)); // 테스트용 JWT 생성

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", adminToken)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(4));
    }
}
