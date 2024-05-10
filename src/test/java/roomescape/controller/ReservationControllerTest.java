package roomescape.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/truncate-data.sql")
public class ReservationControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ReservationController reservationController;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("예약 페이지 요청이 정상적으로 수행된다.")
    void moveToReservationPage_Success() {
        RestAssured.given().log().all()
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("예약 목록 조회 요청이 정상석으로 수행된다.")
    void selectReservationListRequest_Success() {
        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }

    @Test
    @DisplayName("예약 추가, 조회를 정상적으로 수행한다.")
    @Sql(scripts = {"/truncate-data.sql", "/member-data.sql"})
    void ReservationTime_CREATE_READ_Success() {
        Map<String, String> params = Map.of(
                "email", "naknak@example.com",
                "password", "nak123"
        );

        String header = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/login")
                .then().log().all()
                .statusCode(200)
                .extract()
                .header("Set-Cookie");

        String[] parts = header.split(";");
        String token = parts[0].split("=")[1];

        Map<String, String> time = Map.of(
                "startAt", "10:00"
        );

        Map<String, Object> theme = Map.of(
                "name", "테마",
                "description", "테마 설명",
                "thumbnail", "테마 썸네일"
        );

        String timeLocation = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(time)
                .when().post("/times")
                .then().log().all()
                .statusCode(201)
                .extract().header("Location");

        String themeLocation = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(theme)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201)
                .extract().header("Location");

        Long timeId = Long.parseLong(timeLocation.substring(timeLocation.lastIndexOf("/") + 1));
        Long themeId = Long.parseLong(themeLocation.substring(themeLocation.lastIndexOf("/") + 1));

        Map<String, Object> reservation = Map.of(
                "date", LocalDate.now().plusDays(1L).toString(),
                "timeId", timeId,
                "themeId", themeId
        );

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);


        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Test
    @DisplayName("DB에 저장된 예약을 정상적으로 삭제한다.")
    @Sql(scripts = {"/truncate-data.sql", "/member-data.sql"})
    void deleteReservation_InDatabase_Success() {
        Map<String, String> params = Map.of(
                "email", "naknak@example.com",
                "password", "nak123"
        );

        String header = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/login")
                .then().log().all()
                .statusCode(200)
                .extract()
                .header("Set-Cookie");

        String[] parts = header.split(";");
        String token = parts[0].split("=")[1];

        Map<String, String> time = Map.of(
                "startAt", "10:00"
        );

        Map<String, Object> theme = Map.of(
                "name", "테마",
                "description", "테마 설명",
                "thumbnail", "테마 썸네일"
        );

        String timeLocation = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(time)
                .when().post("/times")
                .then().log().all()
                .statusCode(201)
                .extract().header("Location");

        String themeLocation = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(theme)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201)
                .extract().header("Location");

        Long timeId = Long.parseLong(timeLocation.substring(timeLocation.lastIndexOf("/") + 1));
        Long themeId = Long.parseLong(themeLocation.substring(themeLocation.lastIndexOf("/") + 1));

        Map<String, Object> reservation = Map.of(
                "date", LocalDate.now().plusDays(1L).toString(),
                "timeId", timeId,
                "themeId", themeId
        );

        String reservationLocation = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", token)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .extract().header("Location");

        String reservationId = reservationLocation.substring(reservationLocation.lastIndexOf("/") + 1);

        RestAssured.given().log().all()
                .when().delete("/reservations/" + reservationId)
                .then().log().all()
                .statusCode(204);

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }

    @Test
    @DisplayName("데이터베이스 관련 로직을 컨트롤러에서 분리하였다.")
    void layerRefactoring() {
        boolean isJdbcTemplateInjected = false;

        for (Field field : reservationController.getClass().getDeclaredFields()) {
            if (field.getType().equals(JdbcTemplate.class)) {
                isJdbcTemplateInjected = true;
                break;
            }
        }

        assertThat(isJdbcTemplateInjected).isFalse();
    }
}
