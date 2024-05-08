package roomescape.controller;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.dto.MemberRequest;
import roomescape.dto.MemberResponse;

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
    void login() {
        jdbcTemplate.update("INSERT INTO member (name, email, password) values (?,?,?)", "어드민", "admin@email.com",
                "password");

        String cookie = RestAssured
                .given().log().all()
                .body(new MemberRequest("password", "admin@email.com"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login")
                .then().log().all().extract().header("Set-Cookie").split(";")[0];

//        assertThat(cookie).isEqualTo(
//                "token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6ImFkbWluIiwicm9sZSI6IkFETUlOIn0.cwnHsltFeEtOzMHs2Q5-ItawgvBZ140OyWecppNlLoI");
    }

    @Test
    void loginCheck() {
        jdbcTemplate.update("INSERT INTO member (name, email, password) values (?,?,?)", "어드민", "admin@email.com", "password");

        MemberResponse member = RestAssured
                .given().log().all()
                .header("cookie",
                        "_ga=GA1.1.48222725.1666268105; _ga_QD3BVX7MKT=GS1.1.1687746261.15.1.1687747186.0.0.0; Idea-25a74f9c=3cbc3411-daca-48c1-8201-51bdcdd93164; token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwibmFtZSI6IuyWtOuTnOuvvCIsInJvbGUiOiJBRE1JTiJ9.vcK93ONRQYPFCxT5KleSM6b7cl1FE-neSLKaFyslsZM")
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/login/check")
                .then().log().all()
                .statusCode(HttpStatus.OK.value()).extract().as(MemberResponse.class);

        assertThat(member.name()).isEqualTo("어드민");
    }
}
