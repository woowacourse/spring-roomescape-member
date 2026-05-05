package roomescape.admin;

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
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.web.context.WebApplicationContext;
import roomescape.admin.api.ThemeApiController;
import roomescape.admin.api.dto.ThemeRequest;
import roomescape.admin.api.dto.ThemeResponse;
import roomescape.global.auth.Accessor;
import roomescape.service.ThemeService;
import roomescape.service.command.ThemeRegisterCommand;
import roomescape.service.result.ThemeRegisterResult;

@WebMvcTest(ThemeApiController.class)
class ThemeApiControllerTest extends BaseControllerUnitTest {

    @MockitoBean
    private ThemeService themeService;

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext) {
        mockMvcSetting(webApplicationContext);
    }

    @Test
    void 테마_정보로_테마_추가_요청_시_성공한다() {
        // given
        ThemeRequest body = new ThemeRequest("공포", "공포 방탈출입니다.", "http://image.com/image.png");

        ThemeRegisterResult result = new ThemeRegisterResult(1L, "공포", "공포 방탈출입니다.", "http://image.com/image.png");
        when(themeService.register(any(Accessor.class), any(ThemeRegisterCommand.class))).thenReturn(result);

        ThemeResponse expected = ThemeResponse.from(result);

        // when : admin 헤더를 포함해서 요청한다.
        ThemeResponse response = RestAssuredMockMvc.given().spec(requestSpec()).log().all()
                .body(body)
                .when().post("/admin/themes")
                .then().log().all()
                .status(HttpStatus.CREATED)
                .header("Location", containsString("/admin/themes/1"))
                .extract().as(new TypeRef<>() {
                });

        // then: 요청 성공 시 테마 등록 비즈니스 로직을 수행한다.
        assertThat(response).isEqualTo(expected);
    }

    @ParameterizedTest(name = "요청 정보가 {0} 일 때, 예외 메세지 \"{1}\"가 발생한다.")
    @MethodSource("roomescape.admin.fixture.ThemeApiRequestFixture#themeFailRequestFixture")
    void 테마_추가_요청_시_형식_검증에_실패하면_예외가_발생한다(ThemeRequest body, String exceptionMessage) {
        // given: 실패하는 request body가 주어짐

        // when & then: validation 위반으로 400 Bad Request가 발생한다.
        RestAssuredMockMvc.given().spec(requestSpec()).log().all()
                .body(body)
                .when().post("/admin/themes")
                .then().log().all()
                .status(HttpStatus.BAD_REQUEST)
                .body(containsString(exceptionMessage));
    }

    @Test
    void 관리자가_특정_테마_삭제_요청_시_204_NoContent를_응답한다() {
        // when & then: 관리자 헤더를 포함해서 요청했을 때, 삭제 로직을 수행한다.
        RestAssuredMockMvc.given().spec(requestSpec()).log().all()
                .contentType(MediaType.APPLICATION_JSON)
                .header("role", "ADMIN")
                .when().delete("/admin/themes/1")
                .then().log().all()
                .status(HttpStatus.NO_CONTENT);

        verify(themeService, times(1)).remove(any(Accessor.class), anyLong());
    }
}
