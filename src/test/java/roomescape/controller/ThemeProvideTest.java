package roomescape.controller;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ThemeProvideTest {

    @Test
    @Sql(scripts = "/testData.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @DisplayName("최근 예약이 많았던 테마는 정해진 갯수 한계 내에서 조회되어야 한다.")
    void readAvailableTime() {
        long limit = 10;

        LocalDate today = LocalDate.of(2026, 5, 5);
        LocalDate startAt = today.minusWeeks(1);
        LocalDate endAt = today.minusDays(1);

        RestAssured.given().log().all()
                    .queryParam("startAt", startAt.toString())
                    .queryParam("endAt", endAt.toString())
                    .queryParam("limit", limit)
                    .when().get("/themes/popular")
                    .then().statusCode(200)
                    .log().all()
                    .body("size()",is(10));
    }

    @Test
    @Sql(scripts = "/testData.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @DisplayName("최근 예약이 많았던 테마는 기간 내 예약이 많은 상위 테마들을 조회할 수 있어야 한다. ")
    void sortThemes() {
        LocalDate today = LocalDate.of(2026, 5, 5);
        LocalDate startAt = today.minusWeeks(1);
        LocalDate endAt = today.minusDays(1);

        RestAssured.given().log().all()
                .queryParam("startAt", startAt.toString())
                .queryParam("endAt",endAt.toString())
                .queryParam("limit", 10)
                .when().get("/themes/popular")
                .then().log().all()
                .statusCode(200)
                .body("[0].name", is("공포의 저택"))
                .body("[1].name", is("우주 탐험대"))
                .body("[2].name", is("탐정 사무소"))
                .body("[3].name", is("마법사의 탑"))
                .body("[4].name", is("해적선"))
                .body("[5].name", is("고대 신전"))
                .body("[6].name", is("좀비 연구소"))
                .body("[7].name", is("타임머신"))
                .body("[8].name", is("사막의 오아시스"))
                .body("[9].name", is("폐광"));
    }
}
