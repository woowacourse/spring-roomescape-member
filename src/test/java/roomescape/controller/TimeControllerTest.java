package roomescape.controller;


import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.dto.TimeRequest;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TimeControllerTest {

    @Test
    public void 전체_시간_조회_API() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().get("admin/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(14));
    }

    @Test
    public void 테마_별_예약가능한_시간_조회_API() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().get("/times?themeId=2&date=2026-05-04")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(12));
    }

    @Test
    public void 예약_가능한_시간_삭제_API() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().delete("admin/times/1")
                .then().log().all()
                .statusCode(500);
    }

    @Test
    public void 예약_가능한_시간_추가_API() {
        TimeRequest timeRequest = new TimeRequest(LocalTime.of(8, 0));

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(timeRequest)
                .when().post("admin/times")
                .then().log().all()
                .statusCode(201)
                .body("size()", is(2));
    }
}
