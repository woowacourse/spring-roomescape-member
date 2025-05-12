package roomescape.ui;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.junit.jupiter.api.Test;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class UserControllerTest {

    @Autowired
    private UserController userController;

    @Test
    void 사용자는_예약_화면_요청에_실패한다() {

        RestAssured.given().log().all()
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(400);

        assertThat(userController).isNotNull();
    }

    @Test
    void 사용자는_예약_시간_화면_요청에_실패한다() {
        RestAssured.given().log().all()
                .when().get("/admin/time")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    void 사용자_예약_화면_요청을_성공한다() {
        RestAssured.given().log().all()
                .when().get("/reservation")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    void 인기테마_화면_요청을_성공한다() {
        RestAssured.given().log().all()
                .when().get("/")
                .then().log().all()
                .statusCode(200);
    }
}
