package roomescape.member.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class MemberRestControllerTest {

    @Test
    void 회원_목록을_조회한다() {
        //given
        final String adminToken = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(Map.of("email", "east@email.com", "password", "1234"))
                .when().post("/login").getCookie("token");

        //wben & then
        RestAssured.given().log().all()
                .cookie("token", adminToken)
                .when().get("/admin/members")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(2));
    }
}
