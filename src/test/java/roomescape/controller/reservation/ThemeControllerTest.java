package roomescape.controller.reservation;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.dto.reservation.ThemeRequest;
import roomescape.dto.reservation.ThemeResponse;
import roomescape.exceptions.EntityNotFoundException;
import roomescape.service.reservation.ThemeService;

@WebMvcTest(ThemeController.class)
class ThemeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ThemeService themeService;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @Test
    @DisplayName("테마 목록을 조회한다.")
    void getThemes() {
        ThemeResponse response1 = new ThemeResponse(1L, "테스트1", "테스트1 설명", "테스트1 썸네일");
        ThemeResponse response2 = new ThemeResponse(2L, "테스트2", "테스트2 설명", "테스트2 썸네일");

        List<ThemeResponse> themeResponses = List.of(
                response1, response2
        );

        given(themeService.getThemes()).willReturn(themeResponses);

        RestAssuredMockMvc.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .status(HttpStatus.OK)
                .body("size()", is(2))
                .body("[0].id", is(1))
                .body("[0].name", is("테스트1"))
                .body("[1].id", is(2))
                .body("[1].name", is("테스트2"));
    }

    @Test
    @DisplayName("테마 관리 페이지 내에서 테마 추가한다.")
    void addTheme() {
        long expectedId = 1L;
        String name = "테스트1";
        String description = "테스트1 설명";
        String thumbnail = "테스트1 썸네일";

        ThemeRequest dto = new ThemeRequest(name, description, thumbnail);

        ThemeResponse response = new ThemeResponse(expectedId, name, description, thumbnail);
        given(themeService.addTheme(dto)).willReturn(response);

        RestAssuredMockMvc.given().log().all()
                .contentType(ContentType.JSON)
                .body(dto)
                .when().post("/themes")
                .then().log().all()
                .status(HttpStatus.CREATED)
                .body("id", is((int) expectedId))
                .body("name", is(name))
                .body("description", is(description));
    }

    @Test
    @DisplayName("존재하는 ID로 삭제 요청 시 성공적으로 처리되어야 한다.")
    void deleteExistingTheme() {
        long themeId = 1L;

        willDoNothing().given(themeService).deleteTheme(themeId);

        RestAssuredMockMvc.given().log().all()
                .when().delete("/themes/" + themeId)
                .then().log().all()
                .status(HttpStatus.NO_CONTENT);

        verify(themeService, times(1)).deleteTheme(themeId);
    }

    @Test
    @DisplayName("존재하지 않는 ID로 삭제 요청 시 404 응답이 반환되어야 한다.")
    void deleteNonExistingTheme() {
        long nonExistingId = 999L;

        willThrow(new EntityNotFoundException("데이터를 찾을 수 없습니다."))
                .given(themeService)
                .deleteTheme(nonExistingId);

        RestAssuredMockMvc.given().log().all()
                .when().delete("/themes/" + nonExistingId)
                .then().log().all()
                .status(HttpStatus.NOT_FOUND);

        verify(themeService, times(1)).deleteTheme(nonExistingId);
    }

    @Test
    @DisplayName("인기 테마 목록을 조회한다.")
    void getPopularThemesByPeriod() {
        String givenName = "우테코 레벨1 탈출";
        List<ThemeResponse> themeResponses = List.of(new ThemeResponse(1L, givenName, "", ""));
        given(themeService.readPopularThemesByPeriod(7, 10)).willReturn(themeResponses);

        RestAssuredMockMvc.given().log().all()
                .when().get("/themes/popular")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1))
                .body("[0].name", is(givenName));
    }
}
