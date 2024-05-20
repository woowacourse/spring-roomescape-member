package roomescape.fixture;

import io.restassured.RestAssured;
import org.springframework.http.MediaType;
import roomescape.controller.api.dto.request.MemberLoginRequest;

public class TokenFixture {

    public static String getToken() {
        return RestAssured
                .given()
                .body(new MemberLoginRequest("jerry@email.com", "1234"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login")
                .getCookie("accessToken");
    }
}
