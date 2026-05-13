package roomescape.controller.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import io.restassured.common.mapper.TypeRef;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.context.WebApplicationContext;
import roomescape.controller.BaseControllerUnitTest;
import roomescape.service.ThemeService;
import roomescape.web.controller.user.ThemeController;
import roomescape.web.dto.theme.ThemeResponse;
import roomescape.web.dto.theme.ThemeResponses;

@WebMvcTest(ThemeController.class)
class ThemeControllerTest extends BaseControllerUnitTest {

    @MockitoBean
    private ThemeService themeService;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) {
        mockMvcSetting(webApplicationContext);
    }

    @Test
    void 테마_목록_조회_요청에_성공하면_200_OK와_정상_응답이_반환된다() {
        // given
        ThemeResponses expected = new ThemeResponses(
                List.of(
                        new ThemeResponse(1L, "공포테마", "어마무시한 공포 테마입니다.", "https://image.com/image.png"),
                        new ThemeResponse(2L, "놀이동산테마", "놀이동산 테마입니다.", "https://image.com/image.png")
                )
        );
        when(themeService.getAllActiveThemesByPaging(any(Integer.class), any(Integer.class))).thenReturn(
                expected.responses());

        // when & then
        ThemeResponses response = RestAssuredMockMvc.given().spec(defaultSpec()).log().all()
                .queryParam("page", "0")
                .queryParam("size", "1")
                .when().get("/api/themes")
                .then().log().all()
                .status(HttpStatus.OK)
                .extract().as(new TypeRef<>() {
                });

        assertThat(response).isEqualTo(expected);
    }

    @Test
    void 테마_목록_조회_요청_시_페이지가_없으면_400_BAD_REQUEST를_응답한다() {
        // when & then
        RestAssuredMockMvc.given().spec(defaultSpec()).log().all()
                .queryParam("size", "1")
                .when().get("/api/themes")
                .then().log().all()
                .status(HttpStatus.BAD_REQUEST)
                .body(containsString("page 파라미터가 누락 되었습니다."));
    }

    @Test
    void 테마_목록_조회_요청_시_조회_개수가_없으면_400_BAD_REQUEST를_응답한다() {
        // when & then
        RestAssuredMockMvc.given().spec(defaultSpec()).log().all()
                .queryParam("page", "0")
                .when().get("/api/themes")
                .then().log().all()
                .status(HttpStatus.BAD_REQUEST)
                .body(containsString("size 파라미터가 누락 되었습니다."));
    }

    @Test
    void 인기_테마_목록_조회_요청에_성공하면_200_OK와_정상_응답이_반환된다() {
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
    void 인기_테마_목록_조회_요청_시_시작일이_없으면_400_BAD_REQUEST를_응답한다() {
        // when & then
        RestAssuredMockMvc.given().spec(defaultSpec()).log().all()
                .queryParam("endDate", "2026-05-06")
                .when().get("/api/themes/popular")
                .then().log().all()
                .status(HttpStatus.BAD_REQUEST)
                .body(containsString("startDate 파라미터가 누락 되었습니다."));
    }

    @Test
    void 인기_테마_목록_조회_요청_시_종료일이_없으면_400_BAD_REQUEST를_응답한다() {
        // when & then
        RestAssuredMockMvc.given().spec(defaultSpec()).log().all()
                .queryParam("startDate", "2026-05-06")
                .when().get("/api/themes/popular")
                .then().log().all()
                .status(HttpStatus.BAD_REQUEST)
                .body(containsString("endDate 파라미터가 누락 되었습니다."));
    }

    @Test
    void 인기_테마_목록_조회_요청_시_조회_개수가_없으면_400_BAD_REQUEST를_응답한다() {
        // when & then
        RestAssuredMockMvc.given().spec(defaultSpec()).log().all()
                .queryParam("startDate", "2026-05-06")
                .queryParam("endDate", "2026-05-09")
                .when().get("/api/themes/popular")
                .then().log().all()
                .status(HttpStatus.BAD_REQUEST)
                .body(containsString("limit 파라미터가 누락 되었습니다."));
    }
}
