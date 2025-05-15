package roomescape.controller.auth;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import roomescape.common.auth.JwtTokenProvider;
import roomescape.dao.TestDaoConfiguration;
import roomescape.dao.member.FakeMemberDaoImpl;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberRole;
import roomescape.dto.auth.request.LoginRequest;
import roomescape.dto.auth.response.MemberNameResponse;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(properties = "spring.sql.init.mode=never")
@Import(TestDaoConfiguration.class)
class AuthControllerTest {

    @Autowired
    private FakeMemberDaoImpl memberDao;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @LocalServerPort
    int port;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
    }

    @DisplayName("로그인이 성공하면 발급된 토큰을 쿠키에 담아 응답한다.")
    @Test
    void createToken() {
        //given
        Member member = Member.fromWithoutId("testName", "testEmail", "1234");
        memberDao.save(member);

        LoginRequest request = new LoginRequest("testEmail", "1234");

        //when
        ExtractableResponse<Response> response = RestAssured
                .given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/login")
                .then()
                .log().all()
                .statusCode(200)
                .extract();

        String tokenCookie = response.cookie("token");

        //then
        assertThat(tokenCookie).isNotNull();
    }

    @DisplayName("사용자 인증 정보를 조회하면 사용자 이름을 응답한다.")
    @Test
    void checkLogin() {
        //given
        Member member = Member.fromWithoutId("testName", "testEmail", "1234");
        memberDao.save(member);

        String token = jwtTokenProvider.createToken(member);

        //when
        MemberNameResponse actual = RestAssured
                .given()
                .log().all()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .when()
                .get("/login/check")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .as(MemberNameResponse.class);

        //then
        assertThat(actual.name()).isEqualTo("testName");
    }

    @DisplayName("로그인 되어있는 사용자가 로그아웃을 하면 쿠키가 삭제된다.")
    @Test
    void logout() {
        //given
        Member member = Member.from(1L, "testName", "testEmail", "1234", MemberRole.USER);
        String token = jwtTokenProvider.createToken(member);

        //when
        ExtractableResponse<Response> response = RestAssured
                .given()
                .log().all()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .when()
                .post("/logout")
                .then()
                .log().all()
                .statusCode(200)
                .extract();

        String actual = response.cookie("token");

        //then
        assertThat(actual).isEmpty();
    }

}
