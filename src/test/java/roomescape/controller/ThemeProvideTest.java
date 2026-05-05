package roomescape.controller;


import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
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
    @DisplayName("갯수 테스트")
    void readAvailableTime() {

        RestAssured.given().log().all()
                    .queryParam("date", "2026-05-05")
                    .queryParam("limit", 10)
                    .when().get("/themes")
                    .then().statusCode(200)
                    .log().all()
                    .body("size()",is(10));
    }

    @Test
    @Sql(scripts = "/testData.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @DisplayName("오름차순 테스트")
    void sortThemes() {

        RestAssured.given().log().all()
                .queryParam("date", "2026-05-05")
                .queryParam("limit", 10)
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(10))
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


