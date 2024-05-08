package roomescape.api;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.test.context.jdbc.Sql;
import roomescape.dto.LoginCheckResponse;
import roomescape.dto.LoginRequest;

@Sql("/member-test-data.sql")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class AuthApiTest {

    private static final String NAME = "ted";
    private static final String EMAIL = "test@email.com";
    private static final String PASSWORD = "123456";

    @LocalServerPort
    int port;

    @Test
    void 토근_로그인() {
        RestAssured
                .given().log().all()
                .port(port)
                .body(new LoginRequest(EMAIL, PASSWORD))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login")
                .then().log().all()
                .assertThat().cookie("token");
    }

    @Test
    void 로그인_사용자_조회() {
        String accessToken = getAccessTokenByLogin();
        ResponseCookie cookie = createCookieByAccessToken(accessToken);

        LoginCheckResponse response = RestAssured
                .given().log().all()
                .port(port)
                .cookie(cookie.toString())
                .auth().oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/login/check")
                .then().log().all()
                .statusCode(HttpStatus.OK.value()).extract().as(LoginCheckResponse.class);

        assertThat(response.name()).isEqualTo(NAME);
    }

    private String getAccessTokenByLogin() {
        return RestAssured
                .given().log().all()
                .port(port)
                .body(new LoginRequest(EMAIL, PASSWORD))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login")
                .then().log().all()
                .extract().cookie("token");
    }

    private ResponseCookie createCookieByAccessToken(String accessToken) {
        return ResponseCookie
                .from("token", accessToken)
                .path("/")
                .httpOnly(true)
                .build();
    }
}
