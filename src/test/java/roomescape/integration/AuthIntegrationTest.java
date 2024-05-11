package roomescape.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import io.restassured.RestAssured;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import roomescape.dto.member.LoginMemberResponse;
import roomescape.dto.member.MemberLoginRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(value = "classpath:clean_data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class AuthIntegrationTest {

    @LocalServerPort
    int port;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private String accessToken = null;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @TestFactory
    @DisplayName("로그인, 토큰 확인, 로그아웃")
    Collection<DynamicTest> loginToLogout() {
        jdbcTemplate.update(
                "INSERT INTO member (name, email, password, role) VALUES ('사용자1', 'user1@wooteco.com', 'user1', 'USER')");

        return List.of(
                dynamicTest("로그인 시 토큰을 발행한다.", () -> {
                    accessToken = RestAssured.given()
                            .body(MemberLoginRequest.of("user1", "user1@wooteco.com"))
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .when().post("/login")
                            .getHeader("Set-Cookie");

                    assertThat(accessToken).isNotBlank();
                }),
                dynamicTest("발행된 토큰으로 회원임을 확인한다.", () -> {
                    LoginMemberResponse member = RestAssured
                            .given().log().all()
                            .header("Cookie", accessToken)
                            .accept(MediaType.APPLICATION_JSON_VALUE)
                            .when().get("/login/check")
                            .then().log().all()
                            .statusCode(HttpStatus.OK.value()).extract().as(LoginMemberResponse.class);

                    assertThat(member.name()).isEqualTo("사용자1");
                }),
                dynamicTest("로그아웃 시 토큰을 지운다.", () -> {
                    String logoutToken = RestAssured.given().log().all()
                            .header("Cookie", accessToken)
                            .when().post("/logout")
                            .getHeader("Set-Cookie")
                            .split(";")[0]
                            .substring("token=".length());

                    assertThat(logoutToken).isBlank();
                })
        );
    }
}
