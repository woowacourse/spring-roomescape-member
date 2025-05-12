package roomescape.presentation.controller.user.page;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
class UserPageControllerTest {

    @Test
    @DisplayName("인기 테마 페이지 요청을 응답한다.")
    void test1() {
        RestAssured.given().log().all()
                .when().get("/")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("로그인 페이지 요청을 응답한다.")
    void test2() {
        RestAssured.given().log().all()
                .when().get("/login")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("회원가입 페이지 요청을 응답한다.")
    void test3() {
        RestAssured.given().log().all()
                .when().get("/signup")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("/reservation 페이지 요청을 응답한다.")
    void test4() {
        RestAssured.given().log().all()
                .when().get("/reservation")
                .then().log().all()
                .statusCode(200);
    }
}