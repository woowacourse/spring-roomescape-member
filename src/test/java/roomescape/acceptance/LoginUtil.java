package roomescape.acceptance;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.Map;

public class LoginUtil {
    public static String login(String email, String password) {
        Map<?, ?> requestBody = Map.of("email", email, "password", password);

        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when().post("/login")
                .then().log().cookies().extract().cookie("token");
    }
}
