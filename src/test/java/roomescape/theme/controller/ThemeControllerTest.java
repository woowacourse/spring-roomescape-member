package roomescape.theme.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import roomescape.theme.domain.dto.ThemeReqDto;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ThemeControllerTest {

    @LocalServerPort
    private int port;


    @DisplayName("theme를 생성하면, 201 응답이 도착한다.")
    @Test
    public void createTheme() {
        ThemeReqDto dto = new ThemeReqDto("a", "b", "c");
        
        RestAssured.given().port(port).log().all()
            .contentType(ContentType.JSON).body(dto)
            .when().post("/themes")
            .then().log().all()
            .statusCode(HttpStatus.CREATED.value());
    }

}
