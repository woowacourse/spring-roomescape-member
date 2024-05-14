package roomescape.controller.view;

import io.restassured.RestAssured;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.config.JwtTokenProvider;
import roomescape.domain.Role;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AdminPageControllerTest {

    @Autowired
    private JwtTokenProvider tokenProvider;

    @LocalServerPort
    int port;

    @BeforeEach
    void setPort() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("/admin/** 페이지의 경우 ADMIN 권한인 사용자는 접속이 가능하다.")
    void reservationPage_admin() {
        // given
        String token = tokenProvider.createToken(1L, "관리자", Role.ADMIN.name());
        Map<String, String> cookies = Map.of(JwtTokenProvider.TOKEN_COOKIE_NAME, token);

        // when  && then
        RestAssured.given().log().all()
                .cookies(cookies)
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("/admin/** 페이지의 경우 ADMIN 권한이 아닌 사용자는 접근 권한 제한을 받는다.")
    void reservationPage_member_401() {
        // given
        String token = tokenProvider.createToken(1L, "사용자1", Role.MEMBER.name());
        Map<String, String> cookies = Map.of(JwtTokenProvider.TOKEN_COOKIE_NAME, token);

        // when  && then
        RestAssured.given().log().all()
                .cookies(cookies)
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(401);
    }

    @Test
    @DisplayName("/admin/** 페이지의 경우 로그인하지 않은 사용자는 접근 권한 제한을 받는다.")
    void reservationPage_notLogin_401() {
        RestAssured.given().log().all()
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(401);
    }
}
