package roomescape.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MemberViewControllerTest {

    @BeforeEach
    void setUp() {
        Map<String, Object> normalParams = new HashMap<>();
        normalParams.put("name", "일반");
        normalParams.put("email", "user@gmail.com");
        normalParams.put("password", "1234");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(normalParams)
                .post("/signup");
    }

    @Test
    @DisplayName("/reservation GET 요청에 응답한다")
    void reservation_page() {
        String token = getToken();
        
        RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/reservation")
                .then().log().all()
                .statusCode(200);
    }

    private String getToken() {
        Map<String, Object> memberParams = new HashMap<>();
        memberParams.put("email", "user@gmail.com");
        memberParams.put("password", "1234");

        String accessToken = RestAssured
                .given().log().all()
                .body(memberParams)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login")
                .then().log().all().extract().cookie("token");
        return accessToken;
    }
}
