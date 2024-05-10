package roomescape.integration;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class AuthIntegrationTest extends IntegrationTest {
    @Nested
    @DisplayName("로그인 API")
    class Login {
        @Test
        void 이메일과_비밀번호로_로그인할_수_있다() {
            String cookie = RestAssured.given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(new LoginRequest("admin@email.com", "password"))
                    .when().post("/login")
                    .then().log().all().extract().header("Set-Cookie").split(";")[0];

            MemberResponse member = RestAssured.given().log().all()
                    .header("Cookie", cookie)
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .when().get("/login/check")
                    .then().log().all()
                    .statusCode(HttpStatus.OK.value()).extract().as(MemberResponse.class);

            assertThat(member.getEmail()).isEqualTo("admin@email.com");
        }

        @Test
        void 이메일이나_비밀번호가_틀리면_로그인할_수_없다() {
            RestAssured.given().log().all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(new LoginRequest("admin@email.com", "wrongpassword"))
                    .when().post("/login")
                    .then().log().all()
                    .statusCode(401);
        }
    }
}
