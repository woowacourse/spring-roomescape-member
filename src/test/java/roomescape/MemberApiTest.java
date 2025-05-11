package roomescape;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static org.hamcrest.Matchers.*;

@ActiveProfiles("test")
@Sql(scripts = {"/schema.sql", "/test.sql"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class MemberApiTest {

    public static final String TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6ImFkbWluIiwicm9sZSI6IkFETUlOIn0.cwnHsltFeEtOzMHs2Q5-ItawgvBZ140OyWecppNlLoI";

    @Test
    void 로그인_페이지로_접근할_수_있다() {
        RestAssured.given().log().all()
            .when().get("/login")
            .then().log().all()
            .statusCode(200);
    }

    @Test
    void 로그인_할_수_있다() {
        RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body("""
                {
                    "email": "admin@email.com",
                    "password": "password"
                }
                """)
            .when().post("/login")
            .then().statusCode(200).cookie("token", notNullValue());

    }

    @Test
    void 인증_정보를_조회_할_수_있다() {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6IuyWtOuTnOuvvCIsInJvbGUiOiJBRE1JTiJ9.vcK93ONRQYPFCxT5KleSM6b7cl1FE-neSLKaFyslsZM";

        RestAssured.given().log().all()
            .cookie("token", token)
            .when()
            .get("/login/check")
            .then().log().all()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("name", equalTo("어드민"));

    }

    @Test
    void 로그아웃_할_수_있다() {
        RestAssured.given().log().all()
            .cookie("token", TOKEN)
            .when().post("/logout")
            .then().log().all()
            .statusCode(200)
            .cookie("token", "");
    }
}
