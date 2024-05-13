package roomescape;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import roomescape.domain.Member;

import java.util.HashMap;
import java.util.Map;

import static roomescape.service.CookieService.TOKEN;

public class LoginUtils {

    public static String loginAndGetToken(Member member) {

        Map<String, String> params = new HashMap<>();
        params.put("email", member.getEmail().getEmail());
        params.put("password", member.getPassword());

        Response response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/login")
                .then().log().all()
                .extract().response();

        return response.cookie(TOKEN);
    }

    public static void logout(Member member) {
        Map<String, String> params = new HashMap<>();
        params.put("email", member.getEmail().getEmail());
        params.put("password", member.getPassword());

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/logout")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().response();
    }
}
