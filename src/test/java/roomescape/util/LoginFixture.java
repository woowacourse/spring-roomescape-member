package roomescape.util;

import io.restassured.RestAssured;
import org.springframework.http.MediaType;
import roomescape.member.dto.LoginRequest;

public class LoginFixture {
    public static String takeAdminToken() {
        return RestAssured
                .given().log().all()
                .body(new LoginRequest("admin@email.com", "password"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login")
                .then().log().all().extract()
                .cookie("token");
    }
}
