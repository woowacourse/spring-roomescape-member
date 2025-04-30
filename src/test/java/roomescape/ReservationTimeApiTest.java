package roomescape;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ReservationTimeApiTest {

    @Test
        // TODO: 같은 시간 추가 못하게 막아야됨
    void 시간을_추가한다() {
        Map<String, String> params = new HashMap<>();
        params.put("startAt", "10:00");

        RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(params)
            .when().post("/times")
            .then().log().all()
            .statusCode(201);
    }

    @Test
    void 모든_시간을_조회한다() {
        RestAssured.given().log().all()
            .when().get("/times")
            .then().log().all()
            .statusCode(200)
            .body("size()", is(6));
    }

    @Test
    void 시간은_null값을_받을_수_없다() {
        Map<String, String> params = new HashMap<>();
        params.put("startAt", null);

        RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(params)
            .when().post("/times")
            .then().log().all()
            .statusCode(400);
    }

    @Test
    void 예약이_존재할_때_예약시간을_제거하면_에러를_반환한다() {
        RestAssured.given().log().all()
            .when().delete("/times/1")
            .then().log().all()
            .statusCode(400)
            .body(equalTo("이 시간의 예약이 존재합니다."));
    }
}
