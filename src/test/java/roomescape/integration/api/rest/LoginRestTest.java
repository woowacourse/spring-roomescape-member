package roomescape.integration.api.rest;

import static org.hamcrest.Matchers.is;

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

class LoginRestTest extends RestAssuredTestBase {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void 로그인을_한다() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        memberRepository.save(
                new MemberEmail("leehyeonsu4888@gmail.com"),
                new MemberName("한스"),
                new MemberEncodedPassword(encoder.encode("gustn111!!")),
                MemberRole.MEMBER
        );

        Map<String, Object> request = Map.of(
                "password", "gustn111!!",
                "email", "leehyeonsu4888@gmail.com"
        );
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/login")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    void 현재_로그인된_멤버가_누구인지_조회한다() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        memberRepository.save(
                new MemberEmail("leenyeonsu4888@gmail.com"),
                new MemberName("한스"),
                new MemberEncodedPassword(encoder.encode("gustn111!!")),
                MemberRole.MEMBER
        );

        Map<String, Object> request = Map.of(
                "password", "gustn111!!",
                "email", "leenyeonsu4888@gmail.com"
        );

        String sessionId = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/login")
                .then().log().all()
                .statusCode(200)
                .extract()
                .cookie("JSESSIONID");

        RestAssured.given().log().all()
                .cookie("JSESSIONID", sessionId)
                .contentType(ContentType.JSON)
                .when().get("/login/check")
                .then().log().all()
                .statusCode(200)
                .body("name", is("한스"));
    }
}
