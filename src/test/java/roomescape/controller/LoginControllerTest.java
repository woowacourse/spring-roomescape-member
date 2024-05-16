package roomescape.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import roomescape.dto.LoginRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql("/testdata.sql")
class LoginControllerTest {

    @Test
    @DisplayName("로그인을 할 수 있다.")
    void login() {
        final LoginRequest loginRequest = new LoginRequest("123@email.com", "123");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(loginRequest)
                .when().post("/login")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("로그아웃을 할 수 있다.")
    void logout() {
        final LoginRequest loginRequest = new LoginRequest("123@email.com", "123");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(loginRequest)
                .when().post("/login")
                .then().log().all()
                .statusCode(200);

        RestAssured.given().log().all()
                .when().post("/logout")
                .then().log().all()
                .statusCode(200);
    }
}
