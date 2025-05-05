package roomescape.admin.controller;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import roomescape.dto.member.MemberResponseDto;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
class AuthenticationControllerTest {


    @Test
    void 회원가입_테스트() {
        Map<String, String> signupParam = Map.of("name", "투다", "email", "signup@email.com", "password", "password");
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(signupParam)
                .when().post("/members")
                .then()
                .statusCode(201);
    }

    @Test
    void 유저_정보_확인_테스트() {
        Map<String, String> signupParam = Map.of("name", "투다", "email", "token-login2@email.com", "password",
                "password");
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(signupParam)
                .when().post("/members")
                .then()
                .statusCode(201);

        Map<String, String> loginParam = Map.of("email", "token-login2@email.com", "password", "password");
        String header = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(loginParam)
                .when().post("/login")
                .then().log().all()
                .extract().header("Set-Cookie");

        String token = header.split("token=")[1];

        MemberResponseDto memberResponseDto = RestAssured.given()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .when().get("/login/check")
                .then().log().all()
                .extract().body().as(MemberResponseDto.class);

        assertThat(memberResponseDto.name()).isEqualTo("투다");
    }

    @Test
    void jwt_토큰_로그인_테스트() {
        Map<String, String> signupParam = Map.of("name", "투다", "email", "token-login3@email.com", "password",
                "password");
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(signupParam)
                .when().post("/members")
                .then()
                .statusCode(201);

        Map<String, String> loginParam = Map.of("email", "token-login3@email.com", "password", "password");
        String header = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(loginParam)
                .when().post("/login")
                .then().log().all()
                .extract().header("Set-Cookie");

        String token = header.split("token=")[1];

        Assertions.assertAll(
                () -> assertThat(header).isNotNull(),
                () -> assertThat(token).isNotEmpty()
        );
    }
}

