package roomescape.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserPageControllerTest {

    @LocalServerPort
    int serverPort;

    @BeforeEach
    public void beforeEach() {
        RestAssured.port = serverPort;
    }

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private String accessToken;

    @TestFactory
    @DisplayName("로그인 동작 기능을 확인한다")
    Stream<DynamicTest> checkLogin() {
        Map<String, String> login = Map.of(
                "email", "admin1@email.com",
                "password", "password"
        );

        return Stream.of(
                dynamicTest("로그인", () -> {
                    RestAssured.given().log().all()
                            .contentType(ContentType.JSON)
                            .body(login)
                            .when().post("/login")
                            .then().log().all()
                            .statusCode(200);
                })
        );
    }

    @TestFactory
    @DisplayName("인증 정보 조회 기능을 확인한다")
    Stream<DynamicTest> checkLoginAuthenticationInformation() {
        Map<String, String> login = Map.of(
                "email", "admin1@email.com",
                "password", "password"
        );

        return Stream.of(
                dynamicTest("로그인", () -> {
                    accessToken = RestAssured.given().log().all()
                            .contentType(ContentType.JSON)
                            .body(login)
                            .when().post("/login")
                            .then().log().all()
                            .extract().cookie("token");
                }),
                dynamicTest("로그인 정보 조회", () -> {
                    RestAssured.given().log().all()
                            .cookie("token", accessToken)
                            .when().get("/login/check")
                            .then().log().all()
                            .statusCode(200);
                })
        );
    }
}
