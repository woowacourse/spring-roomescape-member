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
import roomescape.service.dto.ReservationTimeRequestDto;
import roomescape.service.dto.ThemeRequestDto;
import roomescape.service.dto.ThemeResponseDto;

@TestExecutionListeners(value = {
        DatabaseCleanupListener.class,
        DependencyInjectionTestExecutionListener.class
})
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ThemeApiControllerTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    private final ReservationTimeRequestDto reservationTimeCreate1 = new ReservationTimeRequestDto("10:00");
    private final ReservationTimeRequestDto reservationTimeCreate2 = new ReservationTimeRequestDto("12:00");
    private final ThemeRequestDto themeCreate1 = new ThemeRequestDto("공포", "공포는 무서워", "hi.jpg");
    private final ThemeRequestDto themeCreate2 = new ThemeRequestDto("추리", "추리는 재밌어", "hi.jpg");
    private final ThemeRequestDto themeCreate3 = new ThemeRequestDto("탈출", "탈출은 빠르게", "hi.jpg");
    private final ReservationRequestDto reservationCreate1 = new ReservationRequestDto("재즈", 1L, "2100-01-01", 1L);
    private final ReservationRequestDto reservationCreate2 = new ReservationRequestDto("러너덕", 1L, "2025-08-05", 1L);
    private final ReservationRequestDto reservationCreate3 = new ReservationRequestDto("재즈덕", 2L, "2025-08-05", 2L);
    private final ReservationRequestDto reservationCreate4 = new ReservationRequestDto("덕", 2L, "2025-08-06", 1L);

    private void create(String path, Object param) {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(param)
                .when().post(path)
                .then().log().all()
                .statusCode(201);
    }

    @DisplayName("테마를 생성하는데 성공하면 응답과 201 상태 코드를 반환한다.")
    @Test
    void return_201_when_create_theme() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(themeCreate1)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201);
    }

    @DisplayName("테마 목록을 조회하는데 성공하면 응답과 200 상태 코드를 반환한다.")
    @Test
    void return_200_when_find_all_themes() {
        create("/themes", themeCreate1);
        create("/themes", themeCreate2);
        List<ThemeResponseDto> actualResponse = RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList(".", ThemeResponseDto.class);

        ThemeResponseDto expectedResponse1 = new ThemeResponseDto(1L, "공포", "공포는 무서워", "hi.jpg");
        ThemeResponseDto expectedResponse2 = new ThemeResponseDto(2L, "추리", "추리는 재밌어", "hi.jpg");

        assertThat(actualResponse)
                .usingRecursiveComparison()
                .isEqualTo(List.of(expectedResponse1, expectedResponse2));
    }

    @DisplayName("인기 테마를 조회하는데 성공하면 응답과 200 상태 코드를 반환한다.")
    @Test
    void return_200_when_find_top_booked_themes() {
        create("/themes", themeCreate1);
        create("/themes", themeCreate2);
        create("/themes", themeCreate3);
        create("/times", reservationTimeCreate1);
        create("/times", reservationTimeCreate2);
        create("/reservations", reservationCreate1);
        create("/reservations", reservationCreate2);
        create("/reservations", reservationCreate3);
        create("/reservations", reservationCreate4);

        List<ThemeResponseDto> actualResponse = RestAssured.given().log().all()
                .when().get("/themes/popular?start-date=2025-08-05&end-date=2025-08-07&count=2")
                .then().log().all()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList(".", ThemeResponseDto.class);

        List<ThemeResponseDto> expectedResponse = List.of(
                new ThemeResponseDto(2L, "추리", "추리는 재밌어", "hi.jpg"),
                new ThemeResponseDto(1L, "공포", "공포는 무서워", "hi.jpg")
        );

        assertThat(actualResponse)
                .usingRecursiveComparison()
                .isEqualTo(expectedResponse);
    }

    @DisplayName("테마를 삭제하는데 성공하면 응답과 204 상태 코드를 반환한다.")
    @Test
    void return_204_when_delete_theme() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(themeCreate1)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .when().delete("/themes/1")
                .then().log().all()
                .statusCode(204);
    }
}
