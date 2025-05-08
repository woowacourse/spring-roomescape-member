package roomescape.auth.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.global.config.TestConfig;

@Import(TestConfig.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class LoginIntegrationTest {

    @DisplayName("로그인 후 응답 헤더에 토큰을 반환한다")
    @Test
    void login_test() {
        Map<String, String> params = new HashMap<>();
        params.put("email", "rookie@woowa.com");
        params.put("password", "rookie123");

        // when
        String header = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/login")
                .then().log().all()
                .statusCode(200)
                .extract()
                .header("Set-Cookie");

        // then
        assertAll(
                () -> assertThat(header).isNotNull(),
                () -> assertThat(header).contains("token="),
                () -> assertThat(header).contains("Path=/"),
                () -> assertThat(header).contains("HttpOnly")
        );
    }

}
