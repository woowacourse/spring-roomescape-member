package roomescape.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class LoginControllerTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void 사용자_로그인_성공() {
        Map<String, String> params = new HashMap<>();
        params.put("password", "asd123");
        params.put("email", "asd@asd.com");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/login")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    void 사용자_로그인_실패_잘못된_비밀번호() {
        Map<String, String> params = new HashMap<>();
        params.put("password", "wrong");
        params.put("email", "asd@asd.com");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/login")
                .then().log().all()
                .statusCode(400);
    }

}
