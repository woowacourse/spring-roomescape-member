package roomescape.reservation.controller;

import static org.hamcrest.Matchers.is;
import static roomescape.util.JwtTokenProvider.TOKEN;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import roomescape.member.dto.LoginRequest;
import roomescape.reservation.dto.ReservationTimeRequest;
import roomescape.reservation.service.ReservationTimeService;
import roomescape.util.ControllerTest;

@DisplayName("예약 시간 API 통합 테스트")
class ReservationTimeControllerTest extends ControllerTest {
    private String adminToken;

    @BeforeEach
    void setDate() {
        adminToken = RestAssured
                .given().log().all()
                .body(new LoginRequest("admin@email.com", "password"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login")
                .then().log().all().extract()
                .cookie("token");
    }

    @DisplayName("시간 생성 시, 201을 반환한다.")
    @Test
    void create() {
        //given
        Map<String, String> params = new HashMap<>();
        params.put("startAt", "10:00");

        //when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie(TOKEN, adminToken)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(201);
    }

    @DisplayName("시간 생성 시, 잘못된 시간 형식에 대해 400을 반환한다.")
    @ParameterizedTest
    @ValueSource(strings = {"", "26:57", "23:89", "-1"})
    void createBadRequest(String startAt) {
        //given
        Map<String, String> params = new HashMap<>();
        params.put("startAt", startAt);

        //when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie(TOKEN, adminToken)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("시간 조회 시, 200을 반환한다.")
    @Test
    void findAll() {
        //given & when & then
        RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(4));
    }

    @DisplayName("시간 삭제 시, 200을 반환한다.")
    @Test
    void delete() {
        //given & when & then
        RestAssured.given().log().all()
                .cookie(TOKEN, adminToken)
                .when().delete("/times/4")
                .then().log().all()
                .statusCode(200);
    }
}
