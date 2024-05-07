package roomescape.presentation.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import roomescape.application.dto.ReservationTimeRequest;
import roomescape.application.dto.ReservationTimeResponse;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReservationTimeRepository;
import roomescape.domain.ThemeRepository;

public class ReservationTimeAcceptanceTest extends AcceptanceTest {

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @DisplayName("예약 시간을 추가한다.")
    @Test
    void createReservationTimeTest() {
        ReservationTimeRequest request = new ReservationTimeRequest(LocalTime.of(10, 0));
        ReservationTimeResponse response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/times")
                .then().log().all()
                .statusCode(201)
                .extract()
                .as(ReservationTimeResponse.class);

        assertThat(response.startAt()).isEqualTo("10:00");
    }

    @DisplayName("등록된 모든 예약 시간을 조회한다.")
    @Test
    void getAllTimesTest() {
        List.of(
                new ReservationTime(LocalTime.of(10, 0)),
                new ReservationTime(LocalTime.of(11, 0))
        ).forEach(reservationTimeRepository::create);

        RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .body("size()", is(2));
    }

    @DisplayName("id를 사용해 예약 시간을 삭제한다.")
    @Test
    void deleteByIdTest() {
        ReservationTime reservationTime = reservationTimeRepository.create(ReservationTimeFixture.of(10, 0));

        RestAssured.given().log().all()
                .when().delete("/times/" + reservationTime.getId())
                .then().log().all()
                .statusCode(204);

        assertThat(reservationTimeRepository.findAll()).isEmpty();
    }

    @DisplayName("예약 가능한 시간을 조회한다.")
    @Test
    void findAvailableTimesTest() {
        reservationTimeRepository.create(ReservationTimeFixture.defaultValue());
        themeRepository.create(ThemeFixture.defaultValue());

        RestAssured.given().log().all()
                .when().get("/times/available?date=2024-05-05&themeId=1")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }
}
