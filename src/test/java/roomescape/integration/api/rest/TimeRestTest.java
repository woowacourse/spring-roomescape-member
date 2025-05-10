package roomescape.integration.api.rest;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static roomescape.common.Constant.FIXED_CLOCK;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import roomescape.common.RestAssuredTestBase;
import roomescape.domain.member.MemberEmail;
import roomescape.domain.member.MemberEncodedPassword;
import roomescape.domain.member.MemberName;
import roomescape.domain.member.MemberRole;

class TimeRestTest extends RestAssuredTestBase {

    private Map<String, String> reservationTime = Map.of("startAt", "10:00");

    @Test
    void 예약_시간을_생성한다() {
        RestAssured.given().log().all()
                .cookie("JSESSIONID", getSessionId())
                .contentType(ContentType.JSON)
                .body(reservationTime)
                .when().post("/times")
                .then().log().all()
                .statusCode(201)
                .body("id", is(1))
                .body("startAt", is("10:00"));
    }

    @Test
    void 예약_시간을_조회한다() {
        예약_시간을_생성한다();
        RestAssured.given().log().all()
                .cookie("JSESSIONID", getSessionId())
                .contentType(ContentType.JSON)
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .body("[0].id", is(1))
                .body("[0].startAt", is("10:00"));
    }

    @Test
    void 예약_시간을_삭제한다() {
        예약_시간을_생성한다();
        RestAssured.given().log().all()
                .cookie("JSESSIONID", getSessionId())
                .contentType(ContentType.JSON)
                .when().delete("/times/{id}", 1L)
                .then().log().all()
                .statusCode(204);
    }

    @Test
    void 예약_시간들의_예약_가능_여부를_조회한다() {
        예약_시간을_생성한다();
        RestAssured.given().log().all()
                .cookie("JSESSIONID", getSessionId())
                .contentType(ContentType.JSON)
                .queryParam("date", LocalDate.now(FIXED_CLOCK).toString())
                .queryParam("themeId", 1L)
                .when().get("/times/available")
                .then().log().all()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .body("[0].id", is(1))
                .body("[0].startAt", is("10:00"))
                .body("[0].isReserved", is(false));
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
