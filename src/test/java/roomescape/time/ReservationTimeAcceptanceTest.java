package roomescape.time;

import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import roomescape.time.dao.ReservationTimeDao;
import roomescape.time.dto.ReservationTimeRequestDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql(scripts = {"/schema.sql"})
public class ReservationTimeAcceptanceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private ReservationTimeDao reservationTimeDao;
    private final ReservationTimeRequestDto requestDto = new ReservationTimeRequestDto("10:00");

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("시간을 생성할 수 있다.")
    @Test
    void save() {
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(requestDto)
                .when().post("/times")
                .then().statusCode(201);
    }

    @DisplayName("모든 시간을 조회할 수 있다.")
    @Test
    void findAll() {
        reservationTimeDao.save(requestDto.toReservationTime());

        RestAssured.given()
                .when().get("/times")
                .then().statusCode(200)
                .body("size()", is(1));
    }

    @DisplayName("특정 시간을 삭제할 수 있다.")
    @Test
    void delete() {
        reservationTimeDao.save(requestDto.toReservationTime());

        RestAssured.given()
                .when().delete("/times/1")
                .then().statusCode(200);
    }
}
