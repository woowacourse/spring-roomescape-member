package roomescape.presentation.member.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import roomescape.business.domain.reservation.ReservationTheme;
import roomescape.persistence.fakerepository.FakeReservationThemeRepository;
import roomescape.presentation.AbstractControllerTest;
import roomescape.presentation.admin.dto.ReservationThemeResponseDto;

class ReservationThemeControllerTest extends AbstractControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private FakeReservationThemeRepository reservationThemeRepository;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @AfterEach
    void tearDown() {
        reservationThemeRepository.clear();
    }

    @DisplayName("예약 가능한 테마 목록을 조회하면 상태 코드와 함께 테마 목록이 반환된다.")
    @Test
    void getReservationThemes() {
        // given
        reservationThemeRepository.add(new ReservationTheme("테마1", "설명1", "테마1.jpg"));
        reservationThemeRepository.add(new ReservationTheme("테마2", "설명2", "테마2.jpg"));

        // when
        List<ReservationThemeResponseDto> response = RestAssured
                .given()
                .log().all()
                .contentType(ContentType.JSON)
                .when()
                .get("/themes")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList(".", ReservationThemeResponseDto.class);

        // then
        assertAll(
                () -> assertThat(response).hasSize(2),
                () -> assertThat(response.get(0).name()).isEqualTo("테마1"),
                () -> assertThat(response.get(1).name()).isEqualTo("테마2"),
                () -> assertThat(response.get(0).description()).isEqualTo("설명1"),
                () -> assertThat(response.get(1).description()).isEqualTo("설명2"),
                () -> assertThat(response.get(0).thumbnail()).isEqualTo("테마1.jpg"),
                () -> assertThat(response.get(1).thumbnail()).isEqualTo("테마2.jpg")
        );
    }
}
