package roomescape.presentation.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import roomescape.application.dto.ReservationRequest;
import roomescape.application.dto.ReservationResponse;
import roomescape.domain.PlayerName;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationCommandRepository;
import roomescape.domain.ReservationQueryRepository;
import roomescape.domain.ReservationTime;
import roomescape.domain.ReservationTimeRepository;
import roomescape.domain.Theme;
import roomescape.domain.ThemeRepository;

public class ReservationAcceptanceTest extends AcceptanceTest {

    @Autowired
    private ReservationCommandRepository reservationCommandRepository;

    @Autowired
    private ReservationQueryRepository reservationQueryRepository;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @DisplayName("예약을 추가한다.")
    @Test
    void createReservationTest() {
        ReservationTime reservationTime = reservationTimeRepository.create(ReservationTimeFixture.defaultValue());
        Theme theme = themeRepository.create(ThemeFixture.defaultValue());
        ReservationRequest request = ReservationRequestFixture.defaultValue(reservationTime, theme);

        ReservationResponse response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .extract()
                .as(ReservationResponse.class);

        assertThat(response.name()).isEqualTo("test");
        assertThat(response.date()).isEqualTo(LocalDate.of(2024, 1, 1));
    }

    @DisplayName("등록된 모든 예약을 조회한다.")
    @Test
    void getAllReservationsTest() {
        ReservationTime savedReservationTime1 = reservationTimeRepository.create(ReservationTimeFixture.of(10, 0));
        ReservationTime savedReservationTime2 = reservationTimeRepository.create(ReservationTimeFixture.of(11, 0));
        Theme theme1 = themeRepository.create(ThemeFixture.defaultValue());
        Theme theme2 = themeRepository.create(ThemeFixture.defaultValue());
        List.of(createReservation("아루", LocalDate.of(2024, 1, 1), savedReservationTime1, theme1),
                        createReservation("이상", LocalDate.of(2024, 12, 25), savedReservationTime2, theme2))
                .forEach(reservationCommandRepository::create);

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(2));
    }

    private static Reservation createReservation(String name, LocalDate date, ReservationTime reservationTime, Theme theme) {
        return new Reservation(new PlayerName(name),
                date,
                reservationTime,
                theme);
    }

    @DisplayName("id로 저장된 예약을 삭제한다.")
    @Test
    void deleteByIdTest() {
        ReservationTime reservationTime = reservationTimeRepository.create(ReservationTimeFixture.of(10, 0));
        Theme theme = themeRepository.create(ThemeFixture.defaultValue());
        Reservation reservation = createReservation("이름", LocalDate.of(2024, 12, 25), reservationTime, theme);
        Reservation savedReservation = reservationCommandRepository.create(reservation);

        RestAssured.given().log().all()
                .when().delete("/reservations/" + savedReservation.getId())
                .then().log().all()
                .statusCode(204);

        assertThat(reservationQueryRepository.findAll()).isEmpty();
    }
}
