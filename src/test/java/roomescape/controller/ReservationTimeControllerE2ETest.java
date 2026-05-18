package roomescape.controller;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationTimeControllerE2ETest {

    @DisplayName("존재하는 모든 시간 슬롯을 조회한다")
    @Sql("/initialize_theme_and_time.sql")
    @Test
    void 존재하는_모든_시간_슬롯을_조회한다() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(3));
    }

    @DisplayName("특정 테마의 특정 날짜에서 예약된 시간대를 조회한다")
    @Sql("/data.sql")
    @Test
    void 테마와_날짜를_조건으로_예약된_시간대를_찾아_리턴한다() {
        String requestParamFormat = "/times/reserved?themeId=%d&selectedDate=%s";

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().get(requestParamFormat.formatted(3, LocalDate.now().minusDays(2)))
                .then().log().all()
                .statusCode(200)
                .body("size()", is(2));

    }
}