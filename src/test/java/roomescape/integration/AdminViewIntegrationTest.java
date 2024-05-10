package roomescape.integration;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import roomescape.dto.member.MemberLoginRequest;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql(value = "classpath:clean_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class AdminViewIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private String token;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Nested
    @DisplayName("관리자 접근시")
    class AdminAccess {

        @BeforeEach
        void setUp() {
            jdbcTemplate.update(
                    "INSERT INTO member (name, email, password, role) VALUES ('관리자1', 'admin1@wooteco.com', 'admin1', 'ADMIN')");

            token =  RestAssured.given()
                    .body(MemberLoginRequest.of("admin1", "admin1@wooteco.com"))
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when().post("/login")
                    .getHeader("Set-Cookie");
        }

        @Test
        @DisplayName("홈 화면을 요청하면 200 OK을 응답한다.")
        void adminPageTest() throws Exception {
            RestAssured.given().log().all()
                    .header("Cookie", token)
                    .when().get("/admin")
                    .then().log().all()
                    .statusCode(200);
        }

        @Test
        @DisplayName("예약 관리 페이지를 요청하면 200 OK를 반환한다.")
        void reservationPageTest() throws Exception {
            RestAssured.given().log().all()
                    .header("Cookie", token)
                    .when().get("/admin/reservation")
                    .then().log().all()
                    .statusCode(200);
        }

        @Test
        @DisplayName("테마 관리 페이지를 요청하면 200 OK를 반환한다.")
        void themePageTest() throws Exception {
            RestAssured.given().log().all()
                    .header("Cookie", token)
                    .when().get("/admin/theme")
                    .then().log().all()
                    .statusCode(200);
        }
    }

    @Nested
    @DisplayName("비관리자 접근시")
    class NotAdminAccess {

        @BeforeEach
        void setUp() {
            jdbcTemplate.update(
                    "INSERT INTO member (name, email, password, role) VALUES ('사용자1', 'user1@wooteco.com', 'user1', 'USER')");

            token =  RestAssured.given()
                    .body(MemberLoginRequest.of("user1", "user1@wooteco.com"))
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when().post("/login")
                    .getHeader("Set-Cookie");
        }

        @Test
        @DisplayName("홈 화면을 요청하면 401 UNAUTHORIZED을 응답한다.")
        void adminPageTest() throws Exception {
            RestAssured.given().log().all()
                    .header("Cookie", token)
                    .when().get("/admin")
                    .then().log().all()
                    .statusCode(401);
        }

        @Test
        @DisplayName("예약 관리 페이지를 요청하면 401 UNAUTHORIZED를 반환한다.")
        void reservationPageTest() throws Exception {
            RestAssured.given().log().all()
                    .header("Cookie", token)
                    .when().get("/admin/reservation")
                    .then().log().all()
                    .statusCode(401);
        }

        @Test
        @DisplayName("테마 관리 페이지를 요청하면 401 UNAUTHORIZED를 반환한다.")
        void themePageTest() throws Exception {
            RestAssured.given().log().all()
                    .header("Cookie", token)
                    .when().get("/admin/theme")
                    .then().log().all()
                    .statusCode(401);
        }
    }
}
