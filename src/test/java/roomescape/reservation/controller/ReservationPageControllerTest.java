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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationPageControllerTest {
    private static final String NAME = "김철수";
    private static final String EMAIL = "chulsoo@example.com";
    private static final String PASSWORD = "123";
    private static final String ROLE = "USER";

    @LocalServerPort
    private int port;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private String memberToken;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        memberToken = jwtTokenProvider.createToken(new Member(1L, NAME, EMAIL, PASSWORD, ROLE));
    }

    @Test
    void reservationTest() {
        RestAssured.given()
                .cookie(memberToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/reservation")
                .then()
                .log()
                .all()
                .statusCode(HttpStatus.OK.value());
    }
}
