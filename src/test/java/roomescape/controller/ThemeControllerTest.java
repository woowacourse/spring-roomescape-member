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
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.ThemeRequest;
import roomescape.dto.ThemeResponse;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ReservationTimeRepository;
import roomescape.repository.ThemeRepository;

class ThemeControllerTest extends BaseControllerTest {

    @Autowired
    private ReservationTimeRepository reservationTimeRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private ReservationRepository reservationRepository;

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
    @DisplayName("중복된 이름의 테마를 생성하면 실패한다.")
    Stream<DynamicTest> failWhenDuplicatedTheme() {
        return Stream.of(
                DynamicTest.dynamicTest("테마를 생성한다.", this::addTheme),
                DynamicTest.dynamicTest("이미 존재하는 이름의 테마를 생성한다.", this::addThemeFailWhenDuplicatedTheme)
        );
    }

    @Test
    @DisplayName("존재하지 않는 테마를 삭제하면 실패한다.")
    void deleteThemeByIdFailWhenNotFoundId() {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().delete("/themes/1")
                .then().log().all()
                .extract();

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
            softly.assertThat(response.body().asString()).contains("해당 id의 테마가 존재하지 않습니다.");
        });
    }

    @Test
    @DisplayName("이미 사용 중인 테마을 삭제하면 실패한다.")
    void deleteThemeByIdFailWhenUsedTheme() {
        ReservationTime reservationTime = reservationTimeRepository.save(new ReservationTime(LocalTime.of(10, 30)));
        Theme theme = themeRepository.save(new Theme("테마 이름", "테마 설명", "https://example.com"));
        reservationRepository.save(new Reservation("구름", LocalDate.of(2024, 4, 9), reservationTime, theme));

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().delete("/themes/1")
                .then().log().all()
                .extract();

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            softly.assertThat(response.body().asString()).contains("해당 테마를 사용하는 예약이 존재합니다.");
        });
    }

    void addTheme() {
        ThemeRequest request = new ThemeRequest("테마 이름", "테마 설명", "https://example.com/image.jpg");

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/themes")
                .then().log().all()
                .extract();

        ThemeResponse themeResponse = response.as(ThemeResponse.class);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
            softly.assertThat(response.header("Location")).isEqualTo("/themes/1");
            softly.assertThat(themeResponse)
                    .isEqualTo(new ThemeResponse(1L, "테마 이름", "테마 설명", "https://example.com/image.jpg"));
        });
    }

    void addThemeFailWhenDuplicatedTheme() {
        ThemeRequest request = new ThemeRequest("테마 이름", "테마 설명-2", "https://example.com/image-2.jpg");

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/themes")
                .then().log().all()
                .extract();

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            softly.assertThat(response.body().asString()).contains("해당 이름의 테마는 이미 존재합니다.");
        });

    }

    void getAllThemes() {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .extract();

        List<ThemeResponse> themeResponses = response.jsonPath()
                .getList(".", ThemeResponse.class);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            softly.assertThat(themeResponses).hasSize(1);
            softly.assertThat(themeResponses)
                    .containsExactly(new ThemeResponse(1L, "테마 이름", "테마 설명", "https://example.com/image.jpg"));
        });
    }

    void deleteThemeById() {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().delete("/themes/1")
                .then().log().all()
                .extract();

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        });
    }
}
