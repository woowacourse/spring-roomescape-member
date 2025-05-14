package roomescape.controller.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.auth.controller.dto.SignupRequest;
import roomescape.auth.infrastructure.JwtTokenProvider;
import roomescape.controller.dto.reservation.ReservationMemberRequest;
import roomescape.controller.dto.reservationTime.ReservationTimeRequest;
import roomescape.controller.dto.theme.ThemeRequest;
import roomescape.entity.Member;
import roomescape.entity.Role;

import java.time.LocalDate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationApiTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private Long themeId;
    private Long timeId;

    @BeforeEach
    void setUp() {

        // Theme 생성
        var themeRequest = new ThemeRequest(
                "스테이지",
                "인기 아이돌 실종 사건",
                "무엇보다 무섭다"
        );
        themeId = Long.valueOf(RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(themeRequest)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201)
                .extract().path("id").toString());

        // 시간 생성
        ReservationTimeRequest reservationTimeRequest = new ReservationTimeRequest("19:00");
        timeId = Long.valueOf(RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationTimeRequest)
                .when().post("/times")
                .then().log().all()
                .statusCode(201)
                .extract().path("id").toString());
    }

    @Test
    @DisplayName("예약 조회 테스트")
    void searchReservationsWithStatus200Test() {
        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("예약 생성 테스트")
    void createReservationsWithStatus201Test() {
        ReservationMemberRequest reservationMemberRequest = new ReservationMemberRequest(LocalDate.now(), timeId, themeId);
        // JWT 토큰 생성 (회원 정보 사용)
        String token = jwtTokenProvider.createToken(Member.afterSave(2, "koi", "ywc@com", "123", Role.USER));

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .body(reservationMemberRequest)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);
    }

    @Test
    @DisplayName("예약 삭제 성공")
    void deleteReservationReturns204() {
        SignupRequest signupRequest = new SignupRequest(
                "레몬",
                "suwon@naver.com",
                "123"
        );
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(signupRequest)
                .when().post("/members")
                .then().statusCode(201)
                .extract().response();

        ReservationMemberRequest reservationMemberRequest = new ReservationMemberRequest(LocalDate.now(), timeId, themeId);
        String token = jwtTokenProvider.createToken(Member.afterSave(2, "레몬", "suwon@naver.com", "123", Role.USER));

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .body(reservationMemberRequest)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        RestAssured.given()
                .when().delete("/reservations/" + 1)
                .then().statusCode(204);
    }
}
