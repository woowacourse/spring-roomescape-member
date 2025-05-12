package roomescape.controller.auth;


import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.common.Role;
import roomescape.common.auth.JwtProvider;
import roomescape.model.Member;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AuthControllerTest {

    @Test
    @DisplayName("로그인 email 혹은 password가 일치하지 않는다면 404 에러를 반환한다.")
    void test1() {
        Map<String, String> loginRequest = new HashMap<>();
        loginRequest.put("email", "nonexistent@example.com");
        loginRequest.put("password", "wrongPassword");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(loginRequest)
                .when().post("/login")
                .then().log().all()
                .statusCode(401);
    }

    @DisplayName("user가 관리자 페이지 GET 요청 시 403을 반환한다")
    @Test
    void 관리자_페이지_접근_실패() {
        JwtProvider jwtProvider = new JwtProvider();
        String userToken = jwtProvider.createToken(
                new Member(6L, "포로", "asd", "1234", Role.USER)
        );
        RestAssured.given().log().all()
                .cookie("token", userToken)
                .when().get("/admin")
                .then().log().all()
                .statusCode(403);
    }

}