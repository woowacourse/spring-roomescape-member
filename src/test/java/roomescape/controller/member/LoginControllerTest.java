package roomescape.controller.member;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql(scripts = {"/schema.sql", "/initial_member_data.sql"})
class LoginControllerTest {

    @Test
    @DisplayName("")
    void login() {
        Map<String, String> reservationParams = new HashMap<>();
        reservationParams.put("password", "password");
        reservationParams.put("email", "admin@email.com");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationParams)
                .when().post("/login")
                .then().log().all()
                .statusCode(200)
                .cookie("token");
    }
}
