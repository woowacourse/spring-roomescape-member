package roomescape.controller.member;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import roomescape.dao.TestDaoConfiguration;
import roomescape.dto.member.request.MemberRegisterRequest;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(properties = "spring.sql.init.mode=never")
@Import(TestDaoConfiguration.class)
class MemberControllerTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
    }

    @DisplayName("회원가입에 성공하면 경로 정보를 가지는 응답이 반환된다.")
    @Test
    void register() {
        //given
        final MemberRegisterRequest request = new MemberRegisterRequest("testName", "testEmail@example.com", "1234");

        //when
        final ExtractableResponse<Response> response = RestAssured
                .given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/members")
                .then()
                .log().all()
                .statusCode(201)
                .extract();

        final String locationHeader = response.header("Location");

        //then
        assertThat(locationHeader).isNotNull();
        assertThat(locationHeader).isEqualTo("/members/1");
    }

    @DisplayName("요청에서 잘못된 이메일 형식이라면 예외 메세지와 상태코드 400 bad_request 를 담은 응답이 반환된다.")
    @Test
    void invalidEmailPattern() {
        //given
        final MemberRegisterRequest request = new MemberRegisterRequest("testName", "testEmail", "1234");

        //when
        final ExtractableResponse<Response> response = RestAssured
                .given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/members")
                .then()
                .log().all()
                .statusCode(400)
                .extract();

        final String actual = response.asString();

        //then
        assertThat(actual).contains("유효한 이메일 형식이 아닙니다.");
    }

}
