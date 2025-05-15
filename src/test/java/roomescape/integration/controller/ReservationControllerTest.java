package roomescape.integration.controller;

import static org.hamcrest.Matchers.equalTo;
import java.util.HashMap;
import java.util.Map;
import io.restassured.RestAssured;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import roomescape.controller.ReservationController;
import roomescape.domain.member.Member;
import roomescape.domain.member.Role;
import roomescape.service.AuthService;
import roomescape.integration.service.stub.StubReservationService;

/**
 * 해당 클래스는 ReservationController 의 요청/응답 형식을 테스트합니다.
 * SpringBootTest 어노테이션을 사용하여 실제로 서버를 띄우는 통합 테스트입니다.
 * 단, StubReservationService 를 사용하여 프로덕션 Service 와 분리했고, 실제 DB에 접근하지 않고 테스트합니다.
 */
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
