package roomescape.endpoint;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Cookies;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import roomescape.dto.LoginRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(properties = {"spring.config.location = classpath:application-test.yml"})
class LanderPageTest {

    private Cookies cookies;

    @BeforeEach
    void setAdminToken() {
        cookies = RestAssured.given()
                .contentType(ContentType.JSON)
                .when()
                .body(new LoginRequest("패스워드2", "이메일2"))
                .post("/login")
                .then()
                .statusCode(200)
                .extract()
                .response()
                .getDetailedCookies();
    }

    @ParameterizedTest(name = "{0} ,{1}")
    @CsvSource(value = {
            "/admin:관리자 메인 페이지",
            "/admin/reservation:예약 관리 페이지",
            "/admin/time:예약 시간 관리 페이지",
            "/admin/theme:테마 관리 페이지"
    }, delimiter = ':')
    void loadAdminPage(String path, String description) {
        RestAssured.given().log().all()
                .cookies(cookies)
                .when().get(path)
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @ParameterizedTest(name = "{0} ,{1}")
    @CsvSource(value = {
            "/reservation:예약 페이지",
            "/:사용자 메인 페이지",
            "/login:로그인 페이지",
            "/signup:회원가입 페이지"
    }, delimiter = ':')
    void loadPage(String path, String description) {
        RestAssured.given().log().all()
                .when().get(path)
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }
}
