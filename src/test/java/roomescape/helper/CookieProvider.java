package roomescape.helper;

import io.restassured.RestAssured;
import io.restassured.http.Cookies;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import roomescape.service.dto.LoginRequest;

@Component
public class CookieProvider {
    private Cookies cookies;

    public void execute() {
        cookies = createCookies();
    }

    private Cookies createCookies() {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new LoginRequest("admin@email.com", "password"))
                .when().post("/login")
                .then().log().all()
                .extract().detailedCookies();
    }

    public Cookies getCookies() {
        return cookies;
    }
}
