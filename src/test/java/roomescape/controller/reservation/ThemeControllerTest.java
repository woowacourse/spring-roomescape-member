package roomescape.controller.reservation;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import roomescape.controller.AuthenticationProvider;
import roomescape.infrastructure.TokenCookieProvider;

import java.util.Map;

import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/truncate-data.sql")
public class ThemeControllerTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("권한에 상관없이 테마를 조회할 수 있다.")
    void findTheme_Success() {
        RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }

    @Test
    @DisplayName("관리자는 테마 추가 및 삭제를 할 수 있다.")
    @Sql(scripts = {"/truncate-data.sql", "/member-data.sql"})
    void addDeleteThemeWithAdmin_Success() {
        String token = AuthenticationProvider.loginAdmin();
        String cookie = TokenCookieProvider.createCookie(token).toString();
        Map<String, String> params = Map.of("name", "레벨2 탈출",
                "description", "우테코 레벨2를 탈출하는 내용입니다.",
                "thumbnail", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
        );

        String location = RestAssured.given().log().all()
                .cookie(cookie)
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201)
                .extract().header("Location");

        String id = location.substring(location.lastIndexOf("/") + 1);

        RestAssured.given().log().all()
                .cookie(cookie)
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));

        RestAssured.given().log().all()
                .cookie(cookie)
                .when().delete("/themes/" + id)
                .then().log().all()
                .statusCode(204);

        RestAssured.given().log().all()
                .cookie(cookie)
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }

    @Test
    @DisplayName("일반 사용자는 테마 추가 및 삭제를 할 수 없다.")
    @Sql(scripts = {"/truncate-data.sql", "/member-data.sql"})
    void addDeleteThemeWithMember_Failure() {
        String token = AuthenticationProvider.loginMember();
        String cookie = TokenCookieProvider.createCookie(token).toString();
        Map<String, String> params = Map.of("name", "레벨2 탈출",
                "description", "우테코 레벨2를 탈출하는 내용입니다.",
                "thumbnail", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"
        );

        RestAssured.given().log().all()
                .cookie(cookie)
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/themes")
                .then().log().all()
                .statusCode(403);

        RestAssured.given().log().all()
                .cookie(cookie)
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));

        RestAssured.given().log().all()
                .cookie(cookie)
                .when().delete("/themes/1")
                .then().log().all()
                .statusCode(403);

        RestAssured.given().log().all()
                .cookie(cookie)
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }
}
