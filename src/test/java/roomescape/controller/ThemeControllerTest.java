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
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.Member;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Role;
import roomescape.domain.Theme;
import roomescape.domain.repository.MemberRepository;
import roomescape.domain.repository.ReservationRepository;
import roomescape.domain.repository.ReservationTimeRepository;
import roomescape.domain.repository.ThemeRepository;
import roomescape.dto.request.ThemeRequest;
import roomescape.dto.response.ThemeResponse;

class ThemeControllerTest extends BaseControllerTest {

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private MemberRepository memberRepository;

    @TestFactory
    @DisplayName("테마를 생성, 조회, 삭제한다.")
    Stream<DynamicTest> themeControllerTests() {
        return Stream.of(
                DynamicTest.dynamicTest("테마를 생성한다.", this::addTheme),
                DynamicTest.dynamicTest("테마를 모두 조회한다.", this::getAllThemes),
                DynamicTest.dynamicTest("테마를 삭제한다.", this::deleteThemeById)
        );
    }

    @TestFactory
    @DisplayName("테마 생성 시 중복된 이름의 테마를 생성하면 실패한다.")
    Stream<DynamicTest> failWhenDuplicatedTheme() {
        return Stream.of(
                DynamicTest.dynamicTest("테마를 생성한다.", this::addTheme),
                DynamicTest.dynamicTest("이미 존재하는 이름의 테마를 생성한다.", this::addThemeFailWhenDuplicatedTheme)
        );
    }

    @Test
    @DisplayName("존재하지 않는 테마를 삭제하면 실패한다.")
    void deleteThemeByIdFailWhenNotFoundId() {
        // 테마 삭제 요청
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().delete("/themes/1")
                .then().log().all()
                .extract();

        // 검증: 응답 상태 코드는 NOT_FOUND, 에러 메시지는 예상과 일치
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
            softly.assertThat(response.body().asString()).contains("해당 id의 테마가 존재하지 않습니다.");
        });
    }

    @Test
    @DisplayName("이미 사용 중인 테마을 삭제하면 실패한다.")
    void deleteThemeByIdFailWhenUsedTheme() {
        // 테마, 회원, 예약 시간, 예약 데이터 생성
        Member member = memberRepository.save(new Member("example@example.com", "password", "구름", Role.NORMAL));
        ReservationTime reservationTime = reservationTimeRepository.save(new ReservationTime(LocalTime.of(10, 30)));
        Theme theme = themeRepository.save(new Theme("테마 이름", "테마 설명", "https://example.com"));
        reservationRepository.save(new Reservation(member, LocalDate.of(2024, 4, 9), reservationTime, theme));

        // 테마 삭제 요청
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().delete("/themes/1")
                .then().log().all()
                .extract();

        // 검증: 응답 상태 코드는 BAD_REQUEST, 에러 메시지는 예상과 일치
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            softly.assertThat(response.body().asString()).contains("해당 테마를 사용하는 예약이 존재합니다.");
        });
    }

    @Test
    @DisplayName("인기있는 테마들을 조회하면, 5개의 테마가 반환되어야 한다.")
    @Sql("/reservations.sql")
        // 일주일간 5종류의 테마 예약 데이터가 있음
    void getPopularThemes() {
        // 테마 조회 요청
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().get("/themes/popular?startDate=2024-04-01&endDate=2024-04-07")
                .then().log().all()
                .extract();

        // 응답으로 받은 테마 목록
        List<ThemeResponse> themeResponses = response.jsonPath()
                .getList(".", ThemeResponse.class);

        // 검증: 응답 상태 코드는 OK, 테마는 5개, 각 테마의 정보는 예상과 일치
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            softly.assertThat(themeResponses).hasSize(5);
            softly.assertThat(themeResponses).containsExactly(
                    new ThemeResponse(5L, "테마5", "테마5 설명", "https://via.placeholder.com/150/56a8c2"),
                    new ThemeResponse(4L, "테마4", "테마4 설명", "https://via.placeholder.com/150/30f9e7"),
                    new ThemeResponse(3L, "테마3", "테마3 설명", "https://via.placeholder.com/150/24f355"),
                    new ThemeResponse(2L, "테마2", "테마2 설명", "https://via.placeholder.com/150/771796"),
                    new ThemeResponse(1L, "테마1", "테마1 설명", "https://via.placeholder.com/150/92c952")
            );
        });
    }

    private void addTheme() {
        // 테마 생성 요청
        ThemeRequest request = new ThemeRequest("테마 이름", "테마 설명", "https://example.com/image.jpg");

        // 테마 생성 응답
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/themes")
                .then().log().all()
                .extract();

        // 검증: 응답 상태 코드는 CREATED, 테마 정보는 예상과 일치
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
            softly.assertThat(response.header("Location")).isEqualTo("/themes/1");
            softly.assertThat(response.as(ThemeResponse.class))
                    .isEqualTo(new ThemeResponse(1L, "테마 이름", "테마 설명", "https://example.com/image.jpg"));
        });
    }

    private void addThemeFailWhenDuplicatedTheme() {
        // 중복된 이름의 테마 생성 요청
        ThemeRequest request = new ThemeRequest("테마 이름", "테마 설명-2", "https://example.com/image-2.jpg");

        // 중복된 이름의 테마 생성 응답
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/themes")
                .then().log().all()
                .extract();

        // 검증: 응답 상태 코드는 BAD_REQUEST, 에러 메시지는 예상과 일치
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            softly.assertThat(response.body().asString()).contains("해당 이름의 테마는 이미 존재합니다.");
        });

    }

    private void getAllThemes() {
        // 테마 조회 요청
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .extract();

        // 응답으로 받은 테마 목록
        List<ThemeResponse> themeResponses = response.jsonPath()
                .getList(".", ThemeResponse.class);

        // 검증: 응답 상태 코드는 OK, 테마는 1개, 테마의 정보는 예상과 일치
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            softly.assertThat(themeResponses).hasSize(1);
            softly.assertThat(themeResponses)
                    .containsExactly(new ThemeResponse(1L, "테마 이름", "테마 설명", "https://example.com/image.jpg"));
        });
    }

    private void deleteThemeById() {
        // 테마 삭제 요청
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().delete("/themes/1")
                .then().log().all()
                .extract();

        // 검증: 응답 상태 코드는 NO_CONTENT
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        });
    }
}
