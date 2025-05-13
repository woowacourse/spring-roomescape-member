package roomescape.presentation.member.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

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
import roomescape.business.domain.member.Member;
import roomescape.business.domain.member.SignUpMember;
import roomescape.business.domain.reservation.Reservation;
import roomescape.business.domain.reservation.ReservationTheme;
import roomescape.business.domain.reservation.ReservationTime;
import roomescape.persistence.fakerepository.FakeMemberRepository;
import roomescape.persistence.fakerepository.FakeReservationRepository;
import roomescape.persistence.fakerepository.FakeReservationThemeRepository;
import roomescape.persistence.fakerepository.FakeReservationTimeRepository;
import roomescape.presentation.AbstractControllerTest;
import roomescape.presentation.member.dto.LoginRequestDto;
import roomescape.presentation.member.dto.ReservationRequestDto;
import roomescape.presentation.member.dto.ReservationResponseDto;

class ReservationControllerTest extends AbstractControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private FakeReservationRepository reservationRepository;

    @Autowired
    private FakeReservationTimeRepository reservationTimeRepository;

    @Autowired
    private FakeReservationThemeRepository reservationThemeRepository;

    @Autowired
    private FakeMemberRepository memberRepository;

    private String token;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        memberRepository.save(new SignUpMember("벨로", "bello@email.com", "password"));
        token = testLoginAndReturnToken();
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
                        new Member(1L, "수양", "test@email.com"),
                        LocalDate.now().plusDays(1),
                        reservationTimeRepository.findById(1L).get(),
                        reservationThemeRepository.findById(1L).get()
                )
        );

        // when
        List<ReservationResponseDto> response = RestAssured
                .given()
                .log().all()
                .cookie("token", token)
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
                () -> assertThat(response.get(0).member().name()).isEqualTo("수양"),
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
                        new Member(1L, "수양", "test@email.com"),
                        LocalDate.now().plusDays(1),
                        reservationTimeRepository.findById(1L).get(),
                        reservationThemeRepository.findById(1L).get()
                )
        ).getId();

        // when
        ReservationResponseDto response = RestAssured
                .given()
                .log().all()
                .cookie("token", token)
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
                () -> assertThat(response.member().name()).isEqualTo("수양"),
                () -> assertThat(response.date()).isEqualTo(LocalDate.now().plusDays(1)),
                () -> assertThat(response.time().startAt()).isEqualTo(LocalTime.of(10, 0)),
                () -> assertThat(response.theme().name()).isEqualTo("테마1"),
                () -> assertThat(response.theme().description()).isEqualTo("설명1"),
                () -> assertThat(response.theme().thumbnail()).isEqualTo("테마1.jpg")
        );
    }

    @DisplayName("로그인한 사용자가 예약한다.")
    @Test
    void createReservation() {
        // given
        reservationTimeRepository.add(new ReservationTime(LocalTime.of(10, 0)));
        reservationThemeRepository.add(new ReservationTheme("테마1", "설명1", "테마1.jpg"));
        Long timeId = reservationTimeRepository.findById(1L).get().getId();
        Long themeId = reservationThemeRepository.findById(1L).get().getId();
        ReservationRequestDto reservationRequestDto = new ReservationRequestDto(
                LocalDate.now().plusDays(1),
                timeId,
                themeId
        );

        // when
        ReservationResponseDto response = RestAssured
                .given()
                .log().all()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .body(reservationRequestDto)
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
                () -> assertThat(response.member().name()).isEqualTo("벨로"),
                () -> assertThat(response.date()).isEqualTo(LocalDate.now().plusDays(1)),
                () -> assertThat(response.time().startAt()).isEqualTo(LocalTime.of(10, 0)),
                () -> assertThat(response.theme().name()).isEqualTo("테마1"),
                () -> assertThat(response.theme().description()).isEqualTo("설명1"),
                () -> assertThat(response.theme().thumbnail()).isEqualTo("테마1.jpg")
        );
    }

    @DisplayName("로그인한 사용자가 과거 일시로 예약할 경우 예외가 발생한다.")
    @Test
    void shouldThrowException_WhenCreatingReservationWithPastDate() {
        // given
        reservationTimeRepository.add(new ReservationTime(LocalTime.of(10, 0)));
        reservationThemeRepository.add(new ReservationTheme("테마1", "설명1", "테마1.jpg"));
        Long timeId = reservationTimeRepository.findById(1L).get().getId();
        Long themeId = reservationThemeRepository.findById(1L).get().getId();
        ReservationRequestDto reservationRequestDto = new ReservationRequestDto(
                LocalDate.now().minusDays(1),
                timeId,
                themeId
        );

        // when
        // then
        RestAssured
                .given()
                .log().all()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .body(reservationRequestDto)
                .when()
                .post("/reservations")
                .then()
                .log().all()
                .statusCode(400);
    }

    @DisplayName("로그인한 사용자가 이미 예약된 경우(날짜, 시간, 테마) 예약할 수 없다.")
    @Test
    void shouldThrowException_WhenCreatingReservationWithDuplicatedReservation() {
        // given
        reservationTimeRepository.add(new ReservationTime(LocalTime.of(10, 0)));
        reservationThemeRepository.add(new ReservationTheme("테마1", "설명1", "테마1.jpg"));
        Long timeId = reservationTimeRepository.findById(1L).get().getId();
        Long themeId = reservationThemeRepository.findById(1L).get().getId();
        ReservationRequestDto reservationRequestDto = new ReservationRequestDto(
                LocalDate.now().plusDays(1),
                timeId,
                themeId
        );
        RestAssured
                .given()
                .log().all()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .body(reservationRequestDto)
                .when()
                .post("/reservations")
                .then()
                .log().all()
                .statusCode(201);

        // when
        // then
        RestAssured
                .given()
                .log().all()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .body(reservationRequestDto)
                .when()
                .post("/reservations")
                .then()
                .log().all()
                .statusCode(400);
    }

    private String testLoginAndReturnToken() {
        return RestAssured
                .given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(new LoginRequestDto("bello@email.com", "password"))
                .when()
                .post("/login")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .cookie("token");
    }
}
