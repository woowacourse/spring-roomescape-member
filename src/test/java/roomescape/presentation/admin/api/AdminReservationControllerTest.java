package roomescape.presentation.admin.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import roomescape.business.domain.member.Member;
import roomescape.business.domain.member.MemberRole;
import roomescape.business.domain.member.SignUpMember;
import roomescape.business.domain.reservation.Reservation;
import roomescape.business.domain.reservation.ReservationTheme;
import roomescape.business.domain.reservation.ReservationTime;
import roomescape.exception.ErrorResponseDto;
import roomescape.persistence.fakerepository.FakeMemberRepository;
import roomescape.persistence.fakerepository.FakeReservationRepository;
import roomescape.persistence.fakerepository.FakeReservationThemeRepository;
import roomescape.persistence.fakerepository.FakeReservationTimeRepository;
import roomescape.presentation.AbstractControllerTest;
import roomescape.presentation.admin.dto.AdminReservationRequestDto;
import roomescape.presentation.auth.dto.LoginRequestDto;
import roomescape.presentation.member.dto.ReservationResponseDto;

class AdminReservationControllerTest extends AbstractControllerTest {

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

    private Long memberId;

    private String token;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        memberId = memberRepository.save(new SignUpMember("벨로", "bello@email.com", "password", MemberRole.ADMIN));
        token = testLoginAndReturnToken();
    }

    @AfterEach
    void tearDown() {
        reservationRepository.clear();
        reservationTimeRepository.clear();
        reservationThemeRepository.clear();
    }

    @DisplayName("예약을 생성하면 상태 코드와 함께 생성된 예약이 반환된다.")
    @Test
    void createReservation() {
        // given
        reservationTimeRepository.add(new ReservationTime(LocalTime.of(10, 0)));
        reservationThemeRepository.add(new ReservationTheme("테마1", "설명1", "테마1.jpg"));
        AdminReservationRequestDto requestDto = new AdminReservationRequestDto(
                memberId,
                LocalDate.now().plusDays(1),
                1L,
                1L
        );

        // when
        ReservationResponseDto response = RestAssured
                .given()
                .log().all()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .body(requestDto)
                .when()
                .post("/admin/reservations")
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

    @DisplayName("과거 일시로 예약을 추가할 수 없다.")
    @Test
    void createReservationWithPastDate() {
        // given
        reservationTimeRepository.add(new ReservationTime(LocalTime.of(10, 0)));
        reservationThemeRepository.add(new ReservationTheme("테마1", "설명1", "테마1.jpg"));
        AdminReservationRequestDto requestDto = new AdminReservationRequestDto(
                memberId,
                LocalDate.now().minusDays(1),
                1L,
                1L
        );

        // when
        ErrorResponseDto errorResponseDto = RestAssured
                .given()
                .log().all()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .body(requestDto)
                .when()
                .post("/admin/reservations")
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
                        new Member(1L, "수양", "test@email.com"),
                        LocalDate.now().plusDays(1),
                        reservationTimeRepository.findById(1L).get(),
                        reservationThemeRepository.findById(1L).get()
                )
        );

        AdminReservationRequestDto requestDto = new AdminReservationRequestDto(
                memberId,
                LocalDate.now().plusDays(1),
                1L,
                1L
        );

        // when
        ErrorResponseDto errorResponseDto = RestAssured
                .given()
                .log().all()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .body(requestDto)
                .when()
                .post("/admin/reservations")
                .then()
                .log().all()
                .statusCode(400)
                .extract()
                .as(ErrorResponseDto.class);

        // then
        assertThat(errorResponseDto.message())
                .isEqualTo("해당 날짜와 시간에 이미 예약이 존재합니다.");
    }

    @DisplayName("예약을 삭제한다.")
    @Test
    void deleteReservation() {
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
        // then
        RestAssured
                .given()
                .cookie("token", token)
                .log().all()
                .when()
                .delete("/admin/reservations/" + id)
                .then()
                .log().all()
                .statusCode(204);
        assertThat(reservationRepository.findById(id))
                .isEmpty();
    }

    private String testLoginAndReturnToken() {
        return RestAssured
                .given()
                .log().all()
                .contentType(ContentType.JSON)
                .cookie("token", token)
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
