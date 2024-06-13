package roomescape.controller;


import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.application.ThemeService;
import roomescape.domain.member.Member;
import roomescape.domain.member.MemberName;
import roomescape.domain.member.MemberRole;
import roomescape.domain.theme.Theme;
import roomescape.dto.theme.ThemeRequest;
import roomescape.fixture.ThemeFixture;
import roomescape.infrastructure.JwtTokenProvider;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ThemeControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private ThemeService themeService;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void 테마를_생성한다() throws Exception {
        ThemeRequest request = new ThemeRequest("테마 : 공포 공포", "무서웡", "https://image.jpg");
        Mockito.when(themeService.save(request))
                .thenReturn(new Theme(1L, request.name(), request.description(), request.thumbnail()));

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .cookie(makeToken())
                .body(request)
                .when().post("/themes")
                .then()
                .statusCode(201)
                .body("id", equalTo(1))
                .body("name", equalTo(request.name()))
                .body("description", equalTo(request.description()))
                .body("thumbnail", equalTo(request.thumbnail()));
    }

    @Test
    void 전체_테마를_조회한다() throws Exception {
        List<Theme> themes = List.of(ThemeFixture.theme(), ThemeFixture.theme());
        Mockito.when(themeService.getThemes()).thenReturn(themes);

        ExtractableResponse<Response> response = RestAssured
                .given()
                .cookie(makeToken())
                .when().get("/themes")
                .then()
                .statusCode(200)
                .extract();
        JsonPath result = response.jsonPath();

        assertAll(
                () -> assertEquals(result.getString("[0].id"), themes.get(0).getId().toString()),
                () -> assertEquals(result.getString("[0].name"), themes.get(0).getName()),
                () -> assertEquals(result.getString("[0].description"), themes.get(0).getDescription()),
                () -> assertEquals(result.getString("[0].thumbnail"), themes.get(0).getThumbnail())
        );
    }

    @Test
    void 테마를_삭제한다() throws Exception {
        long id = 1L;
        Mockito.doNothing().when(themeService).delete(id);

        RestAssured
                .given()
                .cookie(makeToken())
                .when().delete("/themes/" + id)
                .then()
                .statusCode(204);
    }

    @Test
    void 썸네일_URL이_올바르지_않으면_Bad_Request_상태를_반환한다() throws Exception {
        ThemeRequest request = new ThemeRequest("테마 : 공포 공포", "무서웡", "잘못된 썸네일 url");

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .cookie(makeToken())
                .body(request)
                .when().post("/themes")
                .then()
                .statusCode(400);
    }

    private String makeToken() {
        Member member = new Member(1L, new MemberName("레모네"), "lemone@gmail.com", "lemon12", MemberRole.ADMIN);
        return jwtTokenProvider.createToken(member);
    }
}
