package roomescape.ui;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.junit.jupiter.api.Test;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AdminControllerTest {

    @Autowired
    private AdminController adminController;

    @Test
    void 사용자는_예약_화면_요청에_실패한다() {

        RestAssured.given().log().all()
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(400);

        assertThat(adminController).isNotNull();
    }

    @Test
    void 사용자는_예약_시간_화면_요청에_실패한다() {
        RestAssured.given().log().all()
                .when().get("/admin/time")
                .then().log().all()
                .statusCode(400);
    }

}
