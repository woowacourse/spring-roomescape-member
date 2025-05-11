package roomescape.global.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.domain.Role;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class WebMvcConfigTest {

    @Value("${jwt.secret.key}")
    private String secretKey;

    @Test
    @DisplayName("ADMIN 예약 관리 메인 페이지를 렌더링한다")
    void displayAdminMainPage() {
        String token = createAdminToken();

        RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("admin")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("ADMIN 예약 목록 페이지를 렌더링한다")
    void displayAdminReservationPage() {
        String token = createAdminToken();

        RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("admin/reservation")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("ADMIN 예약 시간 페이지를 렌더링한다")
    void displayAdminTimePage() {
        String token = createAdminToken();

        RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("admin/time")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("ADMIN 예약 테마 페이지를 렌더링한다")
    void displayAdminThemePage() {
        String token = createAdminToken();

        RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("admin/theme")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("User 메인 페이지를 렌더링한다")
    void displayMainPage() {
        RestAssured.given().log().all()
                .when().get("/")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("User 예약 목록 페이지를 렌더링한다")
    void displayReservationPage() {
        RestAssured.given().log().all()
                .when().get("reservation")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("로그인 페이지를 렌더링한다")
    void displayLoginPage() {
        RestAssured.given().log().all()
                .when().get("login")
                .then().log().all()
                .statusCode(200);
    }

    private String createAdminToken() {
        return Jwts.builder()
                .setSubject(String.valueOf(1L))
                .claim("role", Role.ADMIN)
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }
}
