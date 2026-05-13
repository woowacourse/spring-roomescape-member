package roomescape.controller.admin;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.restassured.common.mapper.TypeRef;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.context.WebApplicationContext;
import roomescape.controller.BaseControllerUnitTest;
import roomescape.service.ThemeService;
import roomescape.web.controller.admin.AdminThemeController;
import roomescape.web.dto.theme.ThemeRequest;
import roomescape.web.dto.theme.ThemeResponse;

@WebMvcTest(AdminThemeController.class)
class AdminThemeControllerTest extends BaseControllerUnitTest {

    @MockitoBean
    private ThemeService themeService;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) {
        mockMvcSetting(webApplicationContext);
    }

    @Test
    void 관리자가_테마_추가_요청에_성공하면_201_CREATED와_정상_응답을_반환한다() {
        // given
        ThemeRequest request = new ThemeRequest("공포", "공포 방탈출입니다.", "http://image.com/image.png");

        ThemeResponse expected = new ThemeResponse(1L, "공포", "공포 방탈출입니다.", "http://image.com/image.png");
        when(themeService.register(any(ThemeRequest.class))).thenReturn(expected);

        // when
        ThemeResponse response = RestAssuredMockMvc.given().spec(adminSpec()).log().all()
                .body(request)
                .when().post("/api/admin/themes")
                .then().log().all()
                .status(HttpStatus.CREATED)
                .header("Location", containsString("/api/admin/themes/1"))
                .extract().as(new TypeRef<>() {
                });

        // then
        assertThat(response).isEqualTo(expected);
    }

    @ParameterizedTest(name = "요청 정보가 {0} 일 때, 예외 메세지 \"{1}\"가 발생한다.")
    @MethodSource("roomescape.controller.fixture.AdminThemeRequestFixture#themeFailRequestFixture")
    void 관리자가_테마_추가_요청_시_형식_검증에_실패하면_예외가_발생한다(ThemeRequest body, String exceptionMessage) {
        // given

        // when & then
        RestAssuredMockMvc.given().spec(adminSpec()).log().all()
                .body(body)
                .when().post("/api/admin/themes")
                .then().log().all()
                .status(HttpStatus.BAD_REQUEST)
                .body(containsString(exceptionMessage));
    }

    @Test
    void 관리자가_특정_테마_삭제_요청_시_204_NO_CONTENT를_응답한다() {
        // when & then
        RestAssuredMockMvc.given().spec(adminSpec()).log().all()
                .contentType(MediaType.APPLICATION_JSON)
                .header("role", "ADMIN")
                .when().delete("/api/admin/themes/1")
                .then().log().all()
                .status(HttpStatus.NO_CONTENT);

        verify(themeService, times(1)).remove(anyLong());
    }

    @ParameterizedTest
    @ValueSource(longs = {0, -1})
    void 관리자가_잘못된_테마_식별자로_삭제_요청_시_예외가_발생한다(Long invalidId) {
        // when & then
        RestAssuredMockMvc.given().spec(adminSpec()).log().all()
                .contentType(MediaType.APPLICATION_JSON)
                .header("role", "ADMIN")
                .when().delete("/api/admin/themes/" + invalidId)
                .then().log().all()
                .status(HttpStatus.BAD_REQUEST)
                .body(containsString("테마 식별자는 양수여야 합니다."));
    }
}
