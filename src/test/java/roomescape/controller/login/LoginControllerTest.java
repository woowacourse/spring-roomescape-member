package roomescape.controller.login;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import io.restassured.RestAssured;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import roomescape.IntegrationTestSupport;
import roomescape.service.dto.MemberResponse;
import roomescape.controller.dto.TokenRequest;

class LoginControllerTest extends IntegrationTestSupport {

    String accessToken;

    @LocalServerPort
    int port;

    @DisplayName("토큰으로 로그인 인증한다.")
    @TestFactory
    Stream<DynamicTest> dynamicTestsFromCollection() {
        RestAssured.port = port;

        return Stream.of(
                dynamicTest("이메일, 패스워드로 로그인한다.", () -> {
                    accessToken = RestAssured
                            .given().log().all()
                            .body(new TokenRequest(ADMIN_EMAIL, ADMIN_PASSWORD))
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .when().post("/login")
                            .then().log().all().extract().header("Set-Cookie").split("=")[1];
                }),
                dynamicTest("토큰으로 로그인 여부를 확인하여 이름을 받는다.", () -> {
                    MemberResponse member = RestAssured
                            .given().log().all()
                            .cookie("token", accessToken)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .when().get("/login/check")
                            .then().log().all()
                            .statusCode(HttpStatus.OK.value()).extract().as(MemberResponse.class);

                    assertThat(member.name()).isEqualTo(ADMIN_NAME);
                }),
                dynamicTest("로그아웃하면 토큰이 비어있다.", () -> {
                    String cookie = RestAssured
                            .given().log().all()
                            .cookie("token", accessToken)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .when().post("/logout")
                            .then().log().all()
                            .statusCode(HttpStatus.OK.value()).extract().cookie("token");

                    assertThat(cookie).isEmpty();
                })
        );
    }
}
