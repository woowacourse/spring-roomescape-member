package roomescape.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import roomescape.auth.service.AuthService;
import roomescape.member.dao.MemberDao;
import roomescape.member.domain.Member;

import java.util.Map;

import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/truncate.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class AuthControllerTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private MemberDao memberDao;

    @LocalServerPort
    private int port;

    @Test
    @DisplayName("로그인에 성공하면 JWT accessToken을 Response 받는다.")
    void getJwtAccessTokenWhenlogin() {
        // given
        String email = "test@email.com";
        String password = "12341234";
        memberDao.insert(new Member("이름", email, password));

        Map<String, String> loginParams = Map.of(
                "email", email,
                "password", password
        );

        // when
        String cookie = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .port(port)
                .body(loginParams)
                .when().post("/login")
                .then().log().all().extract().header("Set-Cookie").split(";")[0];

        // then
        String tokenResponsePrefix = "token=";
        Assertions.assertThat(cookie.startsWith(tokenResponsePrefix)).isTrue();
    }

    @Test
    @DisplayName("로그인 검증 시, 회원의 name을 응답 받는다.")
    void checkLogin() {
        // given
        String name = "이름";
        String email = "test@email.com";
        String password = "12341234";
        memberDao.insert(new Member(name, email, password));

        Map<String, String> loginParams = Map.of(
                "email", email,
                "password", password
        );

        // when
        String tokenCookie = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .port(port)
                .body(loginParams)
                .when().post("/login")
                .then().log().all().extract().header("Set-Cookie").split(";")[0];

        // then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .port(port)
                .header("cookie", tokenCookie)
                .when().get("/login/check")
                .then()
                .body("data.name", is("이름"));
    }
}
