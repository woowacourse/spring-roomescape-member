package roomescape.view.controller;


import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import roomescape.member.dao.MemberDao;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;

import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/truncate.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class AdminPageControllerTest {

    @Autowired
    private MemberDao memberDao;

    @LocalServerPort
    private int port;

    String accessTokenCookie;

    @Test
    @DisplayName("관리자 권한이 있는 유저가 /admin 으로 GET 요청을 보내면 어드민 페이지와 200 OK 를 받는다.")
    void getAdminPageHasRole() {
        // given
        memberDao.insert(new Member("이름", "admin@admin.com", "1234admin", Role.ADMIN));
        Map<String, String> loginParams = Map.of(
                "email", "admin@admin.com",
                "password", "1234admin"
        );

        accessTokenCookie = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .port(port)
                .body(loginParams)
                .when().post("/login")
                .then().log().all().extract().header("Set-Cookie").split(";")[0];

        // when & then
        RestAssured.given().log().all()
                .port(port)
                .header(new Header("Cookie", accessTokenCookie))
                .when().get("/admin")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("관리자 권한이 없는 유저가 /admin 으로 GET 요청을 보내면 어드민 페이지와 200 OK 를 받는다.")
    void getAdminPageHasNotRole() {
        // given
        memberDao.insert(new Member("이름", "admin@admin.com", "1234admin", Role.MEMBER));
        Map<String, String> loginParams = Map.of(
                "email", "admin@admin.com",
                "password", "1234admin"
        );

        accessTokenCookie = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .port(port)
                .body(loginParams)
                .when().post("/login")
                .then().log().all().extract().header("Set-Cookie").split(";")[0];

        // when & then
        RestAssured.given().log().all()
                .port(port)
                .header(new Header("Cookie", accessTokenCookie))
                .when().get("/admin")
                .then().log().all()
                .statusCode(403);
    }

    @Test
    @DisplayName("/admin/reservation 으로 GET 요청을 보내면 어드민 예약 관리 페이지와 200 OK 를 받는다.")
    void getAdminReservationPageHasRole() {
        // given
        memberDao.insert(new Member("이름", "admin@admin.com", "1234admin", Role.ADMIN));
        Map<String, String> loginParams = Map.of(
                "email", "admin@admin.com",
                "password", "1234admin"
        );

        accessTokenCookie = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .port(port)
                .body(loginParams)
                .when().post("/login")
                .then().log().all().extract().header("Set-Cookie").split(";")[0];

        // when & then
        RestAssured.given().log().all()
                .port(port)
                .header(new Header("Cookie", accessTokenCookie))
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("관리자 권한이 없는 유저가 /admin/reservation 으로 GET 요청을 보내면 어드민 예약 관리 페이지와 200 OK 를 받는다.")
    void getAdminReservationPageHasNotRole() {
        // given
        memberDao.insert(new Member("이름", "admin@admin.com", "1234admin", Role.MEMBER));
        Map<String, String> loginParams = Map.of(
                "email", "admin@admin.com",
                "password", "1234admin"
        );

        accessTokenCookie = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .port(port)
                .body(loginParams)
                .when().post("/login")
                .then().log().all().extract().header("Set-Cookie").split(";")[0];

        // when & then
        RestAssured.given().log().all()
                .port(port)
                .header(new Header("Cookie", accessTokenCookie))
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(403);
    }

    @Test
    @DisplayName("/admin/time 으로 GET 요청을 보내면 어드민 예약 시간 관리 페이지와 200 OK 를 받는다.")
    void getAdminTimePageHasRole() {
        // given
        memberDao.insert(new Member("이름", "admin@admin.com", "1234admin", Role.ADMIN));
        Map<String, String> loginParams = Map.of(
                "email", "admin@admin.com",
                "password", "1234admin"
        );

        accessTokenCookie = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .port(port)
                .body(loginParams)
                .when().post("/login")
                .then().log().all().extract().header("Set-Cookie").split(";")[0];

        // when & then
        RestAssured.given().log().all()
                .port(port)
                .header(new Header("Cookie", accessTokenCookie))
                .when().get("/admin/time")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("/admin/time 으로 GET 요청을 보내면 403 Forbidden 을 받는다.")
    void getAdminTimePageHasNotRole() {
        // given
        memberDao.insert(new Member("이름", "admin@admin.com", "1234admin", Role.MEMBER));
        Map<String, String> loginParams = Map.of(
                "email", "admin@admin.com",
                "password", "1234admin"
        );

        accessTokenCookie = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .port(port)
                .body(loginParams)
                .when().post("/login")
                .then().log().all().extract().header("Set-Cookie").split(";")[0];

        // when & then
        RestAssured.given().log().all()
                .port(port)
                .header(new Header("Cookie", accessTokenCookie))
                .when().get("/admin/time")
                .then().log().all()
                .statusCode(403);
    }

    @Test
    @DisplayName("관리자 권한이 있는 유저가 /admin/theme 으로 GET 요청을 보내면 어드민 테마 관리 페이지와 200 OK 를 받는다.")
    void getAdminThemePageHasRole() {
        // given
        memberDao.insert(new Member("이름", "admin@admin.com", "1234admin", Role.ADMIN));
        Map<String, String> loginParams = Map.of(
                "email", "admin@admin.com",
                "password", "1234admin"
        );

        accessTokenCookie = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .port(port)
                .body(loginParams)
                .when().post("/login")
                .then().log().all().extract().header("Set-Cookie").split(";")[0];

        // when & then
        RestAssured.given().log().all()
                .port(port)
                .header(new Header("Cookie", accessTokenCookie))
                .when().get("/admin/theme")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("관리자 권한이 없는 유저가 /admin/theme 으로 GET 요청을 보내면 403 Forbidden 을 받는다.")
    void getAdminThemePageHasNotRole() {
        // given
        memberDao.insert(new Member("이름", "admin@admin.com", "1234admin", Role.MEMBER));
        Map<String, String> loginParams = Map.of(
                "email", "admin@admin.com",
                "password", "1234admin"
        );

        accessTokenCookie = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .port(port)
                .body(loginParams)
                .when().post("/login")
                .then().log().all().extract().header("Set-Cookie").split(";")[0];

        // when & then
        RestAssured.given().log().all()
                .port(port)
                .header(new Header("Cookie", accessTokenCookie))
                .when().get("/admin/theme")
                .then().log().all()
                .statusCode(403);
    }
}
