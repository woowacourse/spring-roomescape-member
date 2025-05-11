package roomescape.integration;

import static org.hamcrest.CoreMatchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.entity.AccessToken;
import roomescape.entity.Member;
import roomescape.entity.MemberRole;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AuthApiIntegrationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private AccessToken userToken;
    private String userName;
    private String userEmail;
    private String userPassword;

    @BeforeEach
    void setUpData() {
        userName = "moda";
        userEmail = "moda_email";
        userPassword = "moda_password";

        String memberSetUp = "insert into member (name, email, password, role) values (?, ?, ?, 'USER')";
        jdbcTemplate.update(memberSetUp, userName, userEmail, userPassword);

        Member user = new Member(1L, userName, userEmail, userPassword, MemberRole.USER);
        userToken = new AccessToken(user);
    }

    @Test
    @DisplayName("로그인한다.")
    void login() {
        Map<String, String> params = new HashMap<>();
        params.put("name", userName);
        params.put("email", userEmail);
        params.put("password", userPassword);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/auth/login")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("인증 후 회원 이름을 반환한다.")
    void authorize() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", userToken.getValue())
                .when().get("/auth/login/check")
                .then().log().all()
                .statusCode(200)
                .body("name", is("moda"));
    }

    @Test
    @DisplayName("로그아웃한다.")
    void logout() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", userToken.getValue())
                .when().post("/auth/logout")
                .then().log().all()
                .statusCode(200)
                .cookie("token", "");
    }
}
