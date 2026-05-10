package roomescape.controller.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import io.restassured.common.mapper.TypeRef;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.context.WebApplicationContext;
import roomescape.controller.BaseControllerUnitTest;
import roomescape.service.ThemeService;
import roomescape.web.controller.user.ThemeController;
import roomescape.web.dto.ThemeResponse;
import roomescape.web.dto.ThemeResponses;
import roomescape.web.dto.ThemeTimeResponses;
import roomescape.web.dto.ThemeTimesResponse;

@WebMvcTest(ThemeController.class)
class ThemeControllerTest extends BaseControllerUnitTest {

    @MockitoBean
    private ThemeService themeService;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) {
        mockMvcSetting(webApplicationContext);
    }

    @Test
    void 테마_시간대_조회_요청에_성공하면_정상_응답이_반환된다() {
        // given
        ThemeTimeResponses expected = new ThemeTimeResponses(
                List.of(
                        new ThemeTimesResponse(1L, LocalTime.of(10, 0), true),
                        new ThemeTimesResponse(2L, LocalTime.of(11, 0), true)
                )
        );
        when(themeService.getThemeReservationStatus(anyLong(), any(LocalDate.class))).thenReturn(expected.responses());

        // when & then
        ThemeTimeResponses response = RestAssuredMockMvc.given().spec(defaultSpec()).log().all()
                .queryParam("date", "2026-05-06")
                .when().get("/api/themes/1")
                .then().log().all()
                .status(HttpStatus.OK)
                .extract().as(new TypeRef<>() {
                });

        assertThat(response).isEqualTo(expected);
    }

    @ParameterizedTest
    @ValueSource(longs = {-1, 0})
    void 테마_조회_요청시_테마_식별자가_양수가_아니라면_400_BAD_REQUEST(Long invalidThemeId) {
        // when & then
        RestAssuredMockMvc.given().spec(defaultSpec()).log().all()
                .queryParam("date", "2026-05-06")
                .when().get("/api/themes/" + invalidThemeId)
                .then().log().all()
                .status(HttpStatus.BAD_REQUEST)
                .body(containsString("테마 조회 식별자는 양수여야 합니다."));
    }

    @Test
    void 테마_목록_조회_요청에_성공하면_정상_응답이_반환된다() {
        // given
        ThemeResponses expected = new ThemeResponses(
                List.of(
                        new ThemeResponse(1L, "공포테마", "어마무시한 공포 테마입니다.", "https://image.com/image.png"),
                        new ThemeResponse(2L, "놀이동산테마", "놀이동산 테마입니다.", "https://image.com/image.png")
                )
        );
        when(themeService.getAllActiveThemes()).thenReturn(expected.responses());

        // when & then
        ThemeResponses response = RestAssuredMockMvc.given().spec(defaultSpec()).log().all()
                .when().get("/api/themes")
                .then().log().all()
                .status(HttpStatus.OK)
                .extract().as(new TypeRef<>() {
                });

        assertThat(response).isEqualTo(expected);
    }

    @Test
    void 인기_테마_목록_조회_요청에_성공하면_정상_응답이_반환된다() {
        // given
        ThemeResponses expected = new ThemeResponses(
                List.of(
                        new ThemeResponse(1L, "공포테마", "어마무시한 공포 테마입니다.", "https://image.com/image.png"),
                        new ThemeResponse(2L, "놀이동산테마", "놀이동산 테마입니다.", "https://image.com/image.png")
                )
        );
        when(themeService.getPopularThemes(any(LocalDate.class), any(LocalDate.class), any(Integer.class))).thenReturn(
                expected.responses());

        // when & then
        ThemeResponses response = RestAssuredMockMvc.given().spec(defaultSpec()).log().all()
                .queryParam("startDate", "2026-05-06")
                .queryParam("endDate", "2026-05-09")
                .queryParam("limit", "10")
                .when().get("/api/themes/popular")
                .then().log().all()
                .status(HttpStatus.OK)
                .extract().as(new TypeRef<>() {
                });

        assertThat(response).isEqualTo(expected);
    }

    @Test
    void 인기_테마_목록_조회_요청_시_시작일이_없으면_400_BAD_REQUEST() {
        // when & then
        RestAssuredMockMvc.given().spec(defaultSpec()).log().all()
                .queryParam("endDate", "2026-05-06")
                .when().get("/api/themes/popular")
                .then().log().all()
                .status(HttpStatus.BAD_REQUEST)
                .body(containsString("startDate 파라미터가 누락 되었습니다."));
    }

    @Test
    void 인기_테마_목록_조회_요청_시_종료일이_없으면_400_BAD_REQUEST() {
        // when & then
        RestAssuredMockMvc.given().spec(defaultSpec()).log().all()
                .queryParam("startDate", "2026-05-06")
                .when().get("/api/themes/popular")
                .then().log().all()
                .status(HttpStatus.BAD_REQUEST)
                .body(containsString("endDate 파라미터가 누락 되었습니다."));
    }
}
