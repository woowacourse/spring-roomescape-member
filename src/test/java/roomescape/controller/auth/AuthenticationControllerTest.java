package roomescape.controller.auth;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.when;

import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.response.MockMvcResponse;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.dto.auth.LoginCheckResponse;
import roomescape.dto.auth.LoginRequest;
import roomescape.service.auth.AuthenticationService;

@WebMvcTest(AuthenticationController.class)
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthenticationService service;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @Test
    @DisplayName("로그인 성공 시, 쿠키를 반환해야한다")
    void login_ShouldSetTokenCookie() {
        // given
        String email = "user1@example.com";
        String password = "user1123";
        String expectedToken = "generated-jwt-token";
        LoginRequest request = new LoginRequest(email, password);

        when(service.login(request)).thenReturn(expectedToken);
        // when & then
        MockMvcResponse response = RestAssuredMockMvc.given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/auth/login")
                .then()
                .status(HttpStatus.OK)
                .contentType(ContentType.JSON)
                .header("Keep-Alive", "timeout=60")
                .cookie("token", expectedToken)
                .extract().response();

        Cookie tokenCookie = response.getMockHttpServletResponse().getCookie("token");
        assertThat(tokenCookie.getPath()).isEqualTo("/");
        assertThat(tokenCookie.isHttpOnly()).isTrue();
    }

    @Test
    @DisplayName("로그인 체크 시, 토큰이 없다면 401 코드를 반환해야한다")
    void loginCheck_ShouldReturnUnauthorizedWhenNoToken() {
        // when & then
        RestAssuredMockMvc.given()
                .when()
                .get("/auth/login/check")
                .then()
                .status(HttpStatus.UNAUTHORIZED)
                .contentType(ContentType.JSON)
                .body("name", org.hamcrest.Matchers.nullValue());
    }

    @Test
    @DisplayName("로그인 체크 시, 유효한 토큰이 있다면, name 값을 반환해야한다")
    void loginCheck_ShouldReturnUserNameWhenTokenIsValid() {
        // given
        String token = "valid-jwt-token";
        String expectedName = "user1";

        when(service.findNameByToken(token)).thenReturn(expectedName);

        // when & then
        MockMvcResponse response = RestAssuredMockMvc.given()
                .cookie("token", token)
                .when()
                .get("/auth/login/check")
                .then()
                .status(HttpStatus.OK)
                .contentType(ContentType.JSON)
                .header("Connection", "keep-alive")
                .header("Keep-Alive", "timeout=60")
                .body("name", equalTo(expectedName))
                .extract().response();

        LoginCheckResponse responseBody = response.as(LoginCheckResponse.class);
        assertThat(responseBody.name()).isEqualTo(expectedName);
    }
}
