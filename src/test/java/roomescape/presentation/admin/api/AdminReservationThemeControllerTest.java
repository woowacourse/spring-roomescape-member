package roomescape.presentation.admin.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import roomescape.business.domain.member.SignUpMember;
import roomescape.business.domain.reservation.ReservationTheme;
import roomescape.exception.ErrorResponseDto;
import roomescape.persistence.fakerepository.FakeMemberRepository;
import roomescape.persistence.fakerepository.FakeReservationThemeRepository;
import roomescape.presentation.AbstractControllerTest;
import roomescape.presentation.admin.dto.ReservationThemeResponseDto;
import roomescape.presentation.auth.dto.LoginRequestDto;

class AdminReservationThemeControllerTest extends AbstractControllerTest {

    @LocalServerPort
    private int port;

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
        reservationThemeRepository.clear();
    }

    @DisplayName("예약 가능한 테마를 생성하면 상태 코드와 함께 생성된 테마가 반환된다.")
    @Test
    void createReservationTheme() {
        // given
        ReservationTheme reservationTheme = new ReservationTheme("테마1", "설명1", "테마1.jpg");

        // when
        ReservationThemeResponseDto response = RestAssured
                .given()
                .log().all()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .body(reservationTheme)
                .when()
                .post("/admin/themes")
                .then()
                .log().all()
                .statusCode(201)
                .extract()
                .as(ReservationThemeResponseDto.class);

        // then
        assertAll(
                () -> assertThat(response.id()).isNotNull(),
                () -> assertThat(response.name()).isEqualTo("테마1"),
                () -> assertThat(response.description()).isEqualTo("설명1"),
                () -> assertThat(response.thumbnail()).isEqualTo("테마1.jpg")
        );
    }

    @DisplayName("예약 가능한 테마를 생성할 때 중복된 이름이 있으면 예외가 발생한다.")
    @Test
    void createReservationThemeWithDuplicateName() {
        // given
        ReservationTheme reservationTheme1 = new ReservationTheme("테마1", "설명1", "테마1.jpg");
        reservationThemeRepository.add(reservationTheme1);
        ReservationTheme reservationTheme2 = new ReservationTheme("테마1", "설명2", "테마2.jpg");

        // when
        ErrorResponseDto errorResponseDto = RestAssured
                .given()
                .log().all()
                .cookie("token", token)
                .contentType(ContentType.JSON)
                .body(reservationTheme2)
                .when()
                .post("/admin/themes")
                .then()
                .log().all()
                .statusCode(400)
                .extract()
                .as(ErrorResponseDto.class);

        // then
        assertThat(errorResponseDto.message())
                .isEqualTo("동일한 이름의 테마를 추가할 수 없습니다.");
    }

    @DisplayName("예약 가능한 테마를 삭제하면 상태 코드 204가 반환된다.")
    @Test
    void deleteReservationTheme() {
        // given
        ReservationTheme reservationTheme = new ReservationTheme("테마1", "설명1", "테마1.jpg");
        Long id = reservationThemeRepository.add(reservationTheme).getId();

        // when
        RestAssured
                .given()
                .log().all()
                .cookie("token", token)
                .when()
                .delete("/admin/themes/" + id)
                .then()
                .log().all()
                .statusCode(204);

        // then
        assertThat(reservationThemeRepository.findById(id)).isEmpty();
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
