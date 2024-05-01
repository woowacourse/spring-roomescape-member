package roomescape.theme;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeDao;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ThemeControllerTest {

    @Autowired
    private ThemeDao themeDao;

    @Test
    @DisplayName("저장된 모든 테마를 조회하고 상태코드 200을 응답한다.")
    void findAll() {
        insertTheme("브라운", "테마 설명1", "thumbnail1.jpg");
        insertTheme("구구", "테마 설명2", "thumbnail2.jpg");
        assertThemeCountIsEqualTo(2);

        List<Theme> themes = RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(200).extract()
                .jsonPath().getList(".", Theme.class);

        Integer count = themeDao.findAll().size();
        assertThat(themes.size()).isEqualTo(count);
    }

    @Test
    @DisplayName("테마를 추가하고 상태코드 201을 응답한다.")
    void create() {
        insertTheme("브라운", "테마 설명1", "thumbnail1.jpg");
        assertThemeCountIsEqualTo(1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(new Theme(0L, "구구", "테마 설명2", "thumbnail2.jpg"))
                .when().post("/themes")
                .then().log().all()
                .statusCode(201);

        assertThemeCountIsEqualTo(2);
    }

    @Test
    @DisplayName("저장된 테마를 삭제하고 상태코드 204를 응답한다.")
    void delete() {
        long id = insertThemeAndGetId("브라운", "테마 설명1", "thumbnail1.jpg");
        assertThemeCountIsEqualTo(1);

        RestAssured.given().log().all()
                .when().delete("/themes/" + id)
                .then().log().all()
                .statusCode(204);

        assertThemeCountIsEqualTo(0);
    }

    void insertTheme(String name, String description, String thumbnail) {
        insertThemeAndGetId(name, description, thumbnail);
    }

    long insertThemeAndGetId(String name, String description, String thumbnail) {
        return themeDao.save(new Theme(0L, name, description, thumbnail)).id();
    }

    void assertThemeCountIsEqualTo(int count) {
        assertThat(count).isEqualTo(themeDao.findAll().size());
    }
}
