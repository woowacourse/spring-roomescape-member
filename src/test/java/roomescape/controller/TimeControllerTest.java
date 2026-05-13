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
import roomescape.exception.ErrorCode;

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
                .when().delete("admin/times/13")
                .then().log().all()
                .statusCode(204);
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

    @Test
    public void 예약_시간_등록_시_정각이_아니면_422를_반환한다() {
        TimeRequest timeRequest = new TimeRequest(LocalTime.of(8, 22));

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(timeRequest)
                .when().post("admin/times")
                .then().log().all()
                .statusCode(422)
                .body("message", is(ErrorCode.TIME_NOT_ON_THE_HOUR.getMessage()));
    }

    @Test
    public void 예약_시간_등록_시_중복된_시간이면_409를_반환한다() {
        TimeRequest timeRequest = new TimeRequest(LocalTime.of(10, 0));

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(timeRequest)
                .when().post("admin/times")
                .then().log().all()
                .statusCode(409)
                .body("message", is(ErrorCode.TIME_DUPLICATED.getMessage()));
    }

    @Test
    public void 예약_시간_삭제_시_존재하지_않는_시간을_삭제하는_경우_404를_반환한다() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().delete("admin/times/-1")
                .then().log().all()
                .statusCode(404)
                .body("message", is(ErrorCode.TIME_NOT_FOUND.getMessage()));
    }

    @Test
    public void 예약_시간_삭제_시_예약이_존재하는_시간을_삭제하는_경우_409를_반환한다() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().delete("admin/times/1")
                .then().log().all()
                .statusCode(409)
                .body("message", is(ErrorCode.TIME_HAS_RESERVATIONS.getMessage()));
    }
}
