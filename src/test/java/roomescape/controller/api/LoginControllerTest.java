package roomescape.controller.api;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class LoginControllerTest {

    @Test
    @DisplayName("이메일과 비밀번호 검증 후 토큰 생성")
    void createReservation() {
        String cookieHeader = RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(getTestParamsWithLogin())
            .when().post("/login")
            .then().log().all()
            .statusCode(200)
            .extract()
            .header("Set-Cookie");

        assertThat(cookieHeader).isNotNull();
        assertThat(cookieHeader).contains("token=");
        assertThat(cookieHeader).contains("HttpOnly");
    }

    private Map<String, String> getTestParamsWithLogin() {
        return Map.of(
            "email", "sa123",
            "password", "na123"
        );
    }
}
