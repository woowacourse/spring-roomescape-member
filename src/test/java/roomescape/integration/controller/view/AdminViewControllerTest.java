package roomescape.integration.controller.view;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import io.restassured.RestAssured;
import jakarta.servlet.http.Cookie;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import roomescape.domain.member.Member;
import roomescape.domain.member.Role;
import roomescape.service.AuthService;

/**
 * 해당 클래스는 admin/ 하위의 페이지 연결을 테스트합니다.
 * SpringBootTest 를 사용하여 실제로 서버를 띄우는 통합 테스트입니다.
 */
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class AdminViewControllerTest {
    private final Cookie fakeTokenCookie;

    public AdminViewControllerTest() {
        AuthService authService = new AuthService();
        Member member = new Member(1L, "히스타", "hista@woowa.jjang", "1q2w3e4r!", Role.ADMIN);
        this.fakeTokenCookie = authService.generateTokenCookie(member);
    }

    @DisplayName("/admin 페이지 연결 테스트")
    @Test
    void adminPage() {
        RestAssured.given().log().all().cookie(fakeTokenCookie.getName(), fakeTokenCookie.getValue())
                .when().get("/admin")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("/admin/reservation 페이지 연결 테스트")
    @Test
    void adminReservationPage() {
        RestAssured.given().log().all().cookie(fakeTokenCookie.getName(), fakeTokenCookie.getValue())
                .when().get("admin/reservation")
                .then().log().all()
                .statusCode(200);
    }

    @DisplayName("/admin/time 페이지 연결 테스트")
    @Test
    void adminTimePage() throws URISyntaxException, IOException {
        // [요구사항 - 학습 목적] RestAssured 를 사용하지 않고 테스트 코드 작성해보기.
        HttpURLConnection connection = (HttpURLConnection) new URI("http://localhost:8080/admin/time").toURL().openConnection();
        connection.setRequestProperty("Cookie", fakeTokenCookie.getName() + "=" + fakeTokenCookie.getValue());
        int responseCode = connection.getResponseCode();

        Assertions.assertThat(responseCode).isEqualTo(200);
    }

    @DisplayName("/admin/theme 페이지 연결 테스트")
    @Test
    void adminThemePage() throws URISyntaxException, IOException {
        HttpURLConnection connection = (HttpURLConnection) new URI("http://localhost:8080/admin/theme").toURL().openConnection();
        connection.setRequestProperty("Cookie", fakeTokenCookie.getName() + "=" + fakeTokenCookie.getValue());
        int responseCode = connection.getResponseCode();

        Assertions.assertThat(responseCode).isEqualTo(200);
    }
}
