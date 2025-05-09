package roomescape.controller;

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
import roomescape.dao.FakeMemberDaoImpl;
import roomescape.dao.TestDaoConfiguration;
import roomescape.domain.Member;
import roomescape.dto.LoginRequest;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(properties = "spring.sql.init.mode=never")
@Import(TestDaoConfiguration.class)
class AuthControllerTest {

    @Autowired
    private FakeMemberDaoImpl memberDao;

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


}
