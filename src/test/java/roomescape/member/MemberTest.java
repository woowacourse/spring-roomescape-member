package roomescape.member;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.member.dto.MemberInfo;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static roomescape.fixture.MemberFixture.memberFixture;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class MemberTest {

    @LocalServerPort
    int port;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeEach
    void saveMember() {
        RestAssured.port = port;
        jdbcTemplate.update("INSERT INTO member(name, email, password) VALUES (?, ?, ?)",
                memberFixture.getName(), memberFixture.getEmail(), memberFixture.getPassword());
    }

    @DisplayName("로그인 테스트")
    @Test
    void logIn() {
        Map<String, String> params = new HashMap<>();
        params.put("email", memberFixture.getEmail());
        params.put("password", memberFixture.getPassword());

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/login")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("로그인 정보 요청 테스트")
    @Test
    void LogInInfo() {
        Map<String, String> params = new HashMap<>();
        params.put("email", memberFixture.getEmail());
        params.put("password", memberFixture.getPassword());

        String token = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/login")
                .then().log().all().extract().header("Set-Cookie");
        token = token.substring("token=".length(), token.indexOf(';'));

        String name = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .when().get("/login/check")
                .then().log().all()
                .statusCode(200)
                .extract().as(MemberInfo.class).name();

        assertThat(name).isEqualTo(memberFixture.getName());
    }
}
