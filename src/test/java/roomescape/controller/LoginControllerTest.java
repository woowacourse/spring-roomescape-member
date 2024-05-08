package roomescape.controller;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.dto.MemberRequest;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class LoginControllerTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void getAll() {
        jdbcTemplate.update("INSERT INTO member (name, email, password) values (?,?,?)", "a", "admin@email.com", "password");

        String cookie = RestAssured
                .given().log().all()
                .body(new MemberRequest("password", "admin@email.com"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login")
                .then().log().all().extract().header("Set-Cookie").split(";")[0];

//        MemberResponse member = RestAssured
//                .given().log().all()
//                .auth().oauth2(accessToken)
//                .accept(MediaType.APPLICATION_JSON_VALUE)
//                .when().get("/members/me/token")
//                .then().log().all()
//                .statusCode(HttpStatus.OK.value()).extract().as(MemberResponse.class);

        assertThat(cookie).isEqualTo("token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6ImEiLCJyb2xlIjoibWVtYmVyIn0.A5wRysc1zXcxTtUx74qOccFMxG55t-BjcPmDZH9JYw0");
    }
}
