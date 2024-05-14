package roomescape.acceptance;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;

class PageLanderTest extends BaseAcceptanceTest {

    @DisplayName("관리자만 접근 가능한 페이지가 있다.")
    @Nested
    class pagesOnlyForAdmin extends NestedAcceptanceTest {

        @DisplayName("관리자는 접근 가능하다.")
        @ParameterizedTest
        @ValueSource(strings = {
                "/admin",
                "/admin/reservation",
                "/admin/time",
                "/admin/theme"
        })
        void adminPageAccess_success(String path) {

            RestAssured.given().log().all()
                    .cookie("token", tokenFixture.adminToken)
                    .when().get(path)
                    .then().log().all()
                    .statusCode(HttpStatus.OK.value());
        }

        @DisplayName("관리자가 아닌 사람은 접근 할 수 없다.")
        @ParameterizedTest
        @ValueSource(strings = {
                "/admin",
                "/admin/reservation",
                "/admin/time",
                "/admin/theme"
        })
        void adminPageAccess_fail(String path) {

            RestAssured.given().log().all()
                    .cookie("token", tokenFixture.customerToken)
                    .when().get(path)
                    .then().log().all()
                    .statusCode(HttpStatus.FORBIDDEN.value());
        }
    }
}
