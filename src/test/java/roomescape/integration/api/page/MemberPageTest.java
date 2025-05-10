package roomescape.integration.api.page;

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

class MemberPageTest extends RestAssuredTestBase {

    @Test
    void 유저_예약하기_페이지_조회() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("JSESSIONID", generateSessionId())
                .when().get("/reservation")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    void 기본페이지인_인기_테마_페이지_조회() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("JSESSIONID", generateSessionId())
                .when().get("/")
                .then().log().all()
                .statusCode(200);
    }

    private String generateSessionId() {
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
