package roomescape;


import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.time.domain.ReservationTime;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserReservationTest {

    @Test
    void 예약_가능한_시간_목록_조회() {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body("{\"startAt\": \"10:00\"}")
                .when().post("/times")
                .then().statusCode(201);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body("{\"startAt\": \"11:00\"}")
                .when().post("/times")
                .then().statusCode(201);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body("{\"startAt\": \"12:00\"}")
                .when().post("/times")
                .then().statusCode(201);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body("{\"startAt\": \"13:00\"}")
                .when().post("/times")
                .then().statusCode(201);


        Map<String, Object> woowaTheme = new HashMap<>();
        woowaTheme.put("name", "우아한 테마");
        woowaTheme.put("description", "우아한테크코스 전용 테마입니다.");
        woowaTheme.put("thumbnailUrl", "https://example.com/image.png");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(woowaTheme)
                .when().post("/themes")
                .then().statusCode(201);

        Map<String, Object> pairTheme = new HashMap<>();
        pairTheme.put("name", "페어 테마");
        pairTheme.put("description", "페어 전용 테마입니다.");
        pairTheme.put("thumbnailUrl", "https://example.com/pair.png");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(pairTheme)
                .when().post("/themes")
                .then().statusCode(201);

        List<ReservationTime> beforeReservationResults = RestAssured.given().log().all()
                .when().get("/times/available?date=2026-05-01&themId=1")
                .then().log().all()
                .statusCode(200).extract()
                .jsonPath().getList(".", ReservationTime.class);

        assertThat(beforeReservationResults.size()).isEqualTo(4);

        Map<String, Object> firstReservation = new HashMap<>();
        firstReservation.put("name", "브라운");
        firstReservation.put("date", "2026-05-01");
        firstReservation.put("timeId", 1);
        firstReservation.put("themeId", 1);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(firstReservation)
                .when().post("/reservations")
                .then().statusCode(201);

        Map<String, Object> secondReservation = new HashMap<>();
        secondReservation.put("name", "포비");
        secondReservation.put("date", "2026-05-02");
        secondReservation.put("timeId", 2);
        secondReservation.put("themeId", 2);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(secondReservation)
                .when().post("/reservations")
                .then().statusCode(201);

        List<ReservationTime> afterReservationResults = RestAssured.given().log().all()
                .when().get("/times/available?date=2026-05-01&themId=1")
                .then().log().all()
                .statusCode(200).extract()
                .jsonPath().getList(".", ReservationTime.class);

        assertThat(afterReservationResults.size()).isEqualTo(3);

        List<ReservationTime> afterReservationResults_2 = RestAssured.given().log().all()
                .when().get("/times/available?date=2026-05-02&themId=1")
                .then().log().all()
                .statusCode(200).extract()
                .jsonPath().getList(".", ReservationTime.class);

        assertThat(afterReservationResults_2.size()).isEqualTo(4);

        List<ReservationTime> afterReservationResults_3 = RestAssured.given().log().all()
                .when().get("/times/available?date=2026-05-01&themId=2")
                .then().log().all()
                .statusCode(200).extract()
                .jsonPath().getList(".", ReservationTime.class);

        assertThat(afterReservationResults_3.size()).isEqualTo(4);

        List<ReservationTime> afterReservationResults_4 = RestAssured.given().log().all()
                .when().get("/times/available?date=2026-05-02&themId=2")
                .then().log().all()
                .statusCode(200).extract()
                .jsonPath().getList(".", ReservationTime.class);

        assertThat(afterReservationResults_4.size()).isEqualTo(3);
    }
}
