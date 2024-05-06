package roomescape.controller;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import roomescape.repository.DatabaseCleanupListener;
import roomescape.service.dto.ReservationRequestDto;
import roomescape.service.dto.ReservationResponseDto;
import roomescape.service.dto.ReservationTimeRequestDto;
import roomescape.service.dto.ReservationTimeResponseDto;
import roomescape.service.dto.ThemeRequestDto;
import roomescape.service.dto.ThemeResponseDto;

@TestExecutionListeners(value = {
        DatabaseCleanupListener.class,
        DependencyInjectionTestExecutionListener.class
})
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ReservationApiControllerTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        initializeTimesData();
        initializeThemeData();
    }

    private void initializeThemeData() {
        ThemeRequestDto param = new ThemeRequestDto("공포", "공포는 무서워", "hi.jpg");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(param)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201);
    }

    private static void initializeTimesData() {
        ReservationTimeRequestDto param = new ReservationTimeRequestDto("10:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(param)
                .when().post("/times")
                .then().log().all()
                .statusCode(201);
    }

    private final ReservationRequestDto reservationCreate1 = new ReservationRequestDto("재즈", 1L, "2100-08-05", 1L);

    @DisplayName("예약을 생성하는데 성공하면 응답과 201 상태 코드를 반환한다.")
    @Test
    void return_200_when_create_reservation() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationCreate1)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);
    }

    @DisplayName("예약 목록을 조회하는데 성공하면 응답과 200 상태 코드를 반환한다.")
    @Test
    void return_200_when_find_all_reservations() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationCreate1)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        List<ReservationResponseDto> actualResponse = RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList(".", ReservationResponseDto.class);

        ReservationResponseDto expectedResponse = new ReservationResponseDto(
                1L, "재즈",
                new ThemeResponseDto(1L, "공포", "공포는 무서워", "hi.jpg"),
                "2100-08-05",
                new ReservationTimeResponseDto(1L, "10:00")
        );

        assertThat(actualResponse)
                .usingRecursiveComparison()
                .isEqualTo(List.of(expectedResponse));
    }

    @DisplayName("예약을 삭제하는데 성공하면 응답과 204 상태 코드를 반환한다.")
    @Test
    void return_204_when_delete_reservation() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationCreate1)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(204);
    }
}
