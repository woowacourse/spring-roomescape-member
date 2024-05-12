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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Role;
import roomescape.domain.Theme;
import roomescape.domain.repository.MemberRepository;
import roomescape.domain.repository.ReservationRepository;
import roomescape.domain.repository.ReservationTimeRepository;
import roomescape.domain.repository.ThemeRepository;
import roomescape.dto.request.ReservationTimeRequest;
import roomescape.dto.response.AvailableReservationTimeResponse;
import roomescape.dto.response.ReservationTimeResponse;

class ReservationTimeControllerTest extends BaseControllerTest {

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private MemberRepository memberRepository;

    @TestFactory
    @DisplayName("예약 시간을 생성, 조회, 삭제한다.")
    Stream<DynamicTest> addReservationTimeAndGetAndDelete() {
        return Stream.of(
                DynamicTest.dynamicTest("예약 시간을 생성한다.", this::addReservationTime),
                DynamicTest.dynamicTest("예약 시간을 모두 조회한다.", this::getAllReservationTimes),
                DynamicTest.dynamicTest("예약 시간을 삭제한다.", this::deleteReservationTimeById)
        );
    }

    @TestFactory
    @DisplayName("중복된 예약 시간을 생성하면 실패한다.")
    Stream<DynamicTest> failWhenDuplicatedTime() {
        return Stream.of(
                DynamicTest.dynamicTest("예약 시간을 생성한다.", this::addReservationTime),
                DynamicTest.dynamicTest("이미 존재하는 예약 시간을 생성한다.", this::addReservationTimeFailWhenDuplicatedTime)
        );
    }

    @Test
    @DisplayName("존재하지 않는 예약 시간을 삭제하면 실패한다.")
    void deleteReservationTimeByIdFailWhenNotFoundId() {
        // 존재하지 않는 예약 시간 삭제 요청
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().delete("/times/1")
                .then().log().all()
                .extract();

        // 검증: 응답 상태코드는 404, 메시지는 "해당 id의 시간이 존재하지 않습니다."
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
            softly.assertThat(response.body().asString()).contains("해당 id의 시간이 존재하지 않습니다.");
        });
    }

    @Test
    @DisplayName("이미 사용 중인 예약 시간을 삭제하면 실패한다.")
    void deleteReservationTimeByIdFailWhenUsedTime() {
        // 예약 시간, 회원, 테마, 예약 데이터 생성
        Member member = memberRepository.save(new Member(1L, "example@example.com", "password", "구름", Role.NORMAL));
        ReservationTime reservationTime = reservationTimeRepository.save(new ReservationTime(LocalTime.of(10, 30)));
        Theme theme = themeRepository.save(new Theme("테마 이름", "테마 설명", "https://example.com"));
        reservationRepository.save(new Reservation(member, LocalDate.of(2024, 4, 9), reservationTime, theme));

        // 사용 중인 예약 시간 삭제 요청
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().delete("/times/1")
                .then().log().all()
                .extract();

        // 검증: 응답 상태코드는 400, 메시지는 "해당 시간을 사용하는 예약이 존재합니다."
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            softly.assertThat(response.body().asString()).contains("해당 시간을 사용하는 예약이 존재합니다.");
        });
    }

    @Test
    @DisplayName("이용가능한 시간들을 조회한다.")
    void getAvailableReservationTimes() {
        // 예약 시간 데이터 생성
        ReservationTime notBookedTime = reservationTimeRepository.save(new ReservationTime(LocalTime.of(10, 0)));
        ReservationTime alreadyBookedTime = reservationTimeRepository.save(new ReservationTime(LocalTime.of(11, 0)));

        // 회원 생성, 테마 생성, 예약 데이터 생성
        Member member = memberRepository.save(new Member("example@example.com", "password", "구름", Role.NORMAL));
        Theme theme = themeRepository.save(new Theme("테마 이름", "테마 설명", "https://example.com"));
        reservationRepository.save(new Reservation(member, LocalDate.of(2024, 4, 9), alreadyBookedTime, theme));

        // 이용가능한 시간 조회 요청
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .param("date", "2024-04-09")
                .param("themeId", 1L)
                .when().get("/times/available")
                .then().log().all()
                .extract();

        // 이용가능한 시간 데이터
        List<AvailableReservationTimeResponse> availableReservationTimeResponses = response.jsonPath()
                .getList(".", AvailableReservationTimeResponse.class);

        // 검증: 응답 상태코드는 200, 예약된 가능 여부 일치하는지 확인
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            softly.assertThat(availableReservationTimeResponses).hasSize(2);
            softly.assertThat(availableReservationTimeResponses).containsExactlyInAnyOrder(
                    new AvailableReservationTimeResponse(
                            notBookedTime.getId(),
                            notBookedTime.getStartAt(),
                            false),
                    new AvailableReservationTimeResponse(
                            alreadyBookedTime.getId(),
                            alreadyBookedTime.getStartAt(),
                            true)
            );
        });
    }

    private void addReservationTime() {
        // 예약 시간 생성 요청
        ReservationTimeRequest request = new ReservationTimeRequest(LocalTime.of(10, 30));

        // 예약 시간 생성 응답
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/times")
                .then().log().all()
                .extract();

        // 검증: 응답 상태 코드는 CREATED, 예약 시간 정보는 예상과 일치
        ReservationTimeResponse reservationTimeResponse = response.as(ReservationTimeResponse.class);

        // 검증: 응답 상태 코드는 CREATED, 예약 시간 정보는 예상과 일치
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
            softly.assertThat(response.header("Location")).isEqualTo("/times/1");
            softly.assertThat(reservationTimeResponse).isEqualTo(new ReservationTimeResponse(1L, LocalTime.of(10, 30)));
        });
    }

    private void addReservationTimeFailWhenDuplicatedTime() {
        // 중복된 예약 시간 생성 요청
        ReservationTimeRequest request = new ReservationTimeRequest(LocalTime.of(10, 30));

        // 중복된 예약 시간 생성 응답
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/times")
                .then().log().all()
                .extract();

        // 검증: 응답 상태 코드는 BAD_REQUEST, 에러 메시지는 "해당 시간은 이미 존재합니다."
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            softly.assertThat(response.body().asString()).contains("해당 시간은 이미 존재합니다.");
        });
    }

    private void getAllReservationTimes() {
        // 예약 시간 조회 요청
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .extract();

        // 응답으로 받은 예약 시간 목록
        List<ReservationTimeResponse> reservationTimeResponses = response.jsonPath()
                .getList(".", ReservationTimeResponse.class);

        // 검증: 응답 상태 코드는 OK, 예약 시간은 1개, 예약 시간 정보는 예상과 일치
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            softly.assertThat(reservationTimeResponses).hasSize(1);
            softly.assertThat(reservationTimeResponses)
                    .containsExactly(new ReservationTimeResponse(1L, LocalTime.of(10, 30)));
        });
    }

    private void deleteReservationTimeById() {
        // 예약 시간 삭제 요청
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().delete("/times/1")
                .then().log().all()
                .extract();

        // 검증: 응답 상태 코드는 NO_CONTENT
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        });
    }
}
