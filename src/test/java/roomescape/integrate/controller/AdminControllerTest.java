package roomescape.integrate.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class AdminControllerTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @AfterEach
    void cleanup() {
        jdbcTemplate.execute("drop all objects");
    }

    @Test
    void 어드민_페이지에_접근시_권한이_부족하면_403이_발생한다() {
        Map<String, String> signupParam = Map.of("name", "투다", "email", "token-login2@email.com", "password",
                "password");
//        jdbcTemplate.query("update member set ADMIN where name = '투다'");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(signupParam)
                .when().post("/auth/signup")
                .then()
                .statusCode(201);

        Map<String, String> loginParam = Map.of("email", "token-login2@email.com", "password", "password");
        String header = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(loginParam)
                .when().post("/auth/login")
                .then().log().all()
                .extract().header("Set-Cookie");

        String token = header.split("token=")[1];

        RestAssured.given()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .when().get("/admin")
                .then()
                .statusCode(403);
    }

    @Test
    void 어드민_권한을_가지면_어드민_페이지에_접근할_수_있다() {
        Map<String, String> signupParam = Map.of("name", "투다", "email", "token-login2@email.com", "password",
                "password");

        String sql = "UPDATE member SET role = ? WHERE name = ?";
        jdbcTemplate.update(sql, "ADMIN", "투다");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(signupParam)
                .when().post("/auth/signup")
                .then()
                .statusCode(201);

        Map<String, String> loginParam = Map.of("email", "token-login2@email.com", "password", "password");
        String header = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(loginParam)
                .when().post("/auth/login")
                .then().log().all()
                .extract().header("Set-Cookie");

        String token = header.split("token=")[1];

        RestAssured.given()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .when().get("/admin")
                .then()
                .statusCode(403);
    }
}

