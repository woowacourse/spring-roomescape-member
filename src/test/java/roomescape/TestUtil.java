package roomescape;

import io.restassured.RestAssured;
import org.springframework.test.context.jdbc.Sql;
import roomescape.dto.auth.LoginRequest;

@Sql(scripts = {"/test_schema.sql", "/test_admin_member.sql"})
public class TestUtil {

    public static String getAdminUserToken() {
        return RestAssured.given().log().all()
                .contentType("application/json")
                .body(new LoginRequest("admin@email.com", "admin_password"))
                .when().post("/login")
                .then().log().all()
                .extract().cookie("token");
    }
}
