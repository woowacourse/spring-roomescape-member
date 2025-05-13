package roomescape.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.common.Role;
import roomescape.common.auth.JwtProvider;
import roomescape.model.Member;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationControllerTest {

    @Test
    @DisplayName("예약 등록 시 date 타입이 올바르지 않는다면 400에러를 반환한다.")
    void test1() {
        Map<String, String> params = new HashMap<>();
        params.put("memberId", "1");
        params.put("date", "ㅇㅇㅇㅇ");
        params.put("timeId", "1");
        params.put("themeId", "1");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("예약 날짜가 null 이라면 400에러를 반환한다.")
    void test2() {
        Map<String, String> params = new HashMap<>();
        params.put("memberId", "1");
        params.put("date", null);
        params.put("timeId", "1");
        params.put("themeId", "1");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("예약 날짜가 공백 이라면 400에러를 반환한다.")
    void test3() {
        Map<String, String> params = new HashMap<>();
        params.put("memberId", "1");
        params.put("date", "");
        params.put("timeId", "1");
        params.put("themeId", "1");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("예약 시간이 null 이라면 400에러를 반환한다.")
    void test4() {
        Map<String, String> params = new HashMap<>();
        params.put("memberId", "1");
        params.put("date", "");
        params.put("timeId", null);
        params.put("themeId", "1");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("예약 테마가 null 이라면 400에러를 반환한다.")
    void test5() {
        Map<String, String> params = new HashMap<>();
        params.put("memberId", "1");
        params.put("date", "");
        params.put("timeId", "1");
        params.put("themeId", null);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("user GET /admin/reservation 요청 시 403 실패")
    @Test
    void reservation_접근_실패() {
        JwtProvider jwtProvider = new JwtProvider();
        String userToken = jwtProvider.createToken(new Member(2L, "다로", "mail", "pass", Role.USER)); // 테스트용 JWT 생성

        RestAssured.given().log().all()
                .cookie("token", userToken)
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(403);
    }

    @DisplayName("로그인한 유저의 정보를 찾을 수 없다면 예약을 생성할 때 404에러를 반환한다")
    @Test
    void 로그인한_사용자의_예약_생성_테스트() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("date", String.valueOf(LocalDate.now().plusDays(1)));
        params.put("timeId", "1");
        params.put("themeId", "1");

        JwtProvider jwtProvider = new JwtProvider();
        String userToken = jwtProvider.createToken(
                new Member(6L, "사용자", "user@email.com", "password", Role.USER)
        );

        // when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", userToken)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(404);
    }

}