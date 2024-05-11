package roomescape.helper;

import io.restassured.RestAssured;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import roomescape.service.dto.LoginRequest;

@Component
public class CookieProvider {
    private String cookie;

    public void execute() {
        cookie = createCookie();
    }

    private String createCookie() {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new LoginRequest("admin@email.com", "password"))
                .when().post("/login")
                .then().log().all()
                .extract().header("Set-Cookie").split(";")[0];
    }

    public String getCookie() {
        return cookie;
    }
}
