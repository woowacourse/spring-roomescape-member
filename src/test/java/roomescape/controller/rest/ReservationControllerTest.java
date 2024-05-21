package roomescape.controller.rest;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

import io.restassured.RestAssured;
import roomescape.BaseTest;
import roomescape.controller.rest.request.ReservationRequest;
import roomescape.domain.Reservation;
import roomescape.fixture.CookieFixture;

class ReservationControllerTest extends BaseTest {

    private static final int SAVE_COUNT = 5;

    @Autowired
    ReservationController reservationController;

    @Autowired
    CookieFixture cookieFixture;

    @Test
    @DisplayName("저장된 모든 예약을 조회하고 상태코드 200을 응답한다.")
    void findAll() {
        List<Reservation> reservations = RestAssured.given()
                .when().get("/reservations")
                .then()
                .statusCode(200).extract()
                .jsonPath().getList(".", Reservation.class);

        assertThat(reservations.size()).isEqualTo(SAVE_COUNT);
    }

    @DisplayName("저장된 예약을 조회하고 상태코드 200을 응답한다.")
    @ValueSource(longs = {1L, 2L, 3L})
    @ParameterizedTest
    void findById(long id) {
        Reservation reservation = RestAssured.given()
                .when().get("/reservations/" + id)
                .then()
                .statusCode(200).extract()
                .as(Reservation.class);

        assertThat(reservation.id()).isEqualTo(id);
    }

    @Test
    @DisplayName("사용자에 의해 예약을 생성하고 상태코드 201을 응답한다.")
    void createByMember() {
        Reservation reservation = RestAssured.given()
                .contentType("application/json")
                .cookie(cookieFixture.userCookie())
                .body(reservationRequest())
                .when().post("/reservations")
                .then()
                .statusCode(201).extract()
                .as(Reservation.class);
        assertThat(reservation.id()).isEqualTo(6);
    }

    @Test
    @DisplayName("관리자 의해 예약을 생성하고 상태코드 201을 응답한다.")
    void createByAdmin() {
        Reservation reservation = RestAssured.given()
                .contentType("application/json")
                .cookie(cookieFixture.adminCookie())
                .body(reservationRequest())
                .when().post("/admin/reservations")
                .then()
                .statusCode(201).extract()
                .as(Reservation.class);
        assertThat(reservation.id()).isEqualTo(6);
    }

    @Test
    @DisplayName("저장된 예약을 삭제하고 상태코드 204을 응답한다.")
    void delete() {
        RestAssured.given()
                .when().delete("/reservations/" + 1)
                .then()
                .statusCode(204);

        List<Reservation> reservations = RestAssured.given()
                .when().get("/reservations")
                .then()
                .statusCode(200).extract()
                .jsonPath().getList(".", Reservation.class);

        assertThat(reservations.size()).isEqualTo(SAVE_COUNT - 1);
    }

    @Test
    @DisplayName("조건에 맞는 예약을 검색한다.")
    void search() {
        long memberId = 1;
        long themeId = 1;
        String dateFrom = "2024-05-01";
        String dateTo = "2024-05-02";
        String uri = "/admin/reservations"
                + "?memberId=" + memberId
                + "&themeId=" + themeId
                + "&dateFrom=" + dateFrom
                + "&dateTo=" + dateTo;
        List<Reservation> reservations = RestAssured.given()
                .contentType("application/json")
                .cookie(cookieFixture.adminCookie())
                .body(reservationRequest())
                .when().get(uri)
                .then().log().all()
                .statusCode(200).extract()
                .jsonPath().getList(".", Reservation.class);
        assertThat(reservations.size()).isEqualTo(2);
    }

    ReservationRequest reservationRequest() {
        return new ReservationRequest(LocalDate.now().plusDays(1), 1L, 1L, 1L);
    }
}
