package roomescape;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertAll;
import static roomescape.testFixture.Fixture.RESERVATION_1;
import static roomescape.testFixture.Fixture.RESERVATION_TIME_1;
import static roomescape.testFixture.Fixture.RESERVATION_TIME_2;
import static roomescape.testFixture.Fixture.RESERVATION_TIME_3;
import static roomescape.testFixture.Fixture.THEME_1;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.sql.Connection;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.auth.dto.MemberAuthResponse;
import roomescape.auth.dto.TokenRequest;
import roomescape.domain.repository.dto.TimeDataWithBookingInfo;
import roomescape.testFixture.JdbcHelper;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RoomescapeApplicationTest {

    @LocalServerPort
    int port;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void cleanDatabase() {
        RestAssured.port = port;

        jdbcTemplate.execute((Connection connection) -> {
            try (Statement statement = connection.createStatement()) {
                statement.execute("SET REFERENTIAL_INTEGRITY FALSE");
                statement.execute("TRUNCATE TABLE reservation");
                statement.execute("ALTER TABLE reservation ALTER COLUMN id RESTART WITH 1");
                statement.execute("TRUNCATE TABLE reservation_time");
                statement.execute("ALTER TABLE reservation_time ALTER COLUMN id RESTART WITH 1");
                statement.execute("TRUNCATE TABLE theme");
                statement.execute("ALTER TABLE theme ALTER COLUMN id RESTART WITH 1");
                statement.execute("TRUNCATE TABLE member");
                statement.execute("ALTER TABLE member ALTER COLUMN id RESTART WITH 1");
                statement.execute("SET REFERENTIAL_INTEGRITY TRUE");
            }
            return null;
        });
    }

    @DisplayName("스프링 컨텍스트 로딩 성공")
    @Test
    void contextLoads() {
    }

    @DisplayName("테마 조회 요청 시 모든 테마를 응답한다")
    @Test
    void getAllThemes() {
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES ('테마1', '테마 1입니다.', '썸네일입니다.')");
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES ('테마2', '테마 2입니다.', '썸네일입니다.')");

        int themesCount = getThemesCount();

        RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(themesCount));
    }

    @DisplayName("테마 생성 요청 시 생성된 테마 정보를 응답한다")
    @Test
    void createTheme() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("name", "레벨2 탈출");
        params.put("description", "레벨2 탈출입니다.");
        params.put("thumbnail", "썸네일");

        // when & then
        ExtractableResponse<Response> extractedResponse = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201).extract();

        assertAll(
                () -> assertThat(extractedResponse.jsonPath().getString("name")).isEqualTo(params.get("name")),
                () -> assertThat(extractedResponse.jsonPath().getString("description")).isEqualTo(
                        params.get("description")),
                () -> assertThat(extractedResponse.jsonPath().getString("thumbnail")).isEqualTo(params.get("thumbnail"))
        );
    }

    @DisplayName("테마 삭제 요청 시 해당하는 id의 테마를 삭제한다")
    @Test
    void delete() {
        //given
        jdbcTemplate.update("INSERT INTO theme (name, description, thumbnail) VALUES ('테마1', '테마 1입니다.', '썸네일입니다.')");

        int addedCount = getThemesCount();

        assertThat(addedCount).isEqualTo(1);

        //when & then
        RestAssured.given().log().all()
                .when().delete("/themes/1")
                .then().log().all()
                .statusCode(204);

        int deletedCount = getThemesCount();
        assertThat(deletedCount).isEqualTo(0);
    }

    @DisplayName("테마와 날짜 선택 후 예약 가능한 시간 조회 요청")
    @Test
    void getTimesWithBookingInfo() {
        JdbcHelper.insertTheme(jdbcTemplate, THEME_1);
        JdbcHelper.insertReservationTimes(jdbcTemplate, RESERVATION_TIME_1, RESERVATION_TIME_2, RESERVATION_TIME_3);
        JdbcHelper.insertReservation(jdbcTemplate, RESERVATION_1);

        String date = RESERVATION_1.getReservationDate().toString();

        List<TimeDataWithBookingInfo> timesData = RestAssured.given().log().all()
                .when().get(String.format("/times/booking-status?date=%s&themeId=%d", date, THEME_1.getId()))
                .then().log().all()
                .statusCode(200)
                .extract()
                .body().jsonPath().getList(".", TimeDataWithBookingInfo.class);

        long bookedCount = timesData.stream()
                .filter(TimeDataWithBookingInfo::alreadyBooked)
                .count();

        assertAll(
                () -> assertThat(timesData).hasSize(3),
                () -> assertThat(bookedCount).isEqualTo(1)
        );
    }

    private int getThemesCount() {
        int themesCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM theme", Integer.class);
        return themesCount;
    }

    @DisplayName("토큰으로 로그인 성공")
    @Test
    void tokenLogin() {
        jdbcTemplate.update("INSERT INTO member (name, email, password) VALUES ('어드민', 'admin@email.com', 'password')");

        String cookie = RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .body(new TokenRequest("admin@email.com", "password"))
                .when().post("/login")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().header("Set-Cookie").split(";")[0].substring("token=".length());

        MemberAuthResponse member = RestAssured
                .given().log().all()
                .cookie("token", cookie)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/login/check")
                .then().log().all()
                .statusCode(HttpStatus.OK.value()).extract().as(MemberAuthResponse.class);

        assertThat(member.name()).isEqualTo("어드민");
    }

    @DisplayName("회원가입 성공")
    @Test
    void registerMemberTest() {
        int beforeSize = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM member", Integer.class);

        Map<String, String> params = new HashMap<>();
        params.put("name", "어드민");
        params.put("email", "admin@email.com");
        params.put("password", "password");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/members")
                .then().log().all()
                .statusCode(201)
                .body("id", is(beforeSize + 1));
    }
}
