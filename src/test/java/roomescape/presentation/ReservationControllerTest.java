package roomescape.presentation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import roomescape.business.Reservation;
import roomescape.business.ReservationTheme;
import roomescape.business.ReservationTime;
import roomescape.exception.ErrorResponseDto;
import roomescape.persistence.fakerepository.FakeReservationRepository;
import roomescape.persistence.fakerepository.FakeReservationThemeRepository;
import roomescape.persistence.fakerepository.FakeReservationTimeRepository;
import roomescape.presentation.dto.ReservationRequestDto;
import roomescape.presentation.dto.ReservationResponseDto;

class ReservationControllerTest extends AbstractControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private FakeReservationRepository reservationRepository;

    @Autowired
    private FakeReservationTimeRepository reservationTimeRepository;

    @Autowired
    private FakeReservationThemeRepository reservationThemeRepository;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @AfterEach
    void tearDown() {
        reservationRepository.clear();
        reservationTimeRepository.clear();
        reservationThemeRepository.clear();
    }

    @DisplayName("예약 목록을 조회하면 상태 코드와 함께 예약 목록이 반환된다.")
    @Test
    void getAllReservations() {
        // given
        reservationTimeRepository.add(new ReservationTime(LocalTime.of(10, 0)));
        reservationThemeRepository.add(new ReservationTheme("테마1", "설명1", "테마1.jpg"));
        reservationThemeRepository.add(new ReservationTheme("테마2", "설명2", "테마2.jpg"));
        reservationRepository.add(new Reservation(
                        "예약1",
                        LocalDate.now().plusDays(1),
                        reservationTimeRepository.findById(1L).get(),
                        reservationThemeRepository.findById(1L).get()
                )
        );

        // when
        List<ReservationResponseDto> response = RestAssured
                .given()
                .log().all()
                .contentType(ContentType.JSON)
                .when()
                .get("/reservations")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getList(".", ReservationResponseDto.class);

        // then
        assertAll(
                () -> assertThat(response).hasSize(1),
                () -> assertThat(response.get(0).name()).isEqualTo("예약1"),
                () -> assertThat(response.get(0).date()).isEqualTo(LocalDate.now().plusDays(1)),
                () -> assertThat(response.get(0).time().startAt()).isEqualTo(LocalTime.of(10, 0)),
                () -> assertThat(response.get(0).theme().name()).isEqualTo("테마1"),
                () -> assertThat(response.get(0).theme().description()).isEqualTo("설명1"),
                () -> assertThat(response.get(0).theme().thumbnail()).isEqualTo("테마1.jpg")
        );
    }

    @DisplayName("예약을 조회하면 상태 코드와 함께 예약이 반환된다.")
    @Test
    void getReservationById() {
        // given
        reservationTimeRepository.add(new ReservationTime(LocalTime.of(10, 0)));
        reservationThemeRepository.add(new ReservationTheme("테마1", "설명1", "테마1.jpg"));
        Long id = reservationRepository.add(new Reservation(
                        "예약1",
                        LocalDate.now().plusDays(1),
                        reservationTimeRepository.findById(1L).get(),
                        reservationThemeRepository.findById(1L).get()
                )
        );

        // when
        ReservationResponseDto response = RestAssured
                .given()
                .log().all()
                .contentType(ContentType.JSON)
                .when()
                .get("/reservations/" + id)
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .as(ReservationResponseDto.class);

        // then
        assertAll(
                () -> assertThat(response.id()).isNotNull().isEqualTo(id),
                () -> assertThat(response.name()).isEqualTo("예약1"),
                () -> assertThat(response.date()).isEqualTo(LocalDate.now().plusDays(1)),
                () -> assertThat(response.time().startAt()).isEqualTo(LocalTime.of(10, 0)),
                () -> assertThat(response.theme().name()).isEqualTo("테마1"),
                () -> assertThat(response.theme().description()).isEqualTo("설명1"),
                () -> assertThat(response.theme().thumbnail()).isEqualTo("테마1.jpg")
        );
    }

    @DisplayName("예약을 생성하면 상태 코드와 함께 생성된 예약이 반환된다.")
    @Test
    void createReservation() {
        // given
        reservationTimeRepository.add(new ReservationTime(LocalTime.of(10, 0)));
        reservationThemeRepository.add(new ReservationTheme("테마1", "설명1", "테마1.jpg"));
        ReservationRequestDto requestDto = new ReservationRequestDto(
                "예약1",
                LocalDate.now().plusDays(1),
                1L,
                1L
        );

        // when
        ReservationResponseDto response = RestAssured
                .given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(requestDto)
                .when()
                .post("/reservations")
                .then()
                .log().all()
                .statusCode(201)
                .extract()
                .as(ReservationResponseDto.class);

        // then
        assertAll(
                () -> assertThat(response.id()).isNotNull(),
                () -> assertThat(response.name()).isEqualTo("예약1"),
                () -> assertThat(response.date()).isEqualTo(LocalDate.now().plusDays(1)),
                () -> assertThat(response.time().startAt()).isEqualTo(LocalTime.of(10, 0)),
                () -> assertThat(response.theme().name()).isEqualTo("테마1"),
                () -> assertThat(response.theme().description()).isEqualTo("설명1"),
                () -> assertThat(response.theme().thumbnail()).isEqualTo("테마1.jpg")
        );
    }

    @DisplayName("과거 일시로 예약을 추가할 수 없다.")
    @Test
    void createReservationWithPastDate() {
        // given
        reservationTimeRepository.add(new ReservationTime(LocalTime.of(10, 0)));
        reservationThemeRepository.add(new ReservationTheme("테마1", "설명1", "테마1.jpg"));
        ReservationRequestDto requestDto = new ReservationRequestDto(
                "예약1",
                LocalDate.now().minusDays(1),
                1L,
                1L
        );

        // when
        ErrorResponseDto errorResponseDto = RestAssured
                .given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(requestDto)
                .when()
                .post("/reservations")
                .then()
                .log().all()
                .statusCode(400)
                .extract()
                .as(ErrorResponseDto.class);

        // then
        assertThat(errorResponseDto.message())
                .isEqualTo("과거 일시로 예약을 생성할 수 없습니다.");
    }

    @DisplayName("이미 예약된 시간에 예약을 추가할 수 없다.")
    @Test
    void createReservationWithAlreadyReservedTime() {
        // given
        reservationTimeRepository.add(new ReservationTime(LocalTime.of(10, 0)));
        reservationThemeRepository.add(new ReservationTheme("테마1", "설명1", "테마1.jpg"));
        reservationRepository.add(new Reservation(
                        "예약1",
                        LocalDate.now().plusDays(1),
                        reservationTimeRepository.findById(1L).get(),
                        reservationThemeRepository.findById(1L).get()
                )
        );

        ReservationRequestDto requestDto = new ReservationRequestDto(
                "예약2",
                LocalDate.now().plusDays(1),
                1L,
                1L
        );

        // when
        ErrorResponseDto errorResponseDto = RestAssured
                .given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(requestDto)
                .when()
                .post("/reservations")
                .then()
                .log().all()
                .statusCode(400)
                .extract()
                .as(ErrorResponseDto.class);

        // then
        assertThat(errorResponseDto.message())
                .isEqualTo("해당 날짜와 시간에 이미 예약이 존재합니다.");
    }

    @DisplayName("예약을 삭제하면 상태 코드 204가 반환된다.")
    @Test
    void deleteReservation() {
        // given
        reservationTimeRepository.add(new ReservationTime(LocalTime.of(10, 0)));
        reservationThemeRepository.add(new ReservationTheme("테마1", "설명1", "테마1.jpg"));
        Long id = reservationRepository.add(new Reservation(
                        "예약1",
                        LocalDate.now().plusDays(1),
                        reservationTimeRepository.findById(1L).get(),
                        reservationThemeRepository.findById(1L).get()
                )
        );

        // when
        // then
        RestAssured
                .given()
                .log().all()
                .when()
                .delete("/reservations/" + id)
                .then()
                .log().all()
                .statusCode(204);
        assertThat(reservationRepository.findById(id))
                .isEmpty();
    }
}
