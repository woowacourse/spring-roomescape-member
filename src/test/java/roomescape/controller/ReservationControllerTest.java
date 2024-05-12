package roomescape.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.dto.*;
import roomescape.service.MemberService;
import roomescape.service.ReservationTimeService;
import roomescape.service.ThemeService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationControllerTest {

    @Autowired
    LoginController loginController;
    @Autowired
    ReservationTimeService reservationTimeService;
    @Autowired
    ThemeService themeService;
    @Autowired
    MemberService memberService;

    @BeforeEach
    void setUp() {
        reservationTimeService.save(new ReservationTimeRequest(LocalTime.of(15, 40)));
        themeService.save(new ThemeRequest("레벨2 탈출", "우테코 레벨2를 탈출하는 내용입니다.", "https://i.pinimg.com/236x/6e/bc/46/6ebc461a94a49f9ea3b8bbe2204145d4.jpg"));
        memberService.join(new MemberRequest("test@email.com", "password", "name"));
    }

    @Test
    @DisplayName("예약 정보를 조회한다.")
    void readReservations() {
        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }

    @Test
    @DisplayName("예약을 추가한다.")
    void createReservation() {
        String accessToken = getAccessToken();
        Map<String, String> params = getParams();

        RestAssured.given().log().all()
                .cookie("token", accessToken)
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .body("id", is(1));

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Test
    @DisplayName("예약을 삭제한다.")
    void deleteReservation() {
        String accessToken = getAccessToken();
        Map<String, String> params = getParams();

        RestAssured.given().log().all()
                .cookie("token", accessToken)
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .body("id", is(1));

        RestAssured.given().log().all()
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(204);

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }

    private Map<String, String> getParams() {
        Map<String, String> params = new HashMap<>();
        LocalDate localDate = LocalDate.now().plusDays(1);

        params.put("date", localDate.toString());
        params.put("timeId", "1");
        params.put("themeId", "1");

        return params;
    }

    private String getAccessToken() {
        final ResponseEntity<TokenResponse> response = loginController.login(new LoginRequest("test@email.com", "password"), new MockHttpServletResponse());
        return response.getBody().accessToken();
    }
}
