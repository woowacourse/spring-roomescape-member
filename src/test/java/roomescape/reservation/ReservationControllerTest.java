package roomescape.reservation;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationControllerTest {

    @Test
    void 예약추가_시간가능_성공() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "초록");
        params.put("themeId", 2L);
        params.put("date", "2026-05-05");
        params.put("timeId", 7L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/user/reservations")
                .then().log().all()
                .statusCode(201);
    }

    @Test
    void 예약추가_시간없음_실패() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "초록");
        params.put("date", "2026-05-05");
        params.put("timeId", 15L);
        params.put("themeId", 2L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/user/reservations")
                .then().log().all()
                .statusCode(500);
    }

    @Test
    void 예약삭제_성공() {
        RestAssured.given().log().all()
                .when().delete("/user/reservations/1")
                .then().log().all()
                .statusCode(204);
    }
}
