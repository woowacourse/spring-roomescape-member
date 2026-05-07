package roomescape;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThanOrEqualTo;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

/*
 * 미션2 사이클1 - 인기 테마 조회 API 요구사항 테스트.
 * 비즈니스 규칙 4가지를 분리해 검증한다:
 *  1) 7일 이내 예약만 집계 (8일 이전은 제외)
 *  2) 오늘 예약은 제외 (date < CURRENT_DATE)
 *  3) 예약 건수 내림차순 정렬 — 상위 두 자리는 결정적이므로 검증
 * 4) 최대 10개
 * <p>
 * 시드 데이터로부터 기대값:
 * - 7일 이내 예약이 있는 테마: 11개 (theme 1~3, 5~12)
 * - 무인도(theme 1): 어제 3 + 5일전 2 = 5건  ->  1등
 *  - 도시(theme2): 5일전 4건 = 4건   ->  2등
 *  - 그 외 9개 테마: 각 1건씩
 * - 8일전 도시 2건은 7일 범위 밖이라 빠짐
 * - 오늘 무인도 5건은 오늘이라 빠짐
 * 최종적으로 11개 중 10개만 보임

 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class PopularThemeStepTest {

    @Test
    @DisplayName("예약 건수 내림차순으로 정렬된다")
    void 예약_건수_내림차순_정렬() {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().get("/user/themes/popular")
                .then().log().all()
                .statusCode(200)
                .extract();

        List<String> names = response.jsonPath().getList("name");
        List<Integer> counts = response.jsonPath().getList("reservationCount");

        // 1등은 무인도(5건), 2등은 도시(4건)
        assert names.get(0).equals("무인도 탈출") : "1등은 무인도여야 함, 실제: " + names.get(0);
        assert counts.get(0) == 5 : "1등 건수는 5여야 함, 실제: " + counts.get(0);
        assert names.get(1).equals("도시 탈출") : "2등은 도시여야 함, 실제: " + names.get(1);
        assert counts.get(1) == 4 : "2등 건수는 4여야 함, 실제: " + counts.get(1);
    }

    @Test
    @DisplayName("8일 전 예약은 집계에서 제외된다")
    void 기간_밖_예약_제외() {
        // 도시 테마는 5일전 4건 + 8일전 2건 = 총 6건 예약
        // 8일전이 제외되면 4건이 집계되어야 함
        ExtractableResponse<Response> response = RestAssured.given()
                .when().get("/user/themes/popular")
                .then().statusCode(200).extract();

        List<String> names = response.jsonPath().getList("name");
        List<Integer> counts = response.jsonPath().getList("reservationCount");

        int cityIndex = names.indexOf("도시 탈출");
        assert cityIndex >= 0 : "도시 테마가 응답에 있어야 함";
        assert counts.get(cityIndex) == 4
                : "도시 테마 건수는 4여야 함 (8일전 2건 제외), 실제: " + counts.get(cityIndex);
    }

    @Test
    @DisplayName("오늘 예약은 집계에서 제외된다")
    void 오늘_예약_제외() {
        // 무인도 테마는 어제 3 + 5일전 2 + 오늘 5 = 총 10건
        // 오늘이 제외되면 5건만 집계되어야 함
        ExtractableResponse<Response> response = RestAssured.given()
                .when().get("/user/themes/popular")
                .then().statusCode(200).extract();

        List<String> names = response.jsonPath().getList("name");
        List<Integer> counts = response.jsonPath().getList("reservationCount");

        int islandIndex = names.indexOf("무인도 탈출");
        assert islandIndex >= 0 : "무인도 테마가 응답에 있어야 함";
        assert counts.get(islandIndex) == 5
                : "무인도 테마 건수는 5여야 함 (오늘 5건 제외), 실제: " + counts.get(islandIndex);
    }

    @Test
    @DisplayName("최대 10개를 반환한다")
    void 최대_10개를_반환한다() {
        RestAssured.given().log().all()
                .when().get("/user/themes/popular")
                .then().log().all()
                .statusCode(200)
                .body("size()", lessThanOrEqualTo(10));
    }

    @Test
    @DisplayName("응답 항목은 테마 정보와 예약 건수를 포함한다")
    void 응답_항목_형태() {
        RestAssured.given().log().all()
                .when().get("/user/themes/popular")
                .then().log().all()
                .statusCode(200)
                .body("[0].id", is(1))
                .body("[0].name", is("무인도 탈출"))
                .body("[0].reservationCount", is(5));
    }
}
