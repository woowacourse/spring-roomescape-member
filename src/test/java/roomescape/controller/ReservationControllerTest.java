package roomescape.controller;

import static org.hamcrest.Matchers.equalTo;
import java.util.HashMap;
import java.util.Map;
import io.restassured.RestAssured;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import roomescape.domain.member.Member;
import roomescape.domain.member.Role;
import roomescape.service.AuthService;
import roomescape.service.stub.StubReservationService;
import roomescape.service.stub.StubReservationTimeService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class ReservationControllerTest {

    private final AuthService authService;

    @Autowired
    public ReservationControllerTest(AuthService authService) {
        this.authService = authService;
    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        public ReservationController reservationController() {
            return new ReservationController(new StubReservationService());
        }
    }

    private Cookie getFakeTokenCookie() {
        Member member = new Member(1L, "히스타", "hista@woowa.jjang", Role.ADMIN);
        return authService.generateTokenCookie(member);
    }

    @Test
    void createByAuth() {
        Cookie cookie = getFakeTokenCookie();
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("memberId", 1);
        requestBody.put("date", "2001-10-02");
        requestBody.put("timeId", 1);
        requestBody.put("themeId", 1);

        RestAssured.given()
                .cookie(cookie.getName(), cookie.getValue())
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/reservations")
                .then()
                .statusCode(201)
                .body("id", equalTo(1))
                .body("member.id", equalTo(1))
                .body("date", equalTo("2025-05-01"))
                .body("time.startAt", equalTo("12:00:00"))
                .body("theme.name", equalTo("name"));
    }

    @Test
    void createByMemberId() {
        Cookie cookie = getFakeTokenCookie();
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("memberId", 1);
        requestBody.put("date", "2001-10-02");
        requestBody.put("timeId", 1);
        requestBody.put("themeId", 1);

        RestAssured.given()
                .cookie(cookie.getName(), cookie.getValue())
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/admin/reservations")
                .then()
                .statusCode(201)
                .body("id", equalTo(1));
    }

    @Test
    void readAll() {
        Cookie cookie = getFakeTokenCookie();

        RestAssured.given()
                .cookie(cookie.getName(), cookie.getValue())
                .when()
                .get("/reservations")
                .then()
                .statusCode(200)
                .body("[0].id", equalTo(1))
                .body("[0].member.id", equalTo(1))
                .body("[0].date", equalTo("2025-05-01"))
                .body("[0].time.startAt", equalTo("12:00:00"))
                .body("[0].theme.name", equalTo("name"));
    }

    @Test
    void deleteReservation() {
        Cookie cookie = getFakeTokenCookie();

        RestAssured.given()
                .cookie(cookie.getName(), cookie.getValue())
                .when()
                .delete("/reservations/1")
                .then()
                .statusCode(204);
    }
}
