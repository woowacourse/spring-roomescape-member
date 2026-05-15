package roomescape.controller;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class GlobalExceptionHandlerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void 존재하지_않는_예약_취소시_404를_반환한다() {
        Map<String, Object> response = RestAssured.given().log().all()
                .when().delete("/reservations/999")
                .then().log().all()
                .statusCode(404)
                .extract().jsonPath().getMap(".");

        assertThat(response.get("message")).isEqualTo("요청한 예약을 찾을 수 없습니다.");
    }

    @Test
    void 존재하지_않는_시간_삭제시_404를_반환한다() {
        Map<String, Object> response = RestAssured.given().log().all()
                .when().delete("/admin/times/999")  // 변경
                .then().log().all()
                .statusCode(404)
                .extract().jsonPath().getMap(".");

        assertThat(response.get("message")).isEqualTo("요청한 시간을 찾을 수 없습니다.");
    }

    @Test
    void 날짜가_null인_예약_생성시_400을_반환한다() {
        ReservationTime time = reservationTimeRepository.save(ReservationTime.of("10:00"));
        Theme theme = themeRepository.save(Theme.of("공포", "desc", "url"));

        Map<String, Object> params = Map.of(
                "name", "브라운",
                "timeId", time.getId(),
                "themeId", theme.getId()
        );

        Map<String, Object> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .extract().jsonPath().getMap(".");

        assertThat(response.get("message")).isEqualTo("날짜를 입력해야 합니다.");
    }

    @Test
    void 이름이_null인_예약_생성시_400을_반환한다() {
        ReservationTime time = reservationTimeRepository.save(ReservationTime.of("10:00"));
        Theme theme = themeRepository.save(Theme.of("공포", "desc", "url"));

        Map<String, Object> params = Map.of(
                "date", "2027-06-01",
                "timeId", time.getId(),
                "themeId", theme.getId()
        );

        Map<String, Object> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .extract().jsonPath().getMap(".");

        assertThat(response.get("message")).isNotNull();
    }
}
