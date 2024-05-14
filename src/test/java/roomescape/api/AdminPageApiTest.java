package roomescape.api;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static roomescape.LoginTestSetting.getCookieByLogin;

import io.restassured.http.Cookie;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;

@Sql("/page-test-data.sql")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class AdminPageApiTest {

    private static final String ADMIN_EMAIL = "admin@email.com";
    private static final String ADMIN_PASSWORD = "123456";

    private static final String USER_EMAIL = "test@email.com";
    private static final String USER_PASSWORD = "123456";

    @LocalServerPort
    int port;

    static List<String> provideUrls() {
        return List.of("/admin", "/admin/reservation", "/admin/time", "admin/theme");
    }

    @ParameterizedTest
    @MethodSource("provideUrls")
    void 로그인이_되어있지_않은_경우_어드민_페이지_진입시_로그인_페이지로_이동(String url) {
        given().redirects().follow(false).log().all()
                .port(port)
                .when().get(url)
                .then().log().all()
                .statusCode(302)
                .header("Location", containsString("/login"));
    }

    @ParameterizedTest
    @MethodSource("provideUrls")
    void 로그인_계정의_권한이_관리자가_아닐때_어드민_페이지_진입시_권한없음_응답(String url) {
        Cookie cookieByLogin = getCookieByLogin(port, USER_EMAIL, USER_PASSWORD);

        given().log().all()
                .port(port)
                .cookie(cookieByLogin)
                .when().get(url)
                .then().log().all()
                .statusCode(401);
    }

    @ParameterizedTest
    @MethodSource("provideUrls")
    void 로그인_계정의_권한이_관리자이면_어드민_페이지_정상_진입(String url) {
        Cookie cookieByLogin = getCookieByLogin(port, ADMIN_EMAIL, ADMIN_PASSWORD);

        given().log().all()
                .port(port)
                .cookie(cookieByLogin)
                .when().get(url)
                .then().log().all()
                .statusCode(200);
    }
}
