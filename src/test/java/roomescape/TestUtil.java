package roomescape;

import io.restassured.RestAssured;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import roomescape.dto.auth.LoginRequest;

@Sql(scripts = {"/test_schema.sql", "/test_member.sql"})
public class TestUtil {

    private static final LoginRequest admin = new LoginRequest("admin@email.com", "admin_password");
    private static final LoginRequest member = new LoginRequest("user@email.com", "user_password");

    public static String getAdminUserToken() {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(admin)
                .when().post("/login")
                .then().log().all()
                .extract().cookie("token");
    }

    public static String getMemberToken() {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(member)
                .when().post("/login")
                .then().log().all()
                .extract().cookie("token");
    }
}
