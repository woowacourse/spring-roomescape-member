package roomescape.reservation.controller;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.auth.provider.JwtTokenProvider;
import roomescape.member.domain.Member;
import roomescape.reservation.dto.ReservationRequest;
import roomescape.reservation.dto.SearchRequest;

import java.time.LocalDate;

import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationControllerTest {
    private static final String NAME = "김철수";
    private static final String EMAIL = "chulsoo@example.com";
    private static final String PASSWORD = "123";
    private static final String ROLE = "USER";

    @LocalServerPort
    private int port;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void findAllTest() {
        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/reservations")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(25));
    }

    @Test
    void findBySearchInfo() {
        SearchRequest searchRequest = new SearchRequest(1L, 1L,
                LocalDate.of(2024, 4, 28), LocalDate.of(2024, 4, 29));

        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(searchRequest)
                .when()
                .post("/reservations/search")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(3));
    }

    @Test
    void saveTest() {
        ReservationRequest reservationRequest = new ReservationRequest(
                LocalDate.of(3000, 1, 1), 1L, 1L);

        Member member = new Member(1L, NAME, EMAIL, PASSWORD, ROLE);
        String memberToken = jwtTokenProvider.createToken(member);

        RestAssured.given()
                .cookie("token", memberToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(reservationRequest)
                .log().all()
                .when()
                .post("reservations")
                .then()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    void deleteTest() {
        RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete("reservations/1")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
