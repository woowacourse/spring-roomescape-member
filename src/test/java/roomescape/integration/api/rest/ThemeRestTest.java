package roomescape.integration.api.rest;

import static org.hamcrest.Matchers.*;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import roomescape.common.RestAssuredTestBase;
import roomescape.domain.member.MemberEmail;
import roomescape.domain.member.MemberEncodedPassword;
import roomescape.domain.member.MemberName;
import roomescape.domain.member.MemberRole;
import roomescape.repository.MemberRepository;

class ThemeRestTest extends RestAssuredTestBase {

    @Autowired
    private MemberRepository memberRepository;

    private Map<String, String> createThemeRequest = Map.of(
            "name", "공포방탈출",
            "description", "무서운 분위기 속에서 탈출",
            "thumbnail", "https://example.com/horror.jpg"
    );

    @Test
    void 테마를_생성한다() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("JSESSIONID", getSessionId())
                .body(createThemeRequest)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201)
                .body("id", is(1))
                .body("name", is("공포방탈출"))
                .body("description", is("무서운 분위기 속에서 탈출"))
                .body("thumbnail", is("https://example.com/horror.jpg"));
    }

    @Test
    void 테마_목록을_조회한다() {
        테마를_생성한다();
        RestAssured.given().log().all()
                .cookie("JSESSIONID", getSessionId())
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("[0].id", is(1))
                .body("[0].name", is("공포방탈출"))
                .body("[0].description", is("무서운 분위기 속에서 탈출"))
                .body("[0].thumbnail", is("https://example.com/horror.jpg"));
    }

    @Test
    void 테마를_삭제한다() {
        Integer id = RestAssured.given()
                .contentType(ContentType.JSON)
                .cookie("JSESSIONID", getSessionId())
                .body(createThemeRequest)
                .when().post("/themes")
                .then().statusCode(201)
                .extract().path("id");
        RestAssured.given().log().all()
                .cookie("JSESSIONID", getSessionId())
                .when().delete("/themes/{id}", id)
                .then().log().all()
                .statusCode(204);
    }

    @Test
    void 인기_테마를_조회한다() {
        테마를_생성한다();
        RestAssured.given().log().all()
                .cookie("JSESSIONID", getSessionId())
                .when().get("/themes/popular")
                .then().log().all()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(0));
    }

    private String getSessionId() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        memberRepository.save(
                new MemberEmail("leenyeonsu4888@gmail.com"),
                new MemberName("홍길동"),
                new MemberEncodedPassword(encoder.encode("gustn111!!")),
                MemberRole.MEMBER
        );

        Map<String, Object> request = Map.of(
                "password", "gustn111!!",
                "email", "leenyeonsu4888@gmail.com"
        );

        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/login")
                .then().log().all()
                .statusCode(200)
                .extract()
                .cookie("JSESSIONID");
    }
}
