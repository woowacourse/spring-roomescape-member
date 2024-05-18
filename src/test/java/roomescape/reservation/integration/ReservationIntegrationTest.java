package roomescape.reservation.integration;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.util.Date;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import roomescape.member.domain.Member;
import roomescape.member.security.crypto.JwtTokenProvider;
import roomescape.reservation.dto.ReservationRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/schema-test.sql", "/data-test.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_METHOD)
class ReservationIntegrationTest {

    @Value("${security.jwt.token.secret-key}")
    private String secretKey;
    @Value("${security.jwt.token.expire-length}")
    private long validityInMilliseconds;

    @LocalServerPort
    private int port;

    private String token;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        Member member = new Member(1, "valid", "testUser@email.com", "pass");
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(secretKey, validityInMilliseconds);
        token = jwtTokenProvider.createToken(member, new Date());
    }

    @Test
    @DisplayName("정상적인 요청에 대하여 예약을 정상적으로 등록, 조회, 삭제한다.")
    void adminReservationPageWork() {
        ReservationRequest reservationRequest = new ReservationRequest(LocalDate.MAX, "test", 1L, 1L);
        Map<String, String> request = Map.of(
                "date", LocalDate.MAX.toString(),
                "timeId", "1",
                "themeId", "1"
        );
        RestAssured.given()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .body(request)
                .when()
                .post("/reservations")
                .then()
                .statusCode(201);

        RestAssured.given()
                .cookie("token", token)
                .when()
                .get("/reservations")
                .then()
                .statusCode(200)
                .body("size()", is(3));

        RestAssured.given()
                .cookie("token", token)
                .when()
                .delete("/reservations/1")
                .then()
                .statusCode(204);

        RestAssured.given()
                .cookie("token", token)
                .when()
                .get("/reservations")
                .then()
                .statusCode(200)
                .body("size()", is(2));
    }

    @Test
    @DisplayName("예약을 요청시 존재하지 않은 예약 시간의 id일 경우 예외가 발생한다.")
    void notExistTime() {
        ReservationRequest reservationRequest = new ReservationRequest(LocalDate.MAX, "polla", 99L, 1L);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .body(reservationRequest)
                .when()
                .post("/reservations")
                .then()
                .statusCode(500);
    }

    @Test
    @DisplayName("모든 예약 시간 정보를 조회한다.")
    void findReservationTimeList() {
        RestAssured.given()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .when()
                .get("/reservations/times/1?date=" + LocalDate.now())
                .then()
                .statusCode(200);
    }

}
