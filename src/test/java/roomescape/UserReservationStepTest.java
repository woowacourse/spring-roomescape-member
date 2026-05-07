package roomescape;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

/*
 * 미션2 사이클1 - 사용자 예약 API 요구사항 테스트.
 * data.sql 시드 데이터(시간 6개, 테마 13개, 예약 25건)를 토대로 정상 흐름을 검증한다.
 *
 * 검증 시나리오:
 * 1) 미래 날짜 + 시드 테마의 가능 시간 조회 → 시드의 6개 시간 모두 보임
 * 2) 그중 하나로 예약 생성
 * 3) 같은 (날짜, 테마)로 다시 조회 → 5개 (방금 예약한 시간 빠짐)
 * 4) 같은 (날짜, 시간)이라도 다른 테마에는 여전히 예약 가능
 */

/**
 *
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserReservationStepTest {

    private static final String FUTURE_DATE = "2050-12-31";
    private static final long SEED_THEME_ISLAND = 1L;   // data.sql: 무인도 탈출
    private static final long SEED_THEME_CITY = 2L;     // data.sql: 도시 탈출
    private static final long SEED_TIME_10 = 1L;        // data.sql: 10:00
    private static final int SEED_TIME_COUNT = 6;       // data.sql: 시간  6개

    @Test
    @DisplayName("사용자 예약 정상 흐름: 가능 시간 조회 → 예약 → 다시 조회 시 해당 시간이 빠진다")
    void 사용자_예약_정상_흐름() {
        // 1) 미래 날짜 + 시드 테마의 가능 시간 조회 → 6개 (시드 시간 전부)
        RestAssured.given().log().all()
                .when().get("/user/themes/" + SEED_THEME_ISLAND + "/available-times?date=" + FUTURE_DATE)
                .then().log().all()
                .statusCode(200)
                .body("size()", is(SEED_TIME_COUNT));

        // 2) 10:00 으로 예약 생성
        Map<String, Object> reservationBody = new HashMap<>();
        reservationBody.put("name", "브라운");
        reservationBody.put("date", FUTURE_DATE);
        reservationBody.put("timeId", SEED_TIME_10);
        reservationBody.put("themeId", SEED_THEME_ISLAND);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationBody)
                .when().post("/user/reservations")
                .then().log().all()
                .statusCode(201)
                .body("name", is("브라운"))
                .body("date", is(FUTURE_DATE))
                .body("time.id", is((int) SEED_TIME_10))
                .body("theme.id", is((int) SEED_THEME_ISLAND));

        // 3) 다시 조회 → 5개 (10:00이 빠져 있어야 함)
        ExtractableResponse<Response> afterReservation = RestAssured.given().log().all()
                .when().get("/user/themes/" + SEED_THEME_ISLAND + "/available-times?date=" + FUTURE_DATE)
                .then().log().all()
                .statusCode(200)
                .body("size()", is(SEED_TIME_COUNT - 1))
                .extract();

        // 빠진 시간이 정확히 10:00(id=1)인지 확인
        List<Integer> remainingIds = afterReservation.jsonPath().getList("id");
        assert !remainingIds.contains((int) SEED_TIME_10);
    }

    @Test
    @DisplayName("같은_시간_다른_테마는_각각_예약_가능")
    void 같은_시간_다른_테마는_각각_예약_가능() {
        // 무인도 테마에 10:00 예약
        Map<String, Object> first = new HashMap<>();
        first.put("name", "브라운");
        first.put("date", FUTURE_DATE);
        first.put("timeId", SEED_TIME_10);
        first.put("themeId", SEED_THEME_ISLAND);
        RestAssured.given().contentType(ContentType.JSON).body(first)
                .when().post("/user/reservations")
                .then().statusCode(201);

        // 도시 테마의 같은 날짜 가능 시간 조회 → 10:00 여전히 보여야 함
        RestAssured.given().log().all()
                .when().get("/user/themes/" + SEED_THEME_CITY + "/available-times?date=" + FUTURE_DATE)
                .then().log().all()
                .statusCode(200)
                .body("size()", is(SEED_TIME_COUNT));

        // 그리고 실제로 같은 (날짜, 시간)에 다른 테마로 예약 가능
        Map<String, Object> second = new HashMap<>();
        second.put("name", "콘");
        second.put("date", FUTURE_DATE);
        second.put("timeId", SEED_TIME_10);
        second.put("themeId", SEED_THEME_CITY);
        RestAssured.given().contentType(ContentType.JSON).body(second)
                .when().post("/user/reservations")
                .then().statusCode(201);
    }
}
