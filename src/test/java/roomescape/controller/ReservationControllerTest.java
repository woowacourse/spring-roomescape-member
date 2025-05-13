package roomescape.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.domain.Role;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationControllerTest {

    private static final String DATE = LocalDate.MAX.toString();
    private static final Long TEST_ID = 1L;
    private static final Long TIME_ID = 1L;
    private static final Long THEME_ID = 1L;

    @Value("${jwt.secret.key}")
    private String secretKey;

    @Test
    @DisplayName("예약 목록을 조회한다")
    void readAllReservations() {
        RestAssured.given().log().all()
                .when().get("reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(10));
    }

    @Test
    @DisplayName("예약을 생성한다")
    void createReservation() {
        //given
        Map<String, Object> request = createReservationRequest();

        String token = Jwts.builder()
                .setSubject(String.valueOf(TEST_ID))
                .claim("role", Role.USER)
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();

        //when
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .body(request)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .body("id", not(nullValue()),
                        "date", is(DATE),
                        "reservationTime.id", is(1),
                        "theme.id", is(1),
                        "member.id", is(1)
                );
    }

    @Test
    @DisplayName("예약을 삭제한다")
    void deleteReservation() {
        RestAssured.given().log().all()
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(204);
    }

    private Map<String, Object> createReservationRequest() {
        Map<String, Object> reservation = new HashMap<>();
        reservation.put("date", DATE);
        reservation.put("timeId", TIME_ID);
        reservation.put("themeId", THEME_ID);
        return reservation;
    }
}
