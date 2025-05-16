package roomescape;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

@Sql(scripts = {"/test-schema.sql"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MissionStepTest {

    /**
     * 관리자 페이지를 조회하기 위해서는 Admin 권한이 필요함 추후에 페이지 권한 테스트 코드 작성하면서, 리팩토링 할 예정
     */

    /*
    @Test
    @DisplayName("admin 페이지를 매핑할 수 있어야 한다.")
    void can_mapping_admin_page() {
        RestAssured.given().log().all()
            .when().get("/admin")
            .then().log().all()
            .statusCode(200);
    }

    @Test
    @DisplayName("관리자 페이지의 예약 조회를 테스트 한다.")
    void admin_reservation_get_test() {
        RestAssured.given().log().all()
            .when().get("/admin/reservation")
            .then().log().all()
            .statusCode(200);

        RestAssured.given().log().all()
            .when().get("/reservations")
            .then().log().all()
            .statusCode(200)
            .body("size()",
                is(0)); // 아직 생성 요청이 없으니 Controller에서 임의로 넣어준 Reservation 갯수 만큼 검증하거나 0개임을 확인하세요.
    }
     */
    @Test
    @DisplayName("예약 시간 추가, 조회, 삭제를 테스트 한다.")
    void reservation_time_api_test() {
        Map<String, String> params = new HashMap<>();
        params.put("startAt", "10:00");

        RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(params)
            .when().post("/times")
            .then().log().all()
            .statusCode(201);

        RestAssured.given().log().all()
            .when().get("/times")
            .then().log().all()
            .statusCode(200)
            .body("size()", is(1));

        RestAssured.given().log().all()
            .when().delete("/times/1")
            .then().log().all()
            .statusCode(204);
    }
}
