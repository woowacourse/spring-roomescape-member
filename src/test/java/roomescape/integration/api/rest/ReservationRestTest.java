package roomescape.integration.api.rest;

import static org.hamcrest.Matchers.*;
import static roomescape.common.Constant.FIXED_CLOCK;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
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

class ReservationRestTest extends RestAssuredTestBase {

    private Integer timeId;
    private Integer themeId;

    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        timeId = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(Map.of("startAt", "10:00"))
                .when().post("/times")
                .then().statusCode(201)
                .extract().path("id");
        themeId = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(Map.of(
                        "name", "어드벤처",
                        "description", "정글 탐험 컨셉",
                        "thumbnail", "https://example.com/adventure.jpg"
                ))
                .when().post("/themes")
                .then().statusCode(201)
                .extract().path("id");
    }

    @Test
    void 예약을_생성한다() {
        Map<String, Object> request = Map.of(
                "date", LocalDate.now(FIXED_CLOCK).plusDays(1).toString(),
                "timeId", timeId,
                "themeId", themeId
        );
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("JSESSIONID", getSessionId())
                .body(request)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .body("id", is(1))
                .body("name", is("홍길동"))
                .body("date", is(LocalDate.now(FIXED_CLOCK).plusDays(1).toString()))
                .body("time.startAt", is("10:00"))
                .body("theme.name", is("어드벤처"));
    }

    @Test
    void 예약_목록을_조회한다() {
        예약을_생성한다();
        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .body("[0].name", is("홍길동"))
                .body("[0].time.id", is(1))
                .body("[0].time.startAt", is("10:00"))
                .body("[0].theme.id", is(1))
                .body("[0].theme.name", is("어드벤처"))
                .body("[0].theme.description", is("정글 탐험 컨셉"))
                .body("[0].theme.thumbnail", is("https://example.com/adventure.jpg"));
    }

    @Test
    void 예약을_삭제한다() {
        예약을_생성한다();
        RestAssured.given().log().all()
                .when().delete("/reservations/{id}", 1)
                .then().log().all()
                .statusCode(204);
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
