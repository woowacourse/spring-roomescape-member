package roomescape.controller;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import roomescape.controller.request.AdminReservationRequest;
import roomescape.model.Member;
import roomescape.model.Role;

import java.time.LocalDate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AdminControllerTest {

    private static final String SECRET_KEY = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";

    @DisplayName("관리자가 어드민 API 접근에 시도할 경우 예외를 반환하지 않는다.")
    @Test
    void should_throw_exception_when_admin_contact() {
        Member member = new Member(2L, "우테코", "wtc@gmail.com", "wtc123!", Role.ADMIN);
        String token = Jwts.builder()
                .subject(String.valueOf(member.getId()))
                .claim("name", member.getName())
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .compact();
        AdminReservationRequest request = new AdminReservationRequest(LocalDate.now().plusDays(1), 1L, 1L, 1L);

        RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .body(request)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(201);
    }

    @DisplayName("일반 유저가 어드민 API 접근에 시도할 경우 예외를 반환한다.")
    @Test
    void should_not_throw_exception_when_user_contact() {
        Member member = new Member(1L, "에버", "treeboss@gmail.com", "treeboss123!", Role.USER);
        String token = Jwts.builder()
                .subject(String.valueOf(member.getId()))
                .claim("name", member.getName())
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                .compact();
        AdminReservationRequest request = new AdminReservationRequest(LocalDate.now().plusDays(1), 1L, 1L, 1L);

        RestAssured
                .given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .body(request)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(401);
    }
}
