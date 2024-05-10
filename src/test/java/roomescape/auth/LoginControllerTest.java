package roomescape.auth;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.auth.provider.JwtTokenProvider;
import roomescape.member.domain.Member;
import roomescape.member.dto.MemberRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class LoginControllerTest {
    private static final String NAME = "김철수";
    private static final String EMAIL = "chulsoo@example.com";
    private static final String PASSWORD = "123";
    private static final String ROLE = "USER";

    @LocalServerPort
    private int port;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private Member member;
    private String memberToken;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        member = new Member(1L, NAME, EMAIL, PASSWORD, ROLE);
        memberToken = jwtTokenProvider.createToken(member);
    }

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
        MemberRequest memberRequest = new MemberRequest(EMAIL, PASSWORD);

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

        String email = jwtTokenProvider.getEmail(token);
        assertThat(email).isEqualTo(EMAIL);
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
                .statusCode(HttpStatus.OK.value())
                .log().all()
                .body("id", equalTo(1),
                        "name", equalTo(NAME),
                        "email", equalTo(EMAIL),
                        "role", equalTo(ROLE));
    }
}
