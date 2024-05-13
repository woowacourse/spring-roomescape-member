package roomescape.theme.integration;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import roomescape.member.domain.Member;
import roomescape.member.security.crypto.JwtTokenProvider;
import roomescape.theme.dto.ThemeRequest;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/data-test.sql", executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class ThemeIntegrationTest {

    @LocalServerPort
    private int port;

    private String token;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        Member member = new Member("valid", "e@m.com", "pass");
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider("secret-key", 99999999999L);
        token = jwtTokenProvider.createToken(member, new Date());
    }

    @Test
    @DisplayName("theme 추가 요청을 처리하고 성공 시 201 상태 코드를 응답한다.")
    void createReservationTheme_ShouldReturnCREATED_WhenCreateSuccessfully() {
        ThemeRequest themeRequest = new ThemeRequest("포레스트", "공포 테마", "thumbnail");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .body(themeRequest)
                .when()
                .post("/themes")
                .then()
                .statusCode(201);

        RestAssured.given()
                .cookie("token", token)
                .when()
                .get("/themes")
                .then()
                .statusCode(200)
                .body("size()", is(4));
    }

    @Test
    @DisplayName("theme 삭제 요청을 처리하고 성공 시 204 상태 코드를 응답한다.")
    void deleteReservationTheme_ShouldReturnNOCONTENT_WhenDeleteSuccessfully() {

        RestAssured.given()
                .cookie("token", token)
                .when()
                .delete("/themes/2")
                .then()
                .statusCode(204);

        RestAssured.given()
                .cookie("token", token)
                .when()
                .get("/themes")
                .then()
                .statusCode(200)
                .body("size()", is(2));
    }

}
