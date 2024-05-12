package roomescape.auth.controller;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.base.BaseTest;
import roomescape.member.dto.MemberRequest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class LoginControllerTest extends BaseTest {

    @Test
    void login() {
        RestAssured.given()
                .cookie("token", memberToken)
                .log()
                .all()
                .when()
                .get("/login")
                .then()
                .log()
                .all()
                .statusCode(200);
    }

    @Test
    @DisplayName("{email, password}로 토큰을 생성할 수 있다.")
    void tokenLoginTest() {
        MemberRequest memberRequest = new MemberRequest(member.getEmail(), member.getPassword());

        Response response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberRequest)
                .when()
                .post("/login")
                .thenReturn();

        response.then()
                .statusCode(HttpStatus.OK.value())
                .log().all()
                .cookie("token");

        String token = response.getCookie("token");
        assertThat(token).isNotNull();

        long id = jwtTokenProvider.getId(token);
        assertThat(id).isEqualTo(member.getId());
    }

    @Test
    @DisplayName("토큰을 사용해서 로그인을 할 수 있다.")
    void checkLoginTest() {
        RestAssured.given()
                .cookie("token", memberToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(member)
                .when()
                .get("/login/check")
                .then()
                .statusCode(HttpStatus.OK.value());
    }
}
