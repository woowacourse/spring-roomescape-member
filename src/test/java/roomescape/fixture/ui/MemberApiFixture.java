package roomescape.fixture.ui;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;

public class MemberApiFixture {

    public static Map<String, String> signUpParams1() {
        final Map<String, String> params = new HashMap<>();
        params.put("email", "test@one.com");
        params.put("password", "one");
        params.put("name", "테스트 이름1");

        return params;
    }

    public static Map<String, String> signUpParams2() {
        final Map<String, String> params = new HashMap<>();
        params.put("email", "test2@two.com");
        params.put("password", "two");
        params.put("name", "테스트 이름2");

        return params;
    }

    public static void signUp(final Map<String, String> signUpParams) {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(signUpParams)
                .when().post("/members")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }
}
