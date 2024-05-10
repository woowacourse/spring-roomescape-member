package roomescape.acceptance;

import io.restassured.RestAssured;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.http.HttpStatus;

class PageLanderTest extends AcceptanceTest {

    @ParameterizedTest(name = "{0} ,{1}")
    @CsvSource(value = {
            "/admin:관리자 메인 페이지",
            "/admin/reservation:예약 관리 페이지",
            "/admin/time:예약 시간 관리 페이지",
            "/admin/theme:테마 관리 페이지",
            "/reservation:예약 페이지",
            "/:사용자 메인 페이지"
    }, delimiter = ':')
    void loadPage(String path, String description) {
        RestAssured.given().log().all()
                .when().get(path)
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }
}
