package roomescape.controller.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.service.dto.request.LoginRequest;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationApiControllerTest {

    @Autowired
    private ReservationApiController reservationApiController;

    @Test
    @DisplayName("예약 페이지 요청이 정상적으로 수행된다.")
    void moveToReservationPage_Success() {
        String token = RestAssured.given().log().all()
                .body(new LoginRequest("admin@naver.com", "1234"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/login")
                .then().log().cookies().extract().cookie("token");

        RestAssured.given().log().all()
                .cookie("token", token)
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    @DisplayName("관리자 예약 페이지에 권한이 없는 유저는 401을 받는다.")
    void moveToReservationPage_Failure() {
        RestAssured.given().log().all()
                .when().get("/admin/reservation")
                .then().log().all()
                .statusCode(401);
    }

    @Test
    @DisplayName("예약 목록 조회 요청이 정상석으로 수행된다.")
    void selectReservationListRequest_Success() {
        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Test
    @DisplayName("예약 추가, 조회를 정상적으로 수행한다.")
    void ReservationTime_CREATE_READ_Success() {
        Map<String, Object> reservation = Map.of("name", "브라운",
                "date", LocalDate.now().plusDays(2L).toString(),
                "timeId", 1,
                "themeId", 1
        );

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIn0.cDF0ToCw0beej_PcZZQAhLPSXPZp77-iY8CHOJ9kGLk")
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);


        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(2));
    }

    @Test
    @DisplayName("DB에 저장된 예약을 정상적으로 삭제한다.")
    void deleteReservation_InDatabase_Success() {
        RestAssured.given().log().all()
                .when().delete("/reservations/1")
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

        for (Field field : reservationApiController.getClass().getDeclaredFields()) {
            if (field.getType().equals(JdbcTemplate.class)) {
                isJdbcTemplateInjected = true;
                break;
            }
        }

        assertThat(isJdbcTemplateInjected).isFalse();
    }
}
