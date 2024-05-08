package roomescape.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.controller.api.dto.request.MemberLoginRequest;
import roomescape.service.MemberService;
import roomescape.service.dto.input.MemberCreateInput;

import static org.hamcrest.CoreMatchers.notNullValue;

//@formatter:off
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LoginApiControllerTest {
    @Autowired
    MemberService memberService;
    @LocalServerPort
    int port;
    @Autowired
    JdbcTemplate jdbcTemplate;
    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        jdbcTemplate.update("TRUNCATE TABLE member");
    }

    @Test
    @DisplayName("/login POST 요청시 쿠키를 반환한다.")
    void response_user_reservation_page() {
        memberService.createMember(new MemberCreateInput("어드민", "admin@email.com", "password"));

        RestAssured.given().contentType(ContentType.JSON).body(new MemberLoginRequest("admin@email.com","password")).log().all()
                .when().post("/login")
                .then().log().all().cookie("token",notNullValue()).statusCode(200);
    }
}
