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

class AdminPageTest extends RestAssuredTestBase {

    @Test
    void 어드민_예약_추가_페이지_조회() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("JSESSIONID", generateLoginAdmin().sessionId())
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    void 어드민_예약_시간_관리_페이지_조회() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("JSESSIONID", generateLoginAdmin().sessionId())
                .when().get("/admin/time")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    void 어드민_테마_관리_페이지_조회() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("JSESSIONID", generateLoginAdmin().sessionId())
                .when().get("/admin/theme")
                .then().log().all()
                .statusCode(200);
    }
}
