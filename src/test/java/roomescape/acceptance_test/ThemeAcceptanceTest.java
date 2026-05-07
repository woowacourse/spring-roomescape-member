package roomescape.acceptance_test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.controller.dto.ThemeCreateRequest;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ThemeAcceptanceTest {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("테마 생성 후 목록에서 조회된다.")
    public void scenario1() throws JsonProcessingException {
        Integer themeId = createTheme("brown", "설명", "섬네일");


        given().log().all()
        .when()
            .get("/themes")
        .then().log().all()
            .statusCode(200)
            .body("themes.id", hasItem(themeId))
            .body("themes.name", hasItem("brown"))
            .body("themes.description", hasItem("설명"))
            .body("themes.thumbnail", hasItem("섬네일"));
    }

    @Test
    @DisplayName("테마 생성 후 삭제하면 목록에서 사라진다.")
    public void scenario2() throws JsonProcessingException {
        Integer themeId = createTheme("brown", "설명", "섬네일");

        given().log().all()
                .pathParam("id", themeId)
                .when()
                .delete("/admin/themes/{id}")
                .then().log().all()
                .statusCode(204);

        given().log().all()
                .when()
                .get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("themes.id", not(hasItem(themeId)));

    }

    private Integer createTheme(String name, String description, String thumbnail) throws JsonProcessingException {
        ThemeCreateRequest request = new ThemeCreateRequest(name, description, thumbnail);
        return given().log().all()
                .contentType(ContentType.JSON)
                .body(objectMapper.writeValueAsString(request))
                .when()
                .post("/admin/themes")
                .then().log().all()
                .statusCode(201)
                .body("name", equalTo(name))
                .body("description", equalTo(description))
                .body("thumbnail", equalTo(thumbnail))
                .extract().path("id");
    }




}
