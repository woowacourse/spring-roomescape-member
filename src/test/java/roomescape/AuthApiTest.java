package roomescape;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import roomescape.auth.presentation.request.LoginRequest;

@ActiveProfiles("test")
@Sql(scripts = {"/schema.sql", "/test.sql"})
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class AuthApiTest {

    @Test
    @DisplayName("로그인에 성공하면 JWT accessToken, refreshToken 을 Response 받는다.")
    void getJwtAccessTokenWhenlogin() {
        // given
        String email = "test@test.com";
        String password = "1234";

        LoginRequest request = new LoginRequest(email, password);

        // when
        Map<String, String> cookies = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/login")
                .then().log().all().extract().cookies();

        // then
        Assertions.assertThat(cookies.get("accessToken")).isNotNull();
    }
}
