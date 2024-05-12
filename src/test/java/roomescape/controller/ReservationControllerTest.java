package roomescape.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Stream;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import roomescape.domain.Member;
import roomescape.domain.ReservationTime;
import roomescape.domain.Role;
import roomescape.domain.Theme;
import roomescape.domain.repository.MemberRepository;
import roomescape.domain.repository.ReservationTimeRepository;
import roomescape.domain.repository.ThemeRepository;
import roomescape.dto.request.LoginRequest;
import roomescape.dto.request.ReservationRequest;
import roomescape.dto.response.MemberResponse;
import roomescape.dto.response.ReservationResponse;
import roomescape.dto.response.ReservationTimeResponse;
import roomescape.dto.response.ThemeResponse;

class ReservationControllerTest extends BaseControllerTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @BeforeEach
    void setUp() {
        // 테스트 계정, 예약 시간, 테마 데이터 생성
        memberRepository.save(new Member("qwer@naver.com", "1234", "구름", Role.NORMAL));
        reservationTimeRepository.save(new ReservationTime(LocalTime.of(11, 0)));
        themeRepository.save(new Theme("테마 이름", "테마 설명", "https://example.com"));
    }

    @TestFactory
    @DisplayName("예약을 생성, 조회, 삭제한다.")
    Stream<DynamicTest> reservationControllerTests() {
        return Stream.of(
                DynamicTest.dynamicTest("예약을 생성한다.", this::addReservation),
                DynamicTest.dynamicTest("예약을 모두 조회한다.", this::getAllReservations),
                DynamicTest.dynamicTest("예약을 삭제한다.", this::deleteReservationById)
        );
    }

    @TestFactory
    @DisplayName("중복된 예약을 생성하면 실패한다.")
    Stream<DynamicTest> failWhenDuplicatedReservation() {
        return Stream.of(
                DynamicTest.dynamicTest("예약을 생성한다.", this::addReservation),
                DynamicTest.dynamicTest("이미 존재하는 예약을 생성한다.", this::addReservationFailWhenDuplicatedReservation)
        );
    }

    @Test
    @DisplayName("지나간 날짜/시간에 대한 예약은 실패한다.")
    void failWhenDateTimePassed() {
        // 예약 생성 요청
        ReservationRequest request = new ReservationRequest(LocalDate.of(2024, 4, 7), 1L, 1L);

        // 예약 생성
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", getToken("qwer@naver.com", "1234"))
                .body(request)
                .when().post("/reservations")
                .then().log().all()
                .extract();

        // 검증: 응답 상태 코드는 BAD_REQUEST, 에러 메시지는 예상과 일치
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            softly.assertThat(response.body().asString()).contains("지나간 날짜/시간에 대한 예약은 불가능합니다.");
        });
    }

    @Test
    @DisplayName("존재하지 않는 예약을 삭제하면 실패한다.")
    void failWhenNotFoundReservation() {
        // 존재하지 않는 예약 삭제 요청
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().delete("/reservations/1")
                .then().log().all()
                .extract();

        // 검증: 응답 상태 코드는 NOT_FOUND, 에러 메시지는 예상과 일치
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
            softly.assertThat(response.body().asString()).contains("해당 id의 예약이 존재하지 않습니다.");
        });
    }

    private String getToken(String email, String password) {
        // 로그인 요청
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(new LoginRequest(email, password))
                .when().post("/login")
                .then().log().all()
                .extract();

        // 토큰 반환
        return response.cookie("token");
    }

    private void addReservation() {
        // 예약 생성 요청
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        ReservationRequest request = new ReservationRequest(tomorrow, 1L, 1L);

        // 예약 생성
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", getToken("qwer@naver.com", "1234"))
                .body(request)
                .when().post("/reservations")
                .then().log().all()
                .extract();

        // 검증: 응답 상태 코드는 CREATED, 예약 정보는 예상과 일치
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
            softly.assertThat(response.header("Location")).isEqualTo("/reservations/1");
            softly.assertThat(response.as(ReservationResponse.class))
                    .isEqualTo(new ReservationResponse(
                            1L,
                            new MemberResponse(1L, "qwer@naver.com", "구름", Role.NORMAL), tomorrow,
                            new ReservationTimeResponse(1L, LocalTime.of(11, 0)),
                            new ThemeResponse(1L, "테마 이름", "테마 설명", "https://example.com")));
        });
    }

    private void getAllReservations() {
        // 예약 조회 요청
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .extract();

        // 응답으로 받은 예약 목록
        List<ReservationResponse> reservationResponses = response.jsonPath().getList(".", ReservationResponse.class);

        // 검증: 응답 상태 코드는 OK, 예약은 1개, 예약 정보는 예상과 일치
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            softly.assertThat(reservationResponses).hasSize(1);
            softly.assertThat(reservationResponses)
                    .containsExactly(new ReservationResponse(
                            1L,
                            new MemberResponse(1L, "qwer@naver.com", "구름", Role.NORMAL), tomorrow,
                            new ReservationTimeResponse(1L, LocalTime.of(11, 0)),
                            new ThemeResponse(1L, "테마 이름", "테마 설명", "https://example.com")));
        });
    }

    private void deleteReservationById() {
        // 예약 삭제 요청
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().delete("/reservations/1")
                .then().log().all()
                .extract();

        // 검증: 응답 상태 코드는 NO_CONTENT
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        });
    }

    private void addReservationFailWhenDuplicatedReservation() {
        // 중복된 예약 생성 요청
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        ReservationRequest request = new ReservationRequest(tomorrow, 1L, 1L);

        // 중복된 예약 생성 응답
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .cookie("token", getToken("qwer@naver.com", "1234"))
                .body(request)
                .when().post("/reservations")
                .then().log().all()
                .extract();

        // 검증: 응답 상태 코드는 BAD_REQUEST, 에러 메시지는 예상과 일치
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            softly.assertThat(response.body().asString()).contains("해당 날짜/시간에 이미 예약이 존재합니다.");
        });
    }
}
