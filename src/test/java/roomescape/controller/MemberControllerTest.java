package roomescape.controller;

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
import roomescape.dto.MemberRegisterRequest;

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
        MemberRegisterRequest request = new MemberRegisterRequest("testName", "testEmail", "1234");

        //when
        ExtractableResponse<Response> response = RestAssured
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

        String locationHeader = response.header("Location");

        //then
        assertThat(locationHeader).isNotNull();
        assertThat(locationHeader).isEqualTo("/members/1");
    }


}
