package roomescape.closeddate;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.support.IntegrationTestSupport;

class AdminClosedDateApiTest extends IntegrationTestSupport {

    @Test
    @DisplayName("관리자는 휴무일을 등록할 수 있다")
    void createClosedDate() {
        String date = LocalDate.now().plusDays(1).toString();

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(Map.of("date", date))
                .when().post("/admin/closed-dates")
                .then().log().all()
                .statusCode(201)
                .body("date", is(date));
    }

    @Test
    @DisplayName("관리자는 휴무일 목록을 조회할 수 있다")
    void findClosedDates() {
        String date = LocalDate.now().plusDays(1).toString();
        createClosedDate(date);

        RestAssured.given().log().all()
                .when().get("/admin/closed-dates")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1))
                .body("[0].date", is(date));
    }

    @Test
    @DisplayName("관리자는 휴무일을 삭제할 수 있다")
    void deleteClosedDate() {
        String date = LocalDate.now().plusDays(1).toString();
        Long closedDateId = createClosedDate(date);

        RestAssured.given().log().all()
                .when().delete("/admin/closed-dates/{id}", closedDateId)
                .then().log().all()
                .statusCode(204);
    }

    private Long createClosedDate(String date) {
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .body(Map.of("date", date))
                .when().post("/admin/closed-dates")
                .then()
                .statusCode(201)
                .extract().jsonPath().getLong("id");
    }
}
