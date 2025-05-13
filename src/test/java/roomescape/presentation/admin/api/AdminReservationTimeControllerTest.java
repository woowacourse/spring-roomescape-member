package roomescape.presentation.admin.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import roomescape.business.domain.member.SignUpMember;
import roomescape.business.domain.reservation.ReservationTime;
import roomescape.exception.ErrorResponseDto;
import roomescape.persistence.fakerepository.FakeMemberRepository;
import roomescape.persistence.fakerepository.FakeReservationTimeRepository;
import roomescape.presentation.AbstractControllerTest;
import roomescape.presentation.admin.dto.ReservationTimeRequestDto;
import roomescape.presentation.admin.dto.ReservationTimeResponseDto;
import roomescape.presentation.auth.dto.LoginRequestDto;

class AdminReservationTimeControllerTest extends AbstractControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private FakeReservationTimeRepository reservationTimeRepository;

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
        reservationTimeRepository.clear();
    }

    @DisplayName("예약 가능한 시간을 생성하면 상태 코드와 함께 생성된 예약 시간이 반환된다.")
    @Test
    void createReservationTime() {
        // given
        ReservationTimeRequestDto requestDto = new ReservationTimeRequestDto(LocalTime.of(20, 0));

        // when
        ReservationTimeResponseDto response = RestAssured
                .given()
                .log().all()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .body(requestDto)
                .when()
                .post("/admin/times")
                .then()
                .log().all()
                .statusCode(201)
                .extract()
                .as(ReservationTimeResponseDto.class);

        // then
        assertAll(
                () -> assertThat(response.id())
                        .isNotNull(),
                () -> assertThat(response.startAt())
                        .isEqualTo(LocalTime.of(20, 0))
        );
    }

    @DisplayName("예약 가능한 시간을 생성할 때 이미 존재하는 시간인 경우 예외가 발생한다.")
    @Test
    void shouldThrowException_WhenTimeAlreadyExists() {
        // given
        ReservationTime reservationTime = new ReservationTime(LocalTime.of(20, 0));
        reservationTimeRepository.add(reservationTime);
        ReservationTimeRequestDto requestDto = new ReservationTimeRequestDto(LocalTime.of(20, 0));

        // when
        ErrorResponseDto errorResponseDto = RestAssured
                .given()
                .log().all()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .body(requestDto)
                .when()
                .post("/admin/times")
                .then()
                .log().all()
                .statusCode(400)
                .extract()
                .as(ErrorResponseDto.class);

        // then
        assertThat(errorResponseDto.message())
                .isEqualTo("해당 시간은 이미 존재합니다.");
    }

    @DisplayName("예약 가능한 시간을 삭제한다.")
    @Test
    void deleteReservationTime() {
        // given
        ReservationTime reservationTime = new ReservationTime(LocalTime.of(14, 0));
        Long id = reservationTimeRepository.add(reservationTime).getId();

        // when
        RestAssured
                .given()
                .log().all()
                .cookie("token", token)
                .when()
                .delete("/admin/times/" + id)
                .then()
                .log().all()
                .statusCode(204);

        // then
        assertThat(reservationTimeRepository.findById(id))
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
