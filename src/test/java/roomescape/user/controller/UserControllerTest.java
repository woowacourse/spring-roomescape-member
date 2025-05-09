package roomescape.user.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
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

    @Test
    void login() {
        // given
        userService.add(UserFixture.createRequestDto("name", EMAIL, PASSWORD));

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
}
