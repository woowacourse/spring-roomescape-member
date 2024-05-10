package roomescape.admin.controller;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.admin.dto.AdminReservationRequest;
import roomescape.auth.provider.JwtTokenProvider;
import roomescape.member.domain.Member;

import java.time.LocalDate;

import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AdminControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private String adminToken;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        adminToken = jwtTokenProvider.createToken(
                new Member(1L, "한태웅", "taewoong@example.com", "123", "ADMIN"));
    }

    @Test
    @DisplayName("관리자가 {date, themeId, timeId, memberId}로 예약을 생성할 수 있다.")
    void saveReservationTest() {
        AdminReservationRequest adminReservationRequest = new AdminReservationRequest(
                LocalDate.of(3000, 1, 1), 1L, 1L, 1L);

        RestAssured.given()
                .cookie("token", adminToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(adminReservationRequest)
                .when()
                .post("/admin/reservations")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .log().all()
                .body("date", equalTo("3000-01-01"),
                        "theme.id", equalTo(1),
                        "time.id", equalTo(1),
                        "member.id", equalTo(1));
    }
}
