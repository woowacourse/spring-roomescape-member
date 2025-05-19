package roomescape.presentation.controller.admin.page;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class AdminPageControllerTest {

    private String adminToken;
    private String userToken;

    @BeforeEach
    void createToken() {
        Map<String, String> adminParameters = Map.of(
                "email", "sooyang@woowa.com",
                "password", "1234"
        );
        Map<String, String> userParameters = Map.of(
                "email", "user@woowa.com",
                "password", "user1234"
        );
        adminToken = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(adminParameters)
                .when().post("/login")
                .then().log().all()
                .extract().response().getCookie("token");
        userToken = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(userParameters)
                .when().post("/login")
                .then().log().all()
                .extract().response().getCookie("token");
    }

    private static Stream<Arguments> adminPages() {
        return Stream.of(
                Arguments.of("/admin"),
                Arguments.of("/admin/reservation"),
                Arguments.of("/admin/time"),
                Arguments.of("/admin/theme")
        );
    }

    @ParameterizedTest
    @MethodSource("adminPages")
    @DisplayName("관리자는 관리자 페이지에 접근할 수 있다.")
    void adminAccessAdminPage(String path) {
        RestAssured.given().log().all()
                .cookie("token", adminToken)
                .when().get(path)
                .then().log().all()
                .statusCode(200);
    }

    @ParameterizedTest
    @MethodSource("adminPages")
    @DisplayName("사용자는 관리자 페이지에 접근할 수 없다.")
    void userAccessAdminPage(String path) {
        RestAssured.given().log().all()
                .cookie("token", userToken)
                .when().get(path)
                .then().log().all()
                .statusCode(401);
    }
}