package roomescape.user.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import roomescape.auth.TokenRequestDto;
import roomescape.user.fixture.UserFixture;
import roomescape.user.service.UserService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class UserControllerTest {

    private static final String EMAIL = "user@email.com";
    private static final String PASSWORD = "password";

    @Autowired
    private UserService userService;

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Nested
    @DisplayName("로그인 기능")
    class login {

        @BeforeEach
        void setUPLogin() {
            userService.add(UserFixture.createRequestDto("name", EMAIL, PASSWORD));
        }

        @DisplayName("유효한 이메일과 비밀번호로 로그인을 성공한다.")
        @Test
        void login_success() {
            // given

            // when
            TokenRequestDto requestDto = new TokenRequestDto(EMAIL, PASSWORD);

            // then
            RestAssured
                    .given().log().all()
                    .contentType(ContentType.JSON)
                    .body(requestDto)
                    .when().post("/users/login")
                    .then().log().all()
                    .statusCode(HttpStatus.OK.value())
                    .extract().response();
        }

        @DisplayName("유효하지 않는 이메일로 로그인을 시도하여 실패:  상태 코드 404를 반환한다.")
        @Test
        void login_throwException_byInvalidEmail() {
            // given

            // when
            TokenRequestDto requestDto = new TokenRequestDto("invalidEmail@example.com", PASSWORD);

            // then
            RestAssured
                    .given().log().all()
                    .contentType(ContentType.JSON)
                    .body(requestDto)
                    .when().post("/users/login")
                    .then().log().all()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .extract().response();
        }
    }


}
