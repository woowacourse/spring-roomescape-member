package roomescape.service;

import static org.junit.jupiter.api.Assertions.*;

import groovy.util.logging.Log;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import roomescape.dto.LoginRequest;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AuthenticationServiceTest {

    @LocalServerPort
    int port;

    @Autowired
    AuthenticationService authenticationService;

    @Test
    void 토큰_생성시_이메일이_존재하지_않으면_예외가_발생한다() {
        LoginRequest loginRequest = new LoginRequest("noSuchEmail@abc.com", "abcdefgh");
        Assertions.assertThatThrownBy(() -> authenticationService.generateToken(loginRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(String.format("%s, 이메일과 일치하는 멤버가 존재하지 않습니다.", loginRequest.email()));
    }

    @Test
    void 토큰_생성시_비밀번호가_일치하지_않으면_예외가_발생한다() {
        LoginRequest loginRequest = new LoginRequest("admin@admin.com", "noSuchPassword");
        Assertions.assertThatThrownBy(() -> authenticationService.generateToken(loginRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("비밀번호가 일치하지 않습니다.");
    }

    @Test
    void 토큰으로_멤버를_찾을_수_있다() {
        LoginRequest loginRequest = new LoginRequest("admin@admin.com", "adminadmin");
        Cookie adminCookie = RestAssured.given()
                .port(port)
                .contentType(ContentType.JSON)
                .body(loginRequest)
                .post("/login")
                .getDetailedCookie("token");
        Assertions.assertThatCode(() -> authenticationService.findByToken(adminCookie.getValue()))
                .doesNotThrowAnyException();
    }
}
