package roomescape.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationControllerE2ETest {

    private static final LocalDate PAST_DATE = LocalDate.now().minusDays(1);
    private static final LocalDate FUTURE_DATE = LocalDate.now().plusDays(1);

    @DisplayName("지난 시점으로 예약하면 422 Unprocessable Entity를 응답한다")
    @Sql("/initialize_theme_and_time.sql")
    @Test
    void 지난_시잠을_예약하면_422를_응답한다() {
        Map<String, Object> requestBodyWithPastDateTime = Map.of(
                "name", "",
                "date", PAST_DATE,
                "timeId", 1,
                "themeId", 1
        );

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(requestBodyWithPastDateTime)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(422);
    }

    @DisplayName("비어 있는 이름으로 예약하면 422 Unprocessable Entity를 응답한다")
    @Sql("/initialize_theme_and_time.sql")
    @Test
    void 비어_있는_이름으로_예약하면_422를_응답한다() {
        Map<String, Object> requestBodyWithEmptyName = Map.of(
                "name", "",
                "date", FUTURE_DATE,
                "timeId", 1,
                "themeId", 1
        );

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(requestBodyWithEmptyName)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(422);
    }

    @DisplayName("같은 날짜/시간/테마로 중복 예약하면 409 Conflict를 응답한다")
    @Sql("/initialize_theme_and_time.sql")
    @Test
    void 날짜와_시간_그리고_테마가_중복된_예약은_409를_응답한다() {
        Map<String, Object> requestBodyWithDuplicatedReservation = Map.of(
                "name", "루드비코",
                "date", FUTURE_DATE,
                "timeId", 1,
                "themeId", 1
        );

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(requestBodyWithDuplicatedReservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(requestBodyWithDuplicatedReservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(409);
    }

    @DisplayName("올바르지 않은 날짜 형식으로 예약하면 422 Unprocessable Entity를 응답한다")
    @Sql("/initialize_theme_and_time.sql")
    @Test
    void 날짜_형식이_올바르지_않게_예약하면_422를_응답한다() {
        String illegalDateFormat = "YYYY/MM/dd";
        Map<String, Object> requestBodyWithIllegalDateFormat = Map.of(
                "name", "루드비코",
                "date", FUTURE_DATE.format(DateTimeFormatter.ofPattern(illegalDateFormat)),
                "timeId", 1,
                "themeId", 1
        );

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(requestBodyWithIllegalDateFormat)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(422);
    }
}
