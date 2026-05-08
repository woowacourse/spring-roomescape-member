package roomescape;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RoomescapeApplicationTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void contextLoads() {
    }

    @Test
    @Sql({"/test-truncate.sql", "/test-theme.sql", "/test-reservation-time.sql"})
    void 예약_가능_시간_조회_후_예약하면_해당_시간은_제외된다() {
        LocalDate date = LocalDate.now().plusDays(1);

        List<Integer> times = RestAssured.given()
                .queryParam("date", date.toString())
                .when().get("/themes/1/times")
                .then().statusCode(200)
                .extract().jsonPath().getList("id", Integer.class);

        assertThat(times).contains(1);

        Map<String, Object> request = Map.of(
                "name", "브라운",
                "date", date.toString(),
                "timeId", 1L,
                "themeId", 1L
        );

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/reservations")
                .then().statusCode(201);

        List<Integer> availableTimes = RestAssured.given()
                .queryParam("date", date.toString())
                .when().get("/themes/1/times")
                .then().statusCode(200)
                .extract().jsonPath().getList("id", Integer.class);

        assertThat(availableTimes).doesNotContain(1);
    }
}
