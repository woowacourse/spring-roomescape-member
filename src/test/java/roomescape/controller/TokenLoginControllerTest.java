package roomescape.controller;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseCookie;
import org.springframework.test.context.jdbc.Sql;
import roomescape.application.AuthService;
import roomescape.dto.auth.TokenRequest;

@Sql("/clear.sql")
@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class TokenLoginControllerTest {
    @MockBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void 로그인에_성공한다() throws Exception {
        TokenRequest tokenRequest = new TokenRequest("lemone1234", "lemone@wooteco.com");
        when(authService.createCookie(any())).thenReturn(
                ResponseCookie.from("token", "testtoken").httpOnly(true).build());

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(tokenRequest))
                .when().post("/login")
                .then().statusCode(200)
                .header("Keep-Alive", "timeout=60")
                .header("Set-Cookie", containsString("testtoken"))
                .header("Set-Cookie", containsString("HttpOnly"));
    }
}
